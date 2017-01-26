package com.jnaka.asgolf.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jnaka.asgolf.service.SiteManager;

@Controller
public class MainController {
	final private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SiteManager siteManager;

	// public MainController(
	// SiteManager siteManager) {
	// super();
	// this.siteManager = siteManager;
	// }

	@RequestMapping("/")
	String home() {
		return "redirect:index.html";
	}

	@RequestMapping(value = "/club/save", method = RequestMethod.POST, produces = "text/plain")
	public @ResponseBody String save() throws Exception {
		this.getLogger().debug("Club save");
		return this.getSiteManager().save() ? "Club information saved." : "FAILZ";
	}

	SiteManager getSiteManager() {
		return this.siteManager;
	}

	void setSiteManager(SiteManager siteManager) {
		this.siteManager = siteManager;
	}

	Logger getLogger() {
		return logger;
	}
}