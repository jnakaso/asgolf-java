package com.jnaka.asgolf.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jnaka.asgolf.service.SiteDefaults;
import com.jnaka.asgolf.service.SiteManager;

@RestController
public class SiteController {

	final private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SiteManager siteManager;

	@RequestMapping("/create-site-defaults")
	public SiteDefaults createSiteDefaults() {
		return this.getSiteManager().createDefaults();
	}

	@RequestMapping(value = "/update-site-defaults", method = RequestMethod.POST)
	public boolean updateSiteDefaults(@RequestBody SiteDefaults siteDefaults) throws Exception {
		return this.getSiteManager().writeDefaultsToFile(siteDefaults);
	}

	@RequestMapping(value = "/update-site-info", method = RequestMethod.POST)
	public boolean updateSiteInfo(@RequestParam(value = "season", required = false) Integer season) throws Exception {
		if (season == null) {
			return this.getSiteManager().writeSiteInfo();
		} else {
			return this.getSiteManager().writeSiteInfo(season);
		}
	}

	@RequestMapping("/events")
	public String getEvents() throws Exception {
		return this.getSiteManager().getCurrentEvents();
	}

	SiteManager getSiteManager() {
		return siteManager;
	}

	void setSiteManager(SiteManager siteManager) {
		this.siteManager = siteManager;
	}

	Logger getLogger() {
		return logger;
	}

}