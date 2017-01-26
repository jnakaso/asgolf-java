package com.jnaka.golf.domain;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.jnaka.domain.EntityObjectImpl;

public class Player extends EntityObjectImpl {

	private static final Comparator<SeasonSummary> SEASON_COMPARATOR = new Comparator<SeasonSummary>() {
		@Override
		public int compare(SeasonSummary o1, SeasonSummary o2) {
			return Integer.valueOf(o2.getSeason().getId().intValue()).compareTo(
					Integer.valueOf(o1.getSeason().getId().intValue()));
		}
	};

	private String firstName;
	private String lastName;
	private Boolean active = false;
	private Float handicap = 0f;

	private NavigableSet<SeasonSummary> seasonSummaries = new TreeSet<SeasonSummary>(SEASON_COMPARATOR);

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Float getHandicap() {
		return handicap;
	}

	public void setHandicap(Float handicap) {
		this.handicap = handicap;
	}

	public NavigableSet<SeasonSummary> getSeasonSummaries() {
		return seasonSummaries;
	}

	public void setSeasonSummaries(NavigableSet<SeasonSummary> seasonSummary) {
		this.seasonSummaries = seasonSummary;
	}

	public boolean addSeasonSummary(SeasonSummary seasonSummary) {
		if (seasonSummary != null) {
			seasonSummary.setPlayer(this);
			return this.seasonSummaries.add(seasonSummary);
		}
		return false;
	}

	public boolean removeSeasonSummary(SeasonSummary seasonSummary) {
		if (seasonSummary != null) {
			seasonSummary.setPlayer(null);
			return this.seasonSummaries.remove(seasonSummary);
		}
		return false;
	}

	@Override
	public boolean equals(Object that) {
		if (that == null) {
			return false;
		}
		if (that instanceof Player == false) {
			return false;
		}
		if (this == that) {
			return true;
		}
		return new EqualsBuilder() //
				.append(this.getId(), ((Player) that).getId()) //
				.isEquals();
	}

	@Override
	public int hashCode() {
		if (this.getId() == null) {
			return super.hashCode();
		}
		return new HashCodeBuilder(11, 37) //
				.append(this.getId()) //
				.hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE) //
				.appendSuper(super.toString()) //
				.append("firstName", this.firstName)//
				.append("lastName", this.lastName)//
				.toString();
	}

}
