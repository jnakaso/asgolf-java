package com.jnaka.asgolf.web;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.jnaka.asgolf.service.SiteDefaults;
import com.jnaka.asgolf.service.SiteManager;

public class SiteControllerTest {

	@Test
	public void testCreateDefaults() {
		SiteController controller = this.createController();
		SiteDefaults defaults = new SiteDefaults();
		
		EasyMock.expect(controller.getSiteManager().createDefaults()).andReturn(defaults);
		EasyMock.replay(controller.getSiteManager());
		
		SiteDefaults actual = controller.createSiteDefaults();
		EasyMock.verify(controller.getSiteManager());
		
		Assert.assertEquals(defaults, actual);
	}

	@Test
	public void testUpdateSiteDefaults() throws Exception{
		SiteController controller = this.createController();
		SiteDefaults defaults = new SiteDefaults();
		
		EasyMock.expect(controller.getSiteManager().writeDefaultsToFile(defaults)).andReturn(true);
		EasyMock.replay(controller.getSiteManager());
		
		boolean actual = controller.updateSiteDefaults(defaults);
		EasyMock.verify(controller.getSiteManager());
		
		Assert.assertEquals(true, actual);
	}
	
	@Test
	public void testEvents() throws Exception {
		SiteController controller = this.createController();
		
		EasyMock.expect(controller.getSiteManager().getCurrentEvents()).andReturn("actual");
		EasyMock.replay(controller.getSiteManager());
		
		String actual = controller.getEvents();
		EasyMock.verify(controller.getSiteManager());
		Assert.assertEquals("actual", actual);
	}
	
	private SiteController createController() {
		SiteManager siteManager = EasyMock.createMock(SiteManager.class);
		SiteController controller = new SiteController();
		controller.setSiteManager(siteManager);
		return controller;
	}
}
