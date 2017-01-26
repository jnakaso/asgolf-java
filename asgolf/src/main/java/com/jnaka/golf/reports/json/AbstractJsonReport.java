package com.jnaka.golf.reports.json;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.jnaka.golf.service.stats.StatsCalculator;

public abstract class AbstractJsonReport<T, R> implements JsonReport<T> {

	public static String convert(Float score) {
		if (score == null) {
			return StringUtils.EMPTY;
		}
		NumberFormat form = new DecimalFormat("#0.00");
		return form.format(score);
	}

	public static String convert(Double score) {
		if (score == null) {
			return StringUtils.EMPTY;
		}
		NumberFormat form = new DecimalFormat("#0.00");
		return form.format(score);
	}

	public static String convert(Date date) {
		if (date == null) {
			return StringUtils.EMPTY;
		}
		DateFormat form = new SimpleDateFormat("MM-dd-yyyy");
		return form.format(date);
	}

	private StatsCalculator<R> calculator;

	public StatsCalculator<R> getCalculator() {
		return calculator;
	}

	public void setCalculator(StatsCalculator<R> calculator) {
		this.calculator = calculator;
	}

}
