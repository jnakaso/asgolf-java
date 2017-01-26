package com.jnaka.golf.service;

import java.util.List;
import java.util.Map;

import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;

public interface HoneyPotCalculator {

	static final float DEFAULT_ANTE = 5.0f;

	static enum Type {
		OVERALL, FRONT, BACK;
	}

	static class Winner {
		private Round round;
		private Float earnings = 0f;

		public Winner(Round round, Float earnings) {
			super();
			this.round = round;
			this.earnings = earnings;
		}

		public Round getRound() {
			return round;
		}

		public void setRound(Round round) {
			this.round = round;
		}

		public Float getEarnings() {
			return earnings;
		}

		public void setEarnings(Float earnings) {
			this.earnings = earnings;
		}
	}

	Map<Type, List<Winner>> findWinners(Tournament tournament);

}
