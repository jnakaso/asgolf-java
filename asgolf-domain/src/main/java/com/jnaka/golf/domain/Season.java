package com.jnaka.golf.domain;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.jnaka.domain.EntityObjectImpl;

public class Season extends EntityObjectImpl {

	public static enum ScoringPolicy {
		DEFAULT_15("$15, $10, $5"), DEFAULT_20("$20, $15, $10"), DEFAULT_2015("$40, $25, $15");

		private String label;

		private ScoringPolicy(String label) {
			this.label = label;
		}

		@Override
		public String toString() {
			return this.label;
		}

		public static ScoringPolicy valueOfByCode(String valueOf) {
			if (StringUtils.isNotEmpty(valueOf))
				return valueOf(valueOf);
			return null;
		}

	}

	public static enum HandicapPolicy {
		FIVE_OF_TEN("5 of last 10"), TEN_OF_TWENTY("10 of last 20");

		private String label;

		private HandicapPolicy(String label) {
			this.label = label;
		}

		@Override
		public String toString() {
			return this.label;
		}

		public static HandicapPolicy valueOfByCode(String valueOf) {
			if (StringUtils.isNotEmpty(valueOf))
				return valueOf(valueOf);
			return null;
		}
	}

	private ScoringPolicy scoringPolicy = ScoringPolicy.DEFAULT_2015;
	private HandicapPolicy handicapPolicy = HandicapPolicy.FIVE_OF_TEN;

	public Season() {
		super();
	}

	public Season(Integer id, ScoringPolicy scoringPolicy, HandicapPolicy handicapPolicy) {
		super();
		this.setId(id);
		this.scoringPolicy = scoringPolicy;
		this.handicapPolicy = handicapPolicy;
	}

	public String getName() {
		return this.getId().toString();
	}

	public void setName(String aName) {

	}

	public ScoringPolicy getScoringPolicy() {
		return scoringPolicy;
	}

	public void setScoringPolicy(ScoringPolicy scoringPolicy) {
		this.scoringPolicy = scoringPolicy;
	}

	public HandicapPolicy getHandicapPolicy() {
		return handicapPolicy;
	}

	public void setHandicapPolicy(HandicapPolicy handicapPolicy) {
		this.handicapPolicy = handicapPolicy;
	}

	@Override
	public boolean equals(Object that) {
		if (that == null) {
			return false;
		}
		if (that instanceof Season == false) {
			return false;
		}
		if (this == that) {
			return true;
		}
		return new EqualsBuilder() //
				.append(this.getId(), ((Season) that).getId()) //
				.isEquals();
	}

	@Override
	public int hashCode() {
		if (this.getId() == null) {
			return super.hashCode();
		}
		return new HashCodeBuilder(13, 41).hashCode();
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE);
		builder.appendSuper(super.toString());
		builder.append("scoringPolicy", this.scoringPolicy);
		builder.append("handicapPolicy", this.handicapPolicy);
		return builder.toString();
	}

}
