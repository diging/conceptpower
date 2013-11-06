package edu.asu.conceptpower.viaf;
	
	
	import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(propOrder = { "items"})
	@XmlRootElement(name="rss")
	public class ViafReply {
		/*	
		@XmlElementWrapper(name="channel")
		@XmlElement(name="item")
		private List<ViafReply.Items> items;
		
		public List<ViafReply.Items> getItems() {
			return items;
		}
		public void setItems(List<ViafReply.Items> items) {
			this.items = items;
		}*/
	
	
	
}
