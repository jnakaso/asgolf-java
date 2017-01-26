package com.jnaka.golf.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.jnaka.golf.domain.GolfClub;
import com.jnaka.golf.domain.xml.GolfClubReader;
import com.jnaka.golf.domain.xml.GolfClubReaderImpl;

@Repository("DataStore")
public class JaxbDataStore implements DataStore {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Value("${data.filepath}")
	private String fileName;

	private GolfClub golfClub;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void change(File newFile) {
		this.logger.info("file = : " + newFile);
		this.setFileName(newFile.getAbsolutePath());
		this.setGolfClub(null);
	}

	public GolfClub getGolfClub() {
		if (this.golfClub == null) {
			synchronized (this) {
				this.logger.info(this.hashCode() + " loading golf club." );
				this.setGolfClub(this.defaultClub());
			}
		}
		return golfClub;
	}

	private GolfClub defaultClub() {
		try {
			this.logger.info("Reading file: " + this.getFileName());
			FileReader reader = new FileReader(this.getFileName());
			GolfClubReader clubReader = new GolfClubReaderImpl();
			return clubReader.read(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setGolfClub(GolfClub golfClub) {
		this.golfClub = golfClub;
	}

}
