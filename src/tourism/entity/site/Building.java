package tourism.entity.site;

import java.io.IOException;

import tourism.entity.IQueryable;
import tourism.processor.DataProcessor;

public class Building extends TouristSite implements IQueryable{
	private String hasBuildTime;
	private static final String TARGETFILE = "buildings.txt";
	
	public String getHasBuildTime() {
		return hasBuildTime;
	}
	public void setHasBuildTime(String hasBuildTime) {
		this.hasBuildTime = hasBuildTime;
	}
	
	public Building() {
		
	}
	
	public Building(String name, String hasAdministrativeDivision, String hasBuildTime) {
		super(name, hasAdministrativeDivision);
		this.hasBuildTime = hasBuildTime;
	}
	
	public void query() throws IOException {
		String queryString = "prefix dbc: <http://dbpedia.org/resource/Category:>\r\n"
				+ "prefix dbp: <http://dbpedia.org/property/>\r\n"
				+ "prefix dbo: <http://dbpedia.org/ontology/>\r\n"
				+ "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
				+ "\r\n"
				+ "SELECT DISTINCT ?building ?name ?hasAdministrativeDivision ?hasBuildTime \r\n"
				+ "WHERE {\r\n"
				+ "     {?building dbo:wikiPageWikiLink dbc:Forts_in_Vietnam .}\r\n"
				+ "     UNION {?building dbo:wikiPageWikiLink dbc:Archaeological_sites_in_Vietnam .}\r\n"
				+ "     UNION {?building dbo:wikiPageWikiLink dbc:Monuments_and_memorials_in_Vietnam .}\r\n"
				+ "     UNION {?building dbo:wikiPageWikiLink dbc:Buddhist_temples_in_Vietnam .}\r\n"	
				+ "     ?building rdfs:label ?name.\r\n"
				+ "     ?building dbp:location ?hasAdministrativeDivision.\r\n"
				+ "     OPTIONAL {?building dbp:built ?hasBuildTime.}\r\n"
				+ "     OPTIONAL {?building dbp:established ?hasBuildTime.}\r\n"
				+ "     FILTER (lang(?name) = 'en')\r\n"
				+ "}";
		
		DataProcessor.QueryToFile(queryString, Building.TARGETFILE, new Building());
	}
	
}
