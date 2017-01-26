package com.jnaka.golf.dao;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.jnaka.dao.ObjectDao;
import com.jnaka.dao.XmlDao;
import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.CourseTee;
import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Kp;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.SeasonSummary;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;
import com.jnaka.golf.domain.Tournament.Type;
import com.jnaka.reports.AbstractDocumentFactory;

@Repository("DomMapper")
public class DomMapper {

	private static final String YES_STRING = "yes";

	private static final String NO_STRING = "no";

	public static final String STRING_SEPARATOR = ",";

	private static final String XML_SEASON_ID = "seasonID";
	private static final String XML_DB_ID = "tournamentID";
	private static final String XML_COURSE = "course";
	private static final String XML_DATE = "date";
	private static final String XML_COURSE_SLOPE = "slope";
	private static final String XML_COURSE_RATING = "rating";

	private static final String XML_TOURNAMENT = "Tournament";
	private static final String XML_WINNER = "winner";
	private static final String XML_PLAYER = "player";
	private static final String XML_FLIGHT = "flight";
	private static final String XML_FINISH = "finish";
	private static final String XML_NET = "net";
	private static final String XML_EARNINGS = "earnings";
	private static final String XML_POINTS = "points";
	private static final String XML_PLAYER_ID = "playerID";

	private static final String XML_KP = "kp";
	private static final String XML_HOLE = "hole";

	private static final String XML_ROUND = "round";
	private static final String XML_ROUND_ID = "roundID";
	private static final String XML_FRONT = "front";
	private static final String XML_BACK = "back";
	private static final String XML_TOTAL = "total";
	private static final String XML_FRONT_NET = "frontNet";
	private static final String XML_BACK_NET = "backNet";
	private static final String XML_TOTAL_NET = "totalNet";
	private static final String XML_HDCP = "hdcp";
	private static final String XML_SCORES = "scores";
	private static final String XML_ADJUSTED = "adjusted";
	private static final String XML_ACCEPTED = "accepted";

	private static final String XML_COURSE_ELEMENT = "Course";
	private static final String XML_COURSE_ID = "courseID";
	private static final String XML_NAME = "name";
	private static final String XML_DIRECTIONS = "directions";
	private static final String XML_PHONE = "phone";

	private static final String XML_TEE = "tee";
	private static final String XML_RATING = "rating";
	private static final String XML_SLOPE = "slope";
	private static final String XML_HOLE_PARS = "holePars";
	private static final String XML_HOLE_HANDICAPS = "holeHdcp";

	private static final String XML_ACTIVE = "active";
	private static final String XML_LAST_NAME = "firstName";
	private static final String XML_FIRST_NAME = "lastName";

	private static final String XML_SEASON_SUMMARY = "Season";
	private static final String XML_ATTENDANCE = "attendance";
	private static final String XML_KPS = "kps";

	@Autowired
	@Qualifier("PlayerDao")
	private ObjectDao<Player> playerDao;

	@Autowired
	@Qualifier("CourseDao")
	private CourseDao courseDao;

	public Winner mapWinner(Node xml) {
		Winner winner = new Winner();
		return winner;
	}

	public Element map(Winner winner) {
		Element elem = DocumentHelper.createElement(XML_WINNER);
		Player player = winner.getRound().getPlayer();
		Element playerElement = elem.addElement(XML_PLAYER);
		playerElement.addAttribute(XML_PLAYER_ID, player.getId().toString());
		playerElement.setText(player.getFirstName() + " " + player.getLastName());
		elem.addElement(XML_FLIGHT).setText(winner.getRound().getFlight().name());
		elem.addElement(XML_POINTS).setText(winner.getPoints().toString());
		elem.addElement(XML_FINISH).setText(winner.getFinish());
		elem.addElement(XML_EARNINGS).setText(this.convertMoney(winner.getEarnings()));
		elem.addElement(XML_NET).setText(Integer.toString(winner.getRound().getTotalNet().intValue()));
		return elem;
	}

	/**
	 * <pre>
	 * <kp>
	 * 		<player playerID="1">Bruce Kaneshiro</player>
	 * 		<flight>A</flight>
	 * 		<hole>A02</hole>
	 * 	</kp>
	 * </pre>
	 * 
	 * @param xml
	 * @return
	 */
	public Kp mapKp(Node xml) {
		Kp kp = new Kp();
		kp.setPlayer(this.lookupPlayer(xml));
		String text = xml.selectSingleNode(XML_FLIGHT).getText();
		if (StringUtils.isNotEmpty(text)) {
			kp.setFlight(Flight.valueOf(text));
		}
		kp.setHole(xml.selectSingleNode(XML_HOLE).getText());
		return kp;
	}

	public Element map(Kp kp) {
		Element elem = DocumentHelper.createElement(XML_KP);
		Player player = kp.getPlayer();
		Element playerElement = elem.addElement(XML_PLAYER);
		playerElement.addAttribute(XML_PLAYER_ID, player.getId().toString());
		playerElement.setText(this.getPlayerName(player));

		elem.addElement(XML_FLIGHT).setText(ObjectUtils.toString(kp.getFlight(), StringUtils.EMPTY));
		elem.addElement(XML_HOLE).setText(kp.getHole().toString());
		return elem;
	}

	/**
	 * <pre>
	 * <round roundID="7">
	 * 		<player playerID="8">Darcy Bruton</player>
	 * 	  	<flight>A</flight>
	 * 	  	<hdcp>16</hdcp>
	 * 		<front>42</front>
	 * 		<back>42</back>
	 * 	  	<total>84</total>
	 * 	  	<frontNet>34.0</frontNet>
	 * 		<backNet>34.0</backNet>
	 * 	  	<totalNet>68.0</totalNet>
	 * 	  	<scores>6,5,4,5,5,4,4,5,4,5,4,4,6,3,5,5,4,6</scores>
	 * </round>
	 * </pre>
	 */

	public Element map(Round round) {
		Element elem = DocumentHelper.createElement(XML_ROUND);
		elem.addAttribute(XML_ROUND_ID, ObjectUtils.toString(round.getId()));

		Player player = round.getPlayer();
		Element playerElement = elem.addElement(XML_PLAYER);
		playerElement.addAttribute(XML_PLAYER_ID, ObjectUtils.toString(player.getId()));
		playerElement.setText(this.getPlayerName(player));

		elem.addElement(XML_FLIGHT).setText(ObjectUtils.toString(round.getFlight()));
		elem.addElement(XML_HDCP).setText(ObjectUtils.toString(round.getHandicap()));
		elem.addElement(XML_FRONT).setText(ObjectUtils.toString(round.getFront()));
		elem.addElement(XML_BACK).setText(ObjectUtils.toString(round.getBack()));
		elem.addElement(XML_TOTAL).setText(ObjectUtils.toString(round.getTotal()));
		elem.addElement(XML_FRONT_NET).setText(this.convert(round.getFrontNet()));
		elem.addElement(XML_BACK_NET).setText(this.convert(round.getBackNet()));
		elem.addElement(XML_TOTAL_NET).setText(this.convert(round.getTotalNet()));
		elem.addElement(XML_ADJUSTED).setText(ObjectUtils.toString(round.getAdjusted()));
		elem.addElement(XML_SCORES).setText(StringUtils.join(round.getScores(), STRING_SEPARATOR));
		return elem;
	}

	public Element map(Tournament tour) {
		Element elem = DocumentHelper.createElement(XML_TOURNAMENT);
		elem.addAttribute(XML_DB_ID, ObjectUtils.toString(tour.getId()));
		elem.addAttribute(XML_SEASON_ID, tour.getSeasonID().toString());
		elem.addAttribute(XML_COURSE, tour.getCourse().getName());
		elem.addAttribute(XML_DATE, DateFormat.getDateInstance().format(tour.getDate()));
		elem.addAttribute(XML_COURSE_SLOPE, this.convert(tour.getSlope()));
		elem.addAttribute(XML_COURSE_RATING, this.convert(tour.getRating()));
		return elem;
	}

	public Tournament mapTournament(Element xml) {
		try {
			Tournament tournament = new Tournament();
			tournament.setCourse(this.lookupCourse(xml));
			tournament.setDate(XmlDao.parseDate(xml.valueOf("@date")));
			tournament.setSeasonID(xml.numberValueOf("@seasonID").intValue());
			tournament.setRating(xml.numberValueOf("@rating").floatValue());
			tournament.setSlope(xml.numberValueOf("@slope").floatValue());
			tournament.setType(this.parseTournamentType(xml.valueOf("@type")));

			for (Object obj : xml.selectNodes("round")) {
				Node node = (Node) obj;
				tournament.addRound(this.mapRound(node));
			}

			for (Object obj : xml.selectNodes("kp")) {
				Node node = (Node) obj;
				tournament.addKp(this.mapKp(node));
			}
			return tournament;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <pre>
	 * 		<Course courseID="2" name="Allenmore">
	 * 			<directions>29630 Green River Road Auburn, WA 98002</directions>
	 * 			<phone>253-833-2350</phone>
	 * 			<tee name="white">
	 * 				<rating>68.3</rating>
	 * 				<slope>115.0</slope>
	 * 				<holePars>4,3,4,4,4,5,4,3,5,4,5,3,4,4,4,3,4,4</holePars>
	 * 				<holeHdcp>11,17,5,15,1,3,9,13,7,10,4,18,12,14,2,16,6,8</holeHdcp>
	 * 			</tee>
	 * 		</Course>
	 * </pre>
	 * 
	 * @param course
	 * @return
	 */
	public Element map(Course course) {
		Element elem = DocumentHelper.createElement(XML_COURSE_ELEMENT);
		elem.addAttribute(XML_COURSE_ID, ObjectUtils.toString(course.getId()));
		elem.addAttribute(XML_NAME, course.getName());
		elem.addElement(XML_DIRECTIONS).setText(ObjectUtils.toString(course.getDirection()));
		elem.addElement(XML_PHONE).setText(ObjectUtils.toString(course.getPhone()));
		for (CourseTee tee : course.getTees()) {
			elem.add(this.map(tee));
		}
		return elem;
	}

	public Course mapCourse(Node xml) {
		Course course = new Course();
		course.setId(xml.numberValueOf("@" + XML_COURSE_ID).intValue());
		course.setName(xml.valueOf("@" + XML_NAME));

		Node phoneNode = xml.selectSingleNode(XML_PHONE);
		course.setPhone(phoneNode == null ? StringUtils.EMPTY : phoneNode.getText());

		Node directionNode = xml.selectSingleNode(XML_DIRECTIONS);
		course.setDirection(directionNode == null ? StringUtils.EMPTY : directionNode.getText());

		for (Object obj : xml.selectNodes("tee")) {
			Node node = (Node) obj;
			course.addTee(this.mapCourseTee(node));
		}
		return course;
	}

	private CourseTee mapCourseTee(Node xml) {
		CourseTee tee = new CourseTee();
		tee.setName(xml.valueOf("@name"));
		tee.setRating(xml.numberValueOf("rating").floatValue());
		tee.setSlope(xml.numberValueOf("slope").intValue());
		String parString = xml.valueOf("holePars");
		if (StringUtils.isNotEmpty(parString)) {
			String[] splits = StringUtils.split(parString, STRING_SEPARATOR);
			for (int i = 0; i < 18; i++) {
				tee.getPars()[i] = Integer.valueOf(splits[i]);
			}
		}
		String hdcpString = xml.valueOf("holeHdcp");
		if (StringUtils.isNotEmpty(hdcpString)) {
			String[] splits = StringUtils.split(hdcpString, STRING_SEPARATOR);
			for (int i = 0; i < 18; i++) {
				tee.getHandicaps()[i] = Integer.valueOf(splits[i]);
			}
		}
		return tee;
	}

	public Element map(CourseTee tee) {
		Element elem = DocumentHelper.createElement(XML_TEE);
		elem.addAttribute(XML_NAME, tee.getName());
		if (tee.getRating() != null)
			elem.addElement(XML_RATING).setText(tee.getRating().toString());
		if (tee.getSlope() != null)
			elem.addElement(XML_SLOPE).setText(tee.getSlope().toString());
		if (this.hasValidIntegerArray(tee.getPars(), 18))
			elem.addElement(XML_HOLE_PARS).setText(StringUtils.join(tee.getPars(), STRING_SEPARATOR));
		if (this.hasValidIntegerArray(tee.getHandicaps(), 18))
			elem.addElement(XML_HOLE_HANDICAPS).setText(StringUtils.join(tee.getHandicaps(), STRING_SEPARATOR));
		return elem;
	}

	/**
	 * <pre>
	 * <Player playerID="1" firstName="Bruce" lastName="Kaneshiro"
	 * 			active="yes" hdcp="7.18">
	 * 			<Season seasonID="2009" points="43.50" earnings="$62.00" kps="9"
	 * 				attendance="10" />
	 * </pre>
	 * 
	 * @param player
	 * @return
	 */
	public Element map(Player player) {
		Element elem = DocumentHelper.createElement(XML_PLAYER);
		elem.addAttribute(XML_PLAYER_ID, ObjectUtils.toString(player.getId()));
		elem.addAttribute(XML_FIRST_NAME, player.getFirstName());
		elem.addAttribute(XML_LAST_NAME, player.getFirstName());
		elem.addAttribute(XML_ACTIVE, this.convert(player.getActive()));
		elem.addAttribute(XML_HDCP, ObjectUtils.toString(player.getHandicap()));

		for (SeasonSummary summary : player.getSeasonSummaries()) {
			elem.add(this.map(summary));
		}
		return elem;
	}

	/**
	 * <pre>
	 * <Player playerID="1" firstName="Bruce" lastName="Kaneshiro"
	 * 			active="yes" hdcp="7.18">
	 * 			<Season seasonID="2009" points="43.50" earnings="$62.00" kps="9"
	 * 				attendance="10" />
	 * </pre>
	 * 
	 * @param player
	 * @return
	 */
	public Element map(SeasonSummary summary) {
		Element elem = DocumentHelper.createElement(XML_SEASON_SUMMARY);
		elem.addAttribute(XML_SEASON_ID, ObjectUtils.toString(summary.getSeasonID()));
		elem.addAttribute(XML_POINTS, ObjectUtils.toString(summary.getPoints()));
		elem.addAttribute(XML_EARNINGS, this.convertMoney(summary.getEarnings()));
		elem.addAttribute(XML_KPS, Integer.toString(summary.getKps()));
		elem.addAttribute(XML_ATTENDANCE, Integer.toString(summary.getAttendance()));
		return elem;
	}

	protected String getPlayerName(Player player) {
		if (player == null)
			return StringUtils.EMPTY;
		return player.getFirstName() + " " + player.getLastName();
	}

	protected Type parseTournamentType(String value) {
		String code = StringUtils.trimToEmpty(value);
		return Tournament.Type.valueOfByCode(code);
	}

	protected String convert(Float value) {
		if (value == null) {
			return StringUtils.EMPTY;
		}
		return new DecimalFormat("#0").format(value);
	}

	protected String convert(Boolean value) {
		if (value == null) {
			return NO_STRING;
		}
		return value.booleanValue() ? YES_STRING : NO_STRING;
	}

	protected String convertMoney(Float earn) {
		return AbstractDocumentFactory.convertMoney(earn.doubleValue());
	}

	/**
	 * 
	 * @param xml
	 * @return
	 */
	public Round mapRound(Node xml) {
		Round round = new Round();
		round.setId(xml.numberValueOf("@" + XML_ROUND_ID).intValue());
		round.setPlayer(this.lookupPlayer(xml));
		if (StringUtils.isNotEmpty(xml.selectSingleNode(XML_FLIGHT).getText())) {
			round.setFlight(Flight.valueOf(xml.selectSingleNode(XML_FLIGHT).getText()));
		}
		round.setHandicap(this.parseInteger(xml, XML_HDCP));
		round.setFront(this.parseInteger(xml, XML_FRONT));
		round.setBack(this.parseInteger(xml, XML_BACK));
		round.setTotal(this.parseInteger(xml, XML_TOTAL));
		round.setFrontNet(this.parseFloat(xml, XML_FRONT_NET));
		round.setBackNet(this.parseFloat(xml, XML_BACK_NET));
		round.setTotalNet(this.parseFloat(xml, XML_TOTAL_NET));
		round.setAdjusted(this.parseFloat(xml, XML_ADJUSTED).intValue());
		round.setAccepted(this.parseAccepted(xml));
		round.setScores(this.parseIntegerArray(xml, XML_SCORES));
		return round;
	}

	private String parseAccepted(Node xml) {
		Node acceptedNode = xml.selectSingleNode(XML_ACCEPTED);
		if (acceptedNode == null) {
			return StringUtils.EMPTY;
		}
		return acceptedNode.getText();
	}

	private Player lookupPlayer(Node xml) {
		Node playerNode = xml.selectSingleNode(XML_PLAYER);
		Number idAttr = playerNode.numberValueOf("@" + XML_PLAYER_ID);
		if (idAttr != null) {
			int playerId = idAttr.intValue();
			return this.getPlayerDao().load(playerId);
		}
		return null;
	}

	private Course lookupCourse(Node xml) {
		String courseName = xml.valueOf("@" + XML_COURSE);
		if (StringUtils.isNotEmpty(courseName)) {
			return this.getCourseDao().findByName(courseName);
		}
		return null;
	}

	public Integer parseInteger(Node xml, String xpath) {
		Node idNode = xml.selectSingleNode(xpath);
		if (idNode == null) {
			return null;
		}
		String value = idNode.getText();
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		return Integer.valueOf(value);
	}

	public Float parseFloat(Node xml, String xpath) {
		Node floatNode = xml.selectSingleNode(xpath);
		if (floatNode == null) {
			return 0f;
		}
		String value = floatNode.getText();
		return Float.valueOf(value);
	}

	public Integer[] parseIntegerArray(Node xml, String xpath) {
		String value = xml.selectSingleNode(xpath).getText();
		String[] values = StringUtils.split(value, STRING_SEPARATOR);

		Integer[] scores = new Integer[values.length];
		for (int i = 0; i < values.length; i++) {
			String score = values[i].trim();
			scores[i] = Integer.parseInt(score);
		}
		return scores;
	}

	public Date parseDate(String dateString) throws ParseException {
		return DateFormat.getDateInstance().parse(dateString);
	}

	public ObjectDao<Player> getPlayerDao() {
		return playerDao;
	}

	public void setPlayerDao(ObjectDao<Player> playerDao) {
		this.playerDao = playerDao;
	}

	public CourseDao getCourseDao() {
		return this.courseDao;
	}

	public void setCourseDao(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	private boolean hasValidIntegerArray(Integer[] test, int size) {
		if (test.length != size)
			return false;
		for (int i = 0; i < size; i++) {
			if (test[i] == null) {
				return false;
			}
		}
		return true;
	}

}
