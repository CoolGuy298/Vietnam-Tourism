package tourism.entity.site;

import java.io.IOException;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDFS;
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
	
	public Building(String name, String label, String hasDescription, String hasAdministrativeDivision, String hasBuildTime) {
		super(name, label, hasDescription, hasAdministrativeDivision);
		this.hasBuildTime = hasBuildTime;
	}
	
	public void queryToFile() throws IOException {
		String queryString = "prefix dbc: <http://dbpedia.org/resource/Category:>\r\n"
				+ "prefix dbp: <http://dbpedia.org/property/>\r\n"
				+ "prefix dbo: <http://dbpedia.org/ontology/>\r\n"
				+ "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
				+ "\r\n"
				+ "SELECT DISTINCT ?building ?label ?hasDescription ?hasAdministrativeDivision ?hasBuildTime \r\n"
				+ "WHERE {\r\n"
				+ "     {?building dbo:wikiPageWikiLink dbc:Forts_in_Vietnam .}\r\n"
				+ "     UNION {?building dbo:wikiPageWikiLink dbc:Archaeological_sites_in_Vietnam .}\r\n"
				+ "     UNION {?building dbo:wikiPageWikiLink dbc:Monuments_and_memorials_in_Vietnam .}\r\n"
				+ "     UNION {?building dbo:wikiPageWikiLink dbc:Buddhist_temples_in_Vietnam .}\r\n"	
				+ "     ?building rdfs:label ?label.\r\n"
				+ "     ?building rdfs:comment ?hasDescription.\r\n"
				+ "     ?building dbp:location ?hasAdministrativeDivision.\r\n"
				+ "     OPTIONAL {?building dbp:built ?hasBuildTime.}\r\n"
				+ "     OPTIONAL {?building dbp:established ?hasBuildTime.}\r\n"
				+ "     FILTER (lang(?label) = 'en')\r\n"
				+ "     FILTER (lang(?hasDescription) = 'en')\r\n"
				+ "}";
		
		QueryExecution qexec = DataProcessor.getQueryConnection(queryString);
		ResultSet results = qexec.execSelect();
		Model m = ModelFactory.createDefaultModel();
		m.setNsPrefixes(VNTOURISM.PREFIXMAP);
		
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
		String name = DataProcessor.processImpl(qs, "building");
		String label = qs.getLiteral("label").toString();
		String hasDescription = qs.getLiteral("hasDescription").toString();
		String hasAdministrativeDivision = DataProcessor.processImpl(qs, "hasAdministrativeDivision");
		Literal hasBuildTimeLiteral = qs.getLiteral("hasBuildTime");
		String hasBuildTime = "";
		if (hasBuildTimeLiteral != null)
			hasBuildTime = hasBuildTimeLiteral.toString();
		Building b = new Building(name, label, hasDescription, hasAdministrativeDivision, hasBuildTime);
			
		localModel.createResource(VNTOURISM.URI + b.getName(), VNTOURISM.HeritageSite)
				.addLiteral(RDFS.label, b.getLabel())
				.addLiteral(VNTOURISM.hasDescription, b.getHasDescription())
				.addLiteral(VNTOURISM.hasAdministrativeDivision, b.getHasAdministrativeDivision())
				.addLiteral(VNTOURISM.hasBuildTime, b.getHasBuildTime());
		
		return localModel;
	}
	
}
