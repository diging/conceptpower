package edu.asu.conceptpower.app.profile.impl;

import edu.asu.conceptpower.app.jaxb.viaf.Channel;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * 
 * jaxb class for parsing xml returned by calling viaf service
 * 
 * @author rohit pendbhaje
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "rss")
public class ViafReply {

	@XmlElement(name = "channel")
	private Channel channel;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}
