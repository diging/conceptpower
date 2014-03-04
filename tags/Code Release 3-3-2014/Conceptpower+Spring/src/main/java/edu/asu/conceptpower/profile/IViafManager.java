package edu.asu.conceptpower.profile;

import java.util.List;

import edu.asu.conceptpower.jaxb.viaf.Item;
import edu.asu.conceptpower.jaxb.viaf.Channel;


public interface IViafManager {
	//public List<ViafReply.Items> search(String item, String startIndex);

	public List<ISearchResult> search(String item, String startIndex);
}
