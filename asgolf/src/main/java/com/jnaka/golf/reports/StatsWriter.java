package com.jnaka.golf.reports;

import java.util.Calendar;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.jnaka.reports.AbstractReportWriter;
import com.jnaka.reports.DocumentFactory;

@Service("StatsWriter")
public class StatsWriter extends AbstractReportWriter {

	private static final String DEFAULT_FILE_NAME = "stats.xml";
	private static final String DEFAULT_ROOT_NAME = "stats";

	public static void main(String args[]) {
		int current = Calendar.getInstance().get(Calendar.YEAR);
		int start = current;
		if (args.length != 0) {
			start = Integer.parseInt(args[0]);
		}
		for (int season = start; season <= current; season++) {
			StatsWriter extractor = new StatsWriter();
			extractor.setSeasonID(season);
			extractor.setFileName(DEFAULT_ROOT_NAME + "_" + season + ".xml");
			extractor.extract();
		}
	}

	private int seasonID = 2010; // ;

	public StatsWriter() {
		super();
		this.setFileName(DEFAULT_FILE_NAME);
		this.setRootElementName(DEFAULT_ROOT_NAME);
	}

	@Override
	protected void updateDocument(Document doc) {
		Element root = doc.getRootElement();
		root.addAttribute(XmlConstants.XML_ID_SEASON, Integer.toString(this.getSeasonID()));
		//
		this.extractSandBagger(doc);
		this.extractVardon(doc);
		this.extractWhere(doc);
		this.extractBirdies(doc);
		this.extractGBU(doc);
		this.extractPlayerScoring(doc);
		this.extractMostImproved(doc);
		this.extractTournamentSummary(doc);
		this.extractPlayerLatestRounds(doc);
		this.extractTwoDayTournamentSummary(doc);
		this.extractHolesInOne(doc);
		this.extractKpSweeper(doc);
		this.extractThreePutt(doc);
		this.extractBigMoney(doc);
	}

	private void extractPlayerLatestRounds(Document doc) {
		DocumentFactory factory = (DocumentFactory) this.getBean("playerLatestRounds");
		factory.setSeasonID(this.getSeasonID());
		doc.getRootElement().add(factory.create().getRootElement().detach());
		this.getLog().info("Finished for: " + factory.getRootName());
	}

	private void extractTwoDayTournamentSummary(Document doc) {
		DocumentFactory factory = (DocumentFactory) this.getBean("twoDayTournamentSummary");
		factory.setSeasonID(this.getSeasonID());
		doc.getRootElement().add(factory.create().getRootElement().detach());
		this.getLog().info("Finished for: " + factory.getRootName());
	}

	private void extractTournamentSummary(Document doc) {
		DocumentFactory factory = (DocumentFactory) this.getBean("tournamentSummary");
		factory.setSeasonID(this.getSeasonID());
		doc.getRootElement().add(factory.create().getRootElement().detach());
		this.getLog().info("Finished for: " + factory.getRootName());
	}

	private void extractMostImproved(Document doc) {
		DocumentFactory factory = (DocumentFactory) this.getBean("mostImproved");
		factory.setSeasonID(this.getSeasonID());
		doc.getRootElement().add(factory.create().getRootElement().detach());
		this.getLog().info("Finished for: " + factory.getRootName());
	}

	private void extractPlayerScoring(Document doc) {
		DocumentFactory factory = (DocumentFactory) this.getBean("playerScoring");
		factory.setSeasonID(this.getSeasonID());
		doc.getRootElement().add(factory.create().getRootElement().detach());
		this.getLog().info("Finished for: " + factory.getRootName());
	}

	private void extractGBU(Document doc) {
		DocumentFactory factory = (DocumentFactory) this.getBean("goodBadUgly");
		factory.setSeasonID(this.getSeasonID());
		doc.getRootElement().add(factory.create().getRootElement().detach());
		this.getLog().info("Finished for: " + factory.getRootName());
	}

	private void extractBirdies(Document doc) {
		DocumentFactory factory = (DocumentFactory) this.getBean("birdies");
		factory.setSeasonID(this.getSeasonID());
		doc.getRootElement().add(factory.create().getRootElement().detach());
		this.getLog().info("Finished for: " + factory.getRootName());
	}

	private void extractWhere(Document doc) {
		DocumentFactory factory = (DocumentFactory) this.getBean("whereWePlayed");
		doc.getRootElement().add(factory.create().getRootElement().detach());
		this.getLog().info("Finished for: " + factory.getRootName());
	}

	private void extractVardon(Document doc) {
		DocumentFactory factory = (DocumentFactory) this.getBean("vardon");
		factory.setSeasonID(this.getSeasonID());
		doc.getRootElement().add(factory.create().getRootElement().detach());
		this.getLog().info("Finished for: " + factory.getRootName());
	}

	private void extractSandBagger(Document doc) {
		DocumentFactory factory = (DocumentFactory) this.getBean("sandbagger");
		factory.setSeasonID(this.getSeasonID());
		doc.getRootElement().add(factory.create().getRootElement().detach());
		this.getLog().info("Finished for: " + factory.getRootName());
	}

	private void extractHolesInOne(Document doc) {
		DocumentFactory factory = (DocumentFactory) this.getBean("holesInOne");
		factory.setSeasonID(this.getSeasonID());
		doc.getRootElement().add(factory.create().getRootElement().detach());
		this.getLog().info("Finished for: " + factory.getRootName());
	}

	private void extractKpSweeper(Document doc) {
		DocumentFactory factory = (DocumentFactory) this.getBean("kpSweeper");
		factory.setSeasonID(this.getSeasonID());
		doc.getRootElement().add(factory.create().getRootElement().detach());
		this.getLog().info("Finished for: " + factory.getRootName());
	}

	private void extractThreePutt(Document doc) {
		DocumentFactory factory = (DocumentFactory) this.getBean("threePutt");
		factory.setSeasonID(this.getSeasonID());
		doc.getRootElement().add(factory.create().getRootElement().detach());
		this.getLog().info("Finished for: " + factory.getRootName());
	}

	private void extractBigMoney(Document doc) {
		DocumentFactory factory = (DocumentFactory) this.getBean("bigMoney");
		factory.setSeasonID(this.getSeasonID());
		doc.getRootElement().add(factory.create().getRootElement().detach());
		this.getLog().info("Finished for: " + factory.getRootName());
	}

	public int getSeasonID() {
		return seasonID;
	}

	public void setSeasonID(int seasonID) {
		this.seasonID = seasonID;
	}

	private DocumentFactory getBean(String bean) {
		return (DocumentFactory) this.getBeanFactory().getBean(bean);
	}

}
