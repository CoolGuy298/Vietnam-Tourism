package tourism.entity.event;

import java.io.IOException;

import tourism.entity.IQueryable;
import tourism.processor.DataProcessor;

public class Festival extends Event implements IQueryable {
	private String hasDescription;
	
	private static final String TARGETFILE = "festivals.txt";

	public String getHasDescription() {
		return hasDescription;
	}

	public void setHasDescription(String hasDescription) {
		this.hasDescription = hasDescription;
	}
	
	public Festival() {
		
	}

	public Festival(String name, String hasTimeHappen, String hasDescription) {
		super(name, hasTimeHappen);
		this.hasDescription = hasDescription;
	}

	public void query() throws IOException {
		String queryString = "prefix yago: <http://dbpedia.org/class/yago/>\r\n"
				+ "prefix dbp: <http://dbpedia.org/property/>\r\n"
				+ "prefix dbo: <http://dbpedia.org/ontology/>\r\n"
				+ "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
				+ "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
				+ "\r\n"
				+ "SELECT DISTINCT ?festival ?name ?hasDescription (str(?begin) AS ?hasTimeHappen)\r\n"
				+ "WHERE {\r\n"
				+ "     {?festival rdf:type yago:WikicatFestivalsInVietnam.}\r\n"
				+ "     ?festival rdfs:label ?name.\r\n"
				+ "     ?festival dbo:abstract ?hasDescription.\r\n"
				+ "     OPTIONAL {?festival dbp:date ?begin.}\r\n"
				+ "     FILTER (lang(?name) = 'en')\r\n"
				+ "     FILTER (lang(?hasDescription) = 'en')\r\n"
				+ "}";
		
		DataProcessor.QueryToFile(queryString, Festival.TARGETFILE, new Festival());
	}

	
}
