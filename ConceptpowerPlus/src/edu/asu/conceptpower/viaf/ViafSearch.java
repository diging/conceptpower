package edu.asu.conceptpower.viaf;

	import java.io.File;
	import javax.xml.bind.JAXBContext;
	import javax.xml.bind.JAXBException;
	import javax.xml.bind.Unmarshaller;
	 
		
	public class ViafSearch {
		

	public static void main(String[] args) {
	 
		 try {
	 // converting xml to object
			File file = new File("C:\\file.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ViafSearch.class);
	 
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			ViafSearch search1 = (ViafSearch) jaxbUnmarshaller.unmarshal(file);
			System.out.println(search1);
	 
		  } catch (JAXBException e) {
			e.printStackTrace();
		  }
	 
	}
	}
	

