package com.jnaka.golf.dao;

import java.io.File;

import com.jnaka.golf.domain.Tournament;

public interface TournamentWriter {

	public abstract void write(Tournament tournament, String outFileName);

	public abstract void write(Tournament tournament, File selected);

}