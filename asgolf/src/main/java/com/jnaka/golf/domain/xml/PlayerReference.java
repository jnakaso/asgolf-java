package com.jnaka.golf.domain.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * <pre>
 * 		<kp>
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
public class PlayerReference {

//  This is supposed to work
//	@XmlIDREF
//	@XmlAttribute(name = "playerID")
//	private Player player;
	
	@XmlAttribute(name = "playerID")
	private Integer playerID;
	
	@XmlTransient
	private Player player;


	public Integer getPlayerID() {
		return playerID;
	}

	public void setPlayerID(Integer playerID) {
		this.playerID = playerID;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
		this.playerID = player.getId().intValue();
	}

}
