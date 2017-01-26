package com.jnaka.golf.dao;

import java.io.File;

import com.jnaka.golf.domain.Tournament;

public interface TournamentReader {

	public abstract Tournament read(File file);

}