package com.jnaka.golf.domain.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.jnaka.golf.domain.Flight;

/**
 * 
 * <pre>
 * <kp>
 * 			<player playerID="64">Nate Nouchi</player>
 * 			<flight>A</flight>
 * 			<hole>A05</hole>
 * 		</kp>
 * </pre>
 * 
 * @author nakasones
 * 
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Kp {

	@XmlElement(name = "player")
	private PlayerReference playerReference = new PlayerReference();
	private Flight flight;
	private String hole;

	public PlayerReference getPlayerReference() {
		return playerReference;
	}

	public void setPlayerReference(PlayerReference player) {
		this.playerReference = player;
	}

	public Player getPlayer() {
		return this.getPlayerReference().getPlayer();
	}

	public void setPlayer(Player player) {
		this.getPlayerReference().setPlayer(player);
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public String getHole() {
		return hole;
	}

	public void setHole(String hole) {
		this.hole = hole;
	}

}
