package com.jnaka.golf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CLI {

	Log log = LogFactory.getLog(this.getClass());
	@Autowired
	TournamentProcessor processor;

	@Autowired
	@Qualifier("GolfJsonExporter")
	GolfJsonExporter exporter;

	public void processor(String args[]) {
		this.log.info("Start processing");

		this.log.info("Processing file" + args[0]);
		try {
			this.processor.process(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.log.info("End processing");
	}

	public void exporter(String args[]) {
		this.log.info("Start exporting");
		if (args.length == 1) {
			this.log.info("Start seasons" + args[0]);
			Integer year = Integer.valueOf(args[0]);
			this.exporter.export(year);
		} else if (args.length == 0) {
			this.log.info("Start all Seasons exporting");
			this.exporter.exportAll();
		}
		this.log.info("Done exporting");
	}

}
