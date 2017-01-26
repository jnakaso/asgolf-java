package com.jnaka.golf.reports;

import org.dom4j.Document;
import org.dom4j.Element;

import com.jnaka.dao.ObjectDao;
import com.jnaka.golf.dao.DomMapper;
import com.jnaka.golf.dao.TournamentDao;
import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.reports.AbstractReportWriter;

public class DataStoreWriter extends AbstractReportWriter {

	private static final String DEFAULT_ROOT_NAME = "GolfClub";
	private static final String DEFAULT_FILE_NAME = "asgolf.xml";
	private static final String XML_COURSES = null;
	private static final String XML_PLAYER_HISTORY = null;

	public static void main(String args[]) {
		DataStoreWriter extractor = new DataStoreWriter();
		extractor.extract();
	}

	private DomMapper domMapper;

	public DataStoreWriter() {
		super();
		this.setFileName(DEFAULT_FILE_NAME);
		this.setRootElementName(DEFAULT_ROOT_NAME);
	}

	@Override
	protected void updateDocument(Document doc) {
		this.extractPlayers(doc);
		this.extractCourses(doc);
		this.extractTournaments(doc);
	}

	private void extractPlayers(Document doc) {
		Element coursesElement = doc.getRootElement().addElement(XML_PLAYER_HISTORY);
		@SuppressWarnings("unchecked")
		ObjectDao<Player> dao = (ObjectDao<Player>) this.getBeanFactory().getBean("PlayerDao");
		for (Player player : dao.getAll()) {
			coursesElement.add(this.getDomMapper().map(player));
		}
		this.getLog().info("Finished for: Players");
	}

	private void extractCourses(Document doc) {
		Element coursesElement = doc.getRootElement().addElement(XML_COURSES);
		@SuppressWarnings("unchecked")
		ObjectDao<Course> dao = (ObjectDao<Course>) this.getBeanFactory().getBean("CourseDao");
		for (Course course : dao.getAll()) {
			coursesElement.add(this.getDomMapper().map(course));
		}
		this.getLog().info("Finished for: Courses");
	}

	private void extractTournaments(Document doc) {
		DomMapper mapper = this.getDomMapper();
		TournamentDao dao = (TournamentDao) this.getBeanFactory().getBean("TournamentDao");
		for (Tournament tournament : dao.getAll()) {
			int aTournamentID = tournament.getId().intValue();
			try {
				doc.getRootElement().add(mapper.map(tournament));
				this.getLog().info("Finished for: " + aTournamentID);
			} catch (Exception e) {
				e.printStackTrace();
				this.getLog().warn("Skipping : " + aTournamentID);
			}
		}
	}

	public DomMapper getDomMapper() {
		if (this.domMapper == null) {
			this.domMapper = this.defaultDomMapper();
		}
		return this.domMapper;
	}

	public void setDomMapper(DomMapper domMapper) {
		this.domMapper = domMapper;
	}

	private DomMapper defaultDomMapper() {
		return (DomMapper) this.getBeanFactory().getBean("DomMapper");
	}
}
