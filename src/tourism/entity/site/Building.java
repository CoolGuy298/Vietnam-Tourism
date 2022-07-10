package tourism.entity.site;

import java.io.IOException;

import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;

import tourism.entity.IQueryable;
import tourism.processor.DataProcessor;
import tourism.vocabulary.VNTOURISM;

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
		
		QueryExecution qexec = DataProcessor.getQueryConnection(queryString);
		ResultSet results = qexec.execSelect();
		Model m = ModelFactory.createDefaultModel();
		m.setNsPrefix(VNTOURISM.PREFIX, VNTOURISM.URI);
		
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			Model temp = this.processQuery(qs);
			m = m.union(temp);
		}
		
		DataProcessor.writeToFile(m, Building.TARGETFILE);
		qexec.close();
	}
	
	public Model processQuery(QuerySolution qs) throws IOException {
		Model localModel = ModelFactory.createDefaultModel();
		String name = qs.getLiteral("name").toString().replace(" ", "_");
		String hasAdministrativeDivision = DataProcessor.processImpl(qs, "hasAdministrativeDivision");
		Literal hasBuildTimeLiteral = qs.getLiteral("hasBuildTime");
		String hasBuildTime = "";
		if (hasBuildTimeLiteral != null)
			hasBuildTime = hasBuildTimeLiteral.toString();
		Building b = new Building(name, hasAdministrativeDivision, hasBuildTime);
			
		localModel.createResource(VNTOURISM.URI + b.getName(), VNTOURISM.HeritageSite)
				.addLiteral(VNTOURISM.hasAdministrativeDivision, b.getHasAdministrativeDivision())
				.addLiteral(VNTOURISM.hasBuildTime, b.getHasBuildTime());
		
		return localModel;
	}
	
}
