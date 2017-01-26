package com.jnaka.asgolf.web;

import org.junit.Test;

import com.jnaka.asgolf.service.SiteManager;

import org.easymock.EasyMock;
import org.junit.Assert;

public class MainControllerTest {

	@Test
	public void testHome() {
		MainController controller = this.createController();
		String home = controller.home();
		Assert.assertEquals("redirect:index.html", home);
	}

	@Test
	public void testSave() throws Exception {
		MainController controller = this.createController();
		
		EasyMock.expect(controller.getSiteManager().save()).andReturn(true);
		EasyMock.replay(controller.getSiteManager());
		
		String actual = controller.save();
		EasyMock.verify(controller.getSiteManager());
		Assert.assertEquals("Club information saved.", actual);
	}
	
	@Test
	public void testSaveFail() throws Exception {
		MainController controller = this.createController();
		
		EasyMock.expect(controller.getSiteManager().save()).andReturn(false);
		EasyMock.replay(controller.getSiteManager());
		
		String actual = controller.save();
		EasyMock.verify(controller.getSiteManager());
		Assert.assertEquals("FAILZ", actual);
	}


	private MainController createController() {
		SiteManager siteManager = EasyMock.createMock(SiteManager.class);
		MainController controller = new MainController();
		controller.setSiteManager(siteManager);
		return controller;
	}

}
