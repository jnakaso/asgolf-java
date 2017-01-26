package com.jnaka.golf.domain.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jnaka.domain.xml.CurrencyAdapter;
import com.jnaka.golf.domain.Flight;

/**
 * 
 * <winner> <player>Bruce Kaneshiro</player> <flight>A</flight>
 * <points>9.0</points> <finish>A1</finish> <earnings>$20.00</earnings>
 * <net>68</net> </winner>
 * 
 * @author nakasones
 * 
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Winner {

	@XmlElement(name = "player")
	private PlayerReference playerReference = new PlayerReference();
	private Flight flight;
	private Float points;
	private String finish;
	@XmlJavaTypeAdapter(CurrencyAdapter.class)
	private Float earnings;
	private int net;

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

	public Float getPoints() {
		return points;
	}

	public void setPoints(Float points) {
		this.points = points;
	}

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

	public Float getEarnings() {
		return earnings;
	}

	public void setEarnings(Float earnings) {
		this.earnings = earnings;
	}

	public int getNet() {
		return net;
	}

	public void setNet(int net) {
		this.net = net;
	}

}
