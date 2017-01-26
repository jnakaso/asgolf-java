package com.jnaka.golf.domain.xml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jnaka.domain.xml.DateAdapter;
import com.jnaka.domain.xml.IdAdapter;
import com.jnaka.golf.domain.Tournament.Type;

/**
 *<pre>
 * <Tournament tournamentID="161" seasonID="2010" course="Brookdale" courseID="1" type=""
 * date="Apr 18, 2010" slope="69.00" rating="115.00">
 * </pre>
 * 
 * @author nakasones
 * 
 */
@XmlRootElement(name = "Tournament")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tournament {

	@XmlID
	@XmlAttribute(name = "tournamentID")
	@XmlJavaTypeAdapter(IdAdapter.class)
	private Number id;

	// supposed to work
	// @XmlIDREF
	@XmlAttribute(name = "seasonID")
	private Integer seasonID;
	@XmlTransient
	private Season season;

	// supposed to work
	// @XmlIDREF
	@XmlAttribute(name = "courseID")
	private Integer courseID;
	@XmlTransient
	private Course course;

	@XmlAttribute
	@XmlJavaTypeAdapter(TournamentTypeAdapter.class)
	private com.jnaka.golf.domain.Tournament.Type type = Type.NORMAL;

	@XmlAttribute
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date date;
	@XmlAttribute
	private Float slope;
	@XmlAttribute
	private Float rating;

	@XmlAttribute(name = "course")
	private String courseName;

	@XmlElement(name = "winner")
	private List<Winner> winners = new ArrayList<Winner>();

	@XmlElement(name = "kp")
	private List<Kp> kps = new ArrayList<Kp>();

	@XmlElement(name = "round")
	private List<Round> rounds = new ArrayList<Round>();

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

	public Integer getSeasonID() {
		return seasonID;
	}

	public void setSeasonID(Integer seasonID) {
		this.seasonID = seasonID;
	}



	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
		this.seasonID = season.getId();

	}
	
	public Integer getCourseID() {
		return courseID;
	}

	public void setCourseID(Integer courseID) {
		this.courseID = courseID;
	}
	
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
		this.courseID = course.getId().intValue();
	}

	public com.jnaka.golf.domain.Tournament.Type getType() {
		return type;
	}

	public void setType(com.jnaka.golf.domain.Tournament.Type type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Float getSlope() {
		return slope;
	}

	public void setSlope(Float slope) {
		this.slope = slope;
	}

	public Float getRating() {
		return rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public List<Winner> getWinners() {
		return winners;
	}

	public void setWinners(List<Winner> winners) {
		this.winners = winners;
	}

	public List<Kp> getKps() {
		return kps;
	}

	public void setKps(List<Kp> kps) {
		this.kps = kps;
	}

	public List<Round> getRounds() {
		return rounds;
	}

	public void setRounds(List<Round> rounds) {
		this.rounds = rounds;
	}

}
