package com.jnaka.golf.domain.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.StringUtils;

import com.jnaka.domain.xml.IdAdapter;
import com.jnaka.domain.xml.IntegerArrayAdapter;
import com.jnaka.domain.xml.Number2IntegerAdapter;
import com.jnaka.golf.domain.Flight;

/**
 * 
 * <pre>
 * <round roundID="3374">
 * 			<player playerID="52">Dan Tamashiro</player>
 * 			<flight>B</flight>
 * 			<hdcp>19</hdcp>
 * 			<front>48</front>
 * 			<back>42</back>
 * 			<total>90</total>
 * 			<frontNet>38.5</frontNet>
 * 			<backNet>32.5</backNet>
 * 			<totalNet>71.0</totalNet>
 * 			<adjusted>90.0</adjusted>
 * 			<accepted>*</accepted>
 * 			<scores>5,5,5,6,4,5,5,7,6,4,5,4,5,6,4,5,5,4</scores>
 * 		</round>
 * </pre>
 * 
 * @author nakasones
 * 
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Round {

	@XmlID
	@XmlAttribute(name = "roundID")
	@XmlJavaTypeAdapter(IdAdapter.class)
	private Number id;

	@XmlElement(name = "player")
	private PlayerReference playerReference = new PlayerReference();
	
	private Flight flight;

	@XmlElement(name = "hdcp")
	private Integer handicap = 0;
	private Integer front = 0;
	private Integer back = 0;
	private Integer total = 0;
	private Float frontNet = 0f;
	private Float backNet = 0f;
	private Float totalNet = 0f;

	@XmlJavaTypeAdapter(IntegerArrayAdapter.class)
	private Integer[] scores;
	private String accepted = StringUtils.EMPTY;
	@XmlJavaTypeAdapter(Number2IntegerAdapter.class)
	private Integer adjusted = 0;

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

	public PlayerReference getPlayerReference() {
		return playerReference;
	}

	public void setPlayerReference(PlayerReference playerReference) {
		this.playerReference = playerReference;
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

	public Integer getHandicap() {
		return handicap;
	}

	public void setHandicap(Integer handicap) {
		this.handicap = handicap;
	}

	public Integer getFront() {
		return front;
	}

	public void setFront(Integer front) {
		this.front = front;
	}

	public Integer getBack() {
		return back;
	}

	public void setBack(Integer back) {
		this.back = back;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Float getFrontNet() {
		return frontNet;
	}

	public void setFrontNet(Float frontNet) {
		this.frontNet = frontNet;
	}

	public Float getBackNet() {
		return backNet;
	}

	public void setBackNet(Float backNet) {
		this.backNet = backNet;
	}

	public Float getTotalNet() {
		return totalNet;
	}

	public void setTotalNet(Float totalNet) {
		this.totalNet = totalNet;
	}

	public Integer[] getScores() {
		return scores;
	}

	public void setScores(Integer[] scores) {
		this.scores = scores;
	}

	public String getAccepted() {
		return accepted;
	}

	public void setAccepted(String accepted) {
		this.accepted = accepted;
	}

	public Integer getAdjusted() {
		return adjusted;
	}

	public void setAdjusted(Integer adjusted) {
		this.adjusted = adjusted;
	}

}
