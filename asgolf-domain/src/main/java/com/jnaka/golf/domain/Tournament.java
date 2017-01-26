package com.jnaka.golf.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.jnaka.domain.EntityObjectImpl;

/**
 * <Tournament tournamentID="1" seasonID="1996" course="Lipoma Firs"
 * date="Apr 21, 1996" slope="69.50" rating="118.00">
 * 
 * @author jnakaso
 * 
 */
public class Tournament extends EntityObjectImpl {

	public static final Comparator<Tournament> SEASON_COMPARATOR = new Comparator<Tournament>() {
		@Override
		public int compare(Tournament o1, Tournament o2) {
			return Integer.valueOf(o2.getSeason().getId().intValue()).compareTo(
					Integer.valueOf(o1.getSeason().getId().intValue()));
		}
	};

	public static enum Type {
		NORMAL("", "One Day", false), DAY_ONE("Day 1", "Two Day - Day 1", true), DAY_TWO("Day 2", "Two Day - Day 2",
				true);

		public static Type valueOfByCode(String code) {
			for (Type type : values()) {
				if (type.getCode().equalsIgnoreCase(code)) {
					return type;
				}
			}
			return null;
		}

		private final String code;
		private final String label;
		private final boolean twoDay;

		private Type(String code, String label, boolean twoDay) {
			this.code = code;
			this.label = label;
			this.twoDay = twoDay;
		}

		public boolean isTwoDay() {
			return twoDay;
		}

		public String getCode() {
			return code;
		}

		public String getLabel() {
			return label;
		}

	}

	private Season season;
	private Integer seasonID;
	private Course course;
	private Date date = new Date();
	private Float slope = 0f;
	private Float rating = 0f;
	private List<Round> rounds = new ArrayList<Round>();
	private List<Winner> winners = new ArrayList<Winner>();
	private List<Kp> kps = new ArrayList<Kp>();

	private Type type = Type.NORMAL;

	public Integer getSeasonID() {
		if (seasonID == null) {
			seasonID = this.getSeason().getId().intValue();
		}
		return seasonID;
	}

	public void setSeasonID(Integer seasonID) {
		this.seasonID = seasonID;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
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

	public List<Round> getRounds() {
		return rounds;
	}

	public void setRounds(List<Round> rounds) {
		this.rounds = rounds;
	}

	public boolean addRound(Round round) {
		if (round != null) {
			round.setTournament(this);
			return this.rounds.add(round);
		}
		return false;
	}

	public boolean removeRound(Round round) {
		return this.rounds.remove(round);
	}

	public List<Winner> getWinners() {
		return winners;
	}

	public void setWinners(List<Winner> winners) {
		this.winners = winners;
	}

	public boolean addWinner(Winner winner) {
		if (winner != null) {
			return this.winners.add(winner);
		}
		return false;
	}

	public boolean removeWinner(Winner winner) {
		return this.winners.remove(winner);
	}

	public void clearWinners() {
		this.winners.clear();
	}

	public List<Kp> getKps() {
		return kps;
	}

	public void setKps(List<Kp> kps) {
		this.kps = kps;
	}

	public boolean addKp(Kp kp) {
		if (kp != null) {
			return this.kps.add(kp);
		}
		return false;
	}

	public boolean removeKp(Kp kp) {
		return this.kps.remove(kp);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Season getSeason() {
		return this.season;
	}

	public void setSeason(Season aSeason) {
		this.season = aSeason;
	}

	@Override
	public boolean equals(Object that) {
		if (that == null) {
			return false;
		}
		if (that instanceof Tournament == false) {
			return false;
		}
		if (this == that) {
			return true;
		}
		return new EqualsBuilder() //
				.append(this.getId(), ((Tournament) that).getId()) //
				.isEquals();
	}

	@Override
	public int hashCode() {
		if (this.getId() == null) {
			return super.hashCode();
		}
		return new HashCodeBuilder(17, 37) //
				.append(this.getId()) //
				.hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE) //
				.appendSuper(super.toString()) //
				.append("date", this.date)//
				.append("course", this.course)//
				.toString();
	}

}
