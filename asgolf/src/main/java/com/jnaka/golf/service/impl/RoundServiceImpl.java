package com.jnaka.golf.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.jnaka.golf.domain.CourseTee;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.service.RoundService;

@Service("RoundService")
public class RoundServiceImpl implements RoundService {

	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public Float highestScore(Collection<Round> rounds, Filter filter) {
		if (rounds.isEmpty()) {
			return null;
		}
		List<Round> sorted = new ArrayList<Round>(rounds);
		Collections.sort(sorted, filter.getScoreComparator());
		Collections.reverse(sorted);
		return this.getValue(sorted.iterator().next(), filter);
	}

	public Float lowestScore(Collection<Round> rounds, Filter filter) {
		if (rounds.isEmpty()) {
			return null;
		}
		List<Round> sorted = new ArrayList<Round>(rounds);
		Collections.sort(sorted, filter.getScoreComparator());
		return this.getValue(sorted.iterator().next(), filter);
	}

	public Float average(Collection<Round> rounds, Filter totalNet) {
		float total = 0f;
		for (Round round : rounds) {
			switch (totalNet) {
			case TOTAL_NET:
				total += round.getTotalNet().floatValue();
				break;
			case TOTAL:
				total += round.getTotal().floatValue();
				break;
			case FRONT_NET:
				total += round.getFrontNet().floatValue();
				break;
			case FRONT:
				total += round.getFront().floatValue();
				break;
			case BACK_NET:
				total += round.getFrontNet().floatValue();
				break;
			case BACK:
				total += round.getFront().floatValue();
				break;
			default:
				break;
			}
		}
		return total / rounds.size();
	}

	@Override
	public Integer[] getPlusMinus(Round round) {
		return this.getPlusMinus(round, false);
	}

	@Override
	public Integer[] getPlusMinusAdjusted(Round round) {
		return this.getPlusMinus(round, true);
	}

	@Override
	public void updateTotals(Round round) {
		Integer front = 0;
		Integer back = 0;
		Integer adj = 0;
		for (int i = 0; i < 9; i++) {
			Integer score = round.getScores()[i];
			front += score;
			adj += Math.min(score, this.getMaxAdjustedHole(round, i));
		}
		for (int i = 9; i < 18; i++) {
			Integer score = round.getScores()[i];
			back += score;
			adj += Math.min(score, this.getMaxAdjustedHole(round, i));
		}
		round.setFront(front);
		round.setBack(back);
		round.setTotal(front.intValue() + back.intValue());
		round.setFrontNet(front.floatValue() - round.getHandicap().floatValue() / 2);
		round.setBackNet(back.floatValue() - round.getHandicap().floatValue() / 2f);
		round.setTotalNet(front.floatValue() + back.floatValue() - round.getHandicap().floatValue());
		round.setAdjusted(adj);
	}

	public Integer getMaxAdjustedHole(Round round, int hole) {
		Integer hdcp = round.getHandicap();
		if (hdcp > 39)
			return 10;
		if (hdcp > 29)
			return 9;
		if (hdcp > 19)
			return 8;
		if (hdcp > 9)
			return 7;
		// TODO change to DOUBLE BOGEY
		return 7;
	}

	public Float getHandicapIndex(Round round) {
		Tournament aTournament = round.getTournament();
		float slope = aTournament.getSlope().floatValue();
		float adj = round.getAdjusted().floatValue();
		float rating = aTournament.getRating().floatValue();
		float index = 113f * (adj - rating) / slope;
		if (index < 0.0) {
			System.out.println(aTournament);
		}
		return index;
	}

	private Float getValue(Round round, Filter filter) {
		switch (filter) {
		case BACK:
			return new Float(round.getBack());
		case BACK_NET:
			return new Float(round.getBackNet());
		case FRONT:
			return new Float(round.getFront());
		case FRONT_NET:
			return new Float(round.getFrontNet());
		case TOTAL:
			return new Float(round.getTotal());
		case TOTAL_NET:
			return new Float(round.getTotalNet());

		default:
			throw new RuntimeException("Bad filter.");
		}
	}

	private Integer[] getPlusMinus(Round round, boolean adjusted) {
		Integer[] plusMinus = this.createIntegerArray();
		CourseTee tees = this.getCourseTees(round);
		if (this.hasValidIntegerArray(tees.getPars(), NUMBER_OF_HOLES)) {
			if (this.hasValidIntegerArray(round.getScores(), NUMBER_OF_HOLES)) {
				for (int i = 0; i < NUMBER_OF_HOLES; i++) {
					try {
						plusMinus[i] += round.getScores()[i] - tees.getPars()[i];
						if (adjusted) {
							plusMinus[i] += getHoleHandicap(i, round);
						}
					} catch (Exception e) {
						this.getLogger().log(Level.WARNING, e.getMessage());
					}
				}
			}
		}
		return plusMinus;
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

	private Integer getHoleHandicap(int hole, Round round) {
		CourseTee tees = this.getCourseTees(round);
		return (tees.getHandicaps()[hole] - round.getHandicap() - NUMBER_OF_HOLES) / NUMBER_OF_HOLES;
	}

	private CourseTee getCourseTees(Round round) {
		for (CourseTee tee : round.getTournament().getCourse().getTees()) {
			if ("white".equalsIgnoreCase(tee.getName())) {
				return tee;
			}
		}return round.getTournament().getCourse().getTees().get(0);
	}

	private Integer[] createIntegerArray() {
		Integer[] counts = new Integer[NUMBER_OF_HOLES];
		for (int i = 0; i < NUMBER_OF_HOLES; i++) {
			counts[i] = Integer.valueOf(0);
		}
		return counts;
	}

	protected Logger getLogger() {
		return logger;
	}

}
