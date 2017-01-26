package com.jnaka.golf.service;

import java.util.Collection;
import java.util.Comparator;

import org.apache.commons.collections.Predicate;

import com.jnaka.golf.domain.Round;

public interface RoundService {

	public static final int NUMBER_OF_HOLES = 18;

	public enum Filter {
		FRONT(new Comparator<Round>() {
			@Override
			public int compare(Round o1, Round o2) {
				return o1.getFront().compareTo(o2.getFront());
			}
		}), //
		BACK(new Comparator<Round>() {
			@Override
			public int compare(Round o1, Round o2) {
				return o1.getBack().compareTo(o2.getBack());
			}
		}), //
		TOTAL(new Comparator<Round>() {
			@Override
			public int compare(Round o1, Round o2) {
				return o1.getTotal().compareTo(o2.getTotal());
			}
		}), //
		FRONT_NET(new Comparator<Round>() {
			@Override
			public int compare(Round o1, Round o2) {
				return o1.getFrontNet().compareTo(o2.getFrontNet());
			}
		}), //
		BACK_NET(new Comparator<Round>() {
			@Override
			public int compare(Round o1, Round o2) {
				return o1.getBackNet().compareTo(o2.getBackNet());
			}
		}), //
		TOTAL_NET(new Comparator<Round>() {
			@Override
			public int compare(Round o1, Round o2) {
				return o1.getTotalNet().compareTo(o2.getTotalNet());
			}
		});

		private Comparator<Round> scoreComparator;

		private Filter(Comparator<Round> comparator) {
			this.scoreComparator = comparator;
		}

		public Comparator<Round> getScoreComparator() {
			return scoreComparator;
		}

	}

	public static final Predicate HAS_FLIGHT_PREDICATE = new Predicate() {
		@Override
		public boolean evaluate(Object object) {
			return ((Round) object).getFlight() != null;
		}
	};

	public static final Comparator<Round> DATE_COMPARATOR = new Comparator<Round>() {
		@Override
		public int compare(Round o1, Round o2) {
			return o2.getTournament().getDate().compareTo(o1.getTournament().getDate());
		}
	};

	public Float highestScore(Collection<Round> rounds, Filter filter);

	public Float lowestScore(Collection<Round> rounds, Filter filter);

	public Float average(Collection<Round> rounds, Filter totalNet);

	public Integer[] getPlusMinus(Round round);

	public Integer[] getPlusMinusAdjusted(Round round);

	public void updateTotals(Round round);

	public Float getHandicapIndex(Round round);

}
