package com.jnaka.golf.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jnaka.golf.GolfJsonExporter;
import com.jnaka.golf.dao.JaxbDataStore;
import com.jnaka.golf.domain.GolfClub;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.xml.GolfClubWriterImpl;
import com.jnaka.golf.service.GolfClubService;
import com.jnaka.golf.service.PrizeCalculator;

@Service
public class GolfClubServiceImpl implements GolfClubService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Value("${data.filepath}")
	private String fileName;

	@Autowired
	private JaxbDataStore dataStore;
	
	@Autowired
	GolfJsonExporter exporter;

	@Override
	public GolfClub load(File file) {
		this.getLogger().warning("load");
		//FIXME
		return null;
	}

	@Override
	public boolean save() {
		this.getLogger().warning("save");
		try {
			GolfClub club = this.getDataStore().getGolfClub();
			
			FileWriter writer = new FileWriter(this.fileName);
			GolfClubWriterImpl clubWriter = new GolfClubWriterImpl();
			clubWriter.write(club, writer);
			this.getLogger().warning("Done saving " + this.fileName);
			return true;
		} catch (IOException | JAXBException e) {
			return false;
		}
	}

	@Override
	public PrizeCalculator getPrizeCalculator(GolfClub club) {
		//FIXME
		this.getLogger().warning("getPrizeCalculator");
		return null;
	}

	@Override
	public Tournament uploadTournament(Integer seasonId, File file) {
		//FIXME
		this.getLogger().warning("uploadTournament");
		return null;
	}

	@Override
	public boolean exportSeason(Integer season) {
		this.getLogger().warning("exportSeason");
		this.getExporter().export(season);
		return true;
	}
	
	JaxbDataStore getDataStore() {
		return this.dataStore;
	}

	void setDataStore(JaxbDataStore dataStore) {
		this.dataStore = dataStore;
	}

	GolfJsonExporter getExporter() {
		return exporter;
	}

	void setExporter(GolfJsonExporter exporter) {
		this.exporter = exporter;
	}

	Logger getLogger() {
		return logger;
	}

}
