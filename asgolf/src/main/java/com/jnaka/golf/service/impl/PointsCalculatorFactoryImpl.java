package com.jnaka.golf.service.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.service.PointsCalculator;
import com.jnaka.golf.service.PointsCalculatorFactory;

@Service
public class PointsCalculatorFactoryImpl implements PointsCalculatorFactory, BeanFactoryAware {

	private BeanFactory beanFactory;

	@Override
	public PointsCalculator getCalculator(Season season) {
		return (PointsCalculator) this.beanFactory.getBean("PointsCalculator", season);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
