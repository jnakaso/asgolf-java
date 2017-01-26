package com.jnaka.golf.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jnaka.golf.domain.Kp;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;
import com.jnaka.reports.AbstractReportWriter;

@Scope("prototype")
@Service("TournamentWriter")
public class TournamentWriterImpl extends AbstractReportWriter implements TournamentWriter {

	private static final Comparator<? super Round> FLIGHT_NET_COMPARATOR = new Comparator<Round>() {
		@Override
		public int compare(Round arg0, Round arg1) {
			int result = -1;
			if (arg0.getFlight() != null && arg1.getFlight() != null) {
				result = arg0.getFlight().compareTo(arg1.getFlight());
				if (result == 0) {
					result = arg0.getTotalNet().compareTo(arg1.getTotalNet());
				}
			}
			return result;
		}
	};
	@Autowired
	@Qualifier("DomMapper")
	private DomMapper domMapper;
	private Tournament tournament;

	public TournamentWriterImpl() {
		super();
		this.setRootElementName("Tournament");
	}

	public void write(Tournament tournament, File file) {
		this.setFileName(file.getAbsolutePath());
		this.setTournament(tournament);
		this.extract();
	}

	public void write(Tournament tournament, String outFileName) {
		this.setFileName(outFileName);
		this.setTournament(tournament);
		this.extract();
	}

	@Override
	protected void updateDocument(Document doc) {
		this.appendTournamentInfo(doc);
		this.appendWinnerInfo(doc);
		this.appendKpInfo(doc);
		this.appendRoundsInfo(doc);
	}

	private void appendTournamentInfo(Document doc) {
		Tournament tour = this.getTournament();
		doc.setRootElement(this.getDomMapper().map(tour));
	}

	private void appendWinnerInfo(Document doc) {
		Set<Winner> winners = new TreeSet<Winner>(new Comparator<Winner>() {
			@Override
			public int compare(Winner o1, Winner o2) {
				int val = o1.getRound().getFlight().compareTo(o2.getRound().getFlight());
				if (val == 0) {
					val = o2.getPoints().compareTo(o1.getPoints());
					if (val == 0) {
						val = -1;
					}
				}
				return val;
			}
		});
		winners.addAll(this.getTournament().getWinners());

		Element root = doc.getRootElement();
		for (Winner winner : winners) {
			root.add(this.getDomMapper().map(winner));
		}
	}

	private void appendKpInfo(Document doc) {
		Element root = doc.getRootElement();
		for (Kp kp : this.getTournament().getKps()) {
			root.add(this.getDomMapper().map(kp));
		}
	}

	private void appendRoundsInfo(Document doc) {
		Element root = doc.getRootElement();
		List<Round> rounds = new ArrayList<Round>(this.getTournament().getRounds());
		Collections.sort(rounds, FLIGHT_NET_COMPARATOR);
		for (Round round : rounds) {
			root.add(this.getDomMapper().map(round));
		}
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	public DomMapper getDomMapper() {
		return this.domMapper;
	}

	public void setDomMapper(DomMapper domMapper) {
		this.domMapper = domMapper;
	}
}
