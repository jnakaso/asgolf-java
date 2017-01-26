package com.jnaka.golf.domain.xml;

import java.io.Reader;

import javax.xml.bind.JAXBException;

import com.jnaka.golf.domain.GolfClub;

public interface GolfClubReader {

	public GolfClub read(Reader reader) throws JAXBException;

}