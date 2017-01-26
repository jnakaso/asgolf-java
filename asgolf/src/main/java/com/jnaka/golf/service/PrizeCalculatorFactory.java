package com.jnaka.golf.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jnaka.golf.domain.PrizeMoney;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Season.ScoringPolicy;

public interface PrizeCalculatorFactory {

	public static PrizeMoney ATTENDANCE_PRIZE = new PrizeMoney(1, 0f, 3f);

	@SuppressWarnings("serial")
	public static Map<ScoringPolicy, List<PrizeMoney>> KP_PRIZES = new HashMap<ScoringPolicy, List<PrizeMoney>>() {
		{
			this.put(ScoringPolicy.DEFAULT_15, Arrays.asList( //
					new PrizeMoney(1, Float.valueOf("3"), Float.valueOf("0"))));
			this.put(ScoringPolicy.DEFAULT_20, Arrays.asList( //
					new PrizeMoney(1, Float.valueOf("3"), Float.valueOf("0"))));
			this.put(ScoringPolicy.DEFAULT_2015, Arrays.asList( //
					new PrizeMoney(1, Float.valueOf("3"), Float.valueOf("0"))));
		}
	};

	@SuppressWarnings("serial")
	public static Map<ScoringPolicy, List<PrizeMoney>> NORMAL_PRIZES = new HashMap<ScoringPolicy, List<PrizeMoney>>() {
		{
			this.put(ScoringPolicy.DEFAULT_15, Arrays.asList( //
					new PrizeMoney(1, Float.valueOf("15"), Float.valueOf("9")), //
					new PrizeMoney(2, Float.valueOf("10"), Float.valueOf("6")), //
					new PrizeMoney(3, Float.valueOf("5"), Float.valueOf("3"))));
			this.put(ScoringPolicy.DEFAULT_20, Arrays.asList(
					new PrizeMoney(1, Float.valueOf("20"), Float.valueOf("9")), //
					new PrizeMoney(2, Float.valueOf("15"), Float.valueOf("6")), //
					new PrizeMoney(3, Float.valueOf("10"), Float.valueOf("3"))));
			this.put(ScoringPolicy.DEFAULT_2015, Arrays.asList(
					new PrizeMoney(1, Float.valueOf("40"), Float.valueOf("9")), //
					new PrizeMoney(2, Float.valueOf("25"), Float.valueOf("6")), //
					new PrizeMoney(3, Float.valueOf("15"), Float.valueOf("3"))));
		}
	};

	@SuppressWarnings("serial")
	public Map<ScoringPolicy, List<PrizeMoney>> TWO_DAY_PRIZES = new HashMap<ScoringPolicy, List<PrizeMoney>>() {
		{
			this.put(ScoringPolicy.DEFAULT_15, Arrays.asList( //
					new PrizeMoney(1, Float.valueOf("100"), Float.valueOf("18")), //
					new PrizeMoney(2, Float.valueOf("75"), Float.valueOf("12")), //
					new PrizeMoney(3, Float.valueOf("50"), Float.valueOf("6"))));
			this.put(ScoringPolicy.DEFAULT_20, Arrays.asList( //
					new PrizeMoney(1, Float.valueOf("175"), Float.valueOf("18")), //
					new PrizeMoney(2, Float.valueOf("125"), Float.valueOf("12")), //
					new PrizeMoney(3, Float.valueOf("75"), Float.valueOf("6"))));
			this.put(ScoringPolicy.DEFAULT_2015, Arrays.asList( //
					new PrizeMoney(1, Float.valueOf("100"), Float.valueOf("18")), //
					new PrizeMoney(2, Float.valueOf("75"), Float.valueOf("12")), //
					new PrizeMoney(3, Float.valueOf("50"), Float.valueOf("6"))));
		}
	};

	PrizeCalculator getCalculator(Tournament tournament);

}
