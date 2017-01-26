package com.jnaka.golf.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.jnaka.golf.domain.Tournament;

@Repository("TournamentReader")
public class TournamentReaderImpl implements TournamentReader {

	private final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	@Qualifier("DataStore")
	private JaxbDataStore dataStore;
	@Autowired
	@Qualifier("DomMapper")
	private DomMapper domMapper;

	public Tournament read(File file) {
		this.getLog().debug("loading" + file.getAbsolutePath());
		Document document = this.loadDocument(file);
		if (document == null) {
			return null;
		}
		return this.getDomMapper().mapTournament(document.getRootElement());
	}

	private Document loadDocument(File file) {
		try {
			InputStream ip = new FileInputStream(file);
			SAXReader saxReader = new SAXReader();
			return saxReader.read(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	public JaxbDataStore getDataStore() {
		return dataStore;
	}

	public void setDataStore(JaxbDataStore dataStore) {
		this.dataStore = dataStore;
	}

	public DomMapper getDomMapper() {
		return domMapper;
	}

	public void setDomMapper(DomMapper domMapper) {
		this.domMapper = domMapper;
	}

	private Log getLog() {
		return log;
	}

}
