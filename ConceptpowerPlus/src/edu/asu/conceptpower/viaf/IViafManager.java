package edu.asu.conceptpower.viaf;
import java.util.List;

	import java.util.List;

	import edu.asu.viaf.ViafReply;


	public interface IViafManager {
		public List<ViafReply.Items> search(String item, int startIndex);
	}


