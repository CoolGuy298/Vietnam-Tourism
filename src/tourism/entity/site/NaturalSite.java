package tourism.entity.site;

import java.io.IOException;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;

import tourism.entity.IQueryable;
import tourism.processor.DataProcessor;
import tourism.vocabulary.VNTOURISM;

public class NaturalSite extends TouristSite implements IQueryable{
	private static final String TARGETFILE = "naturalsites.txt";
	
	public NaturalSite() {
		
	}
	
	public NaturalSite(String name, String hasDescription, String hasAdministrativeDivision) {
		super(name, hasDescription, hasAdministrativeDivision);
	}
	
	public void queryToFile() throws IOException {
		String queryString = "prefix yago: <http://dbpedia.org/class/yago/>\r\n"
				+ "prefix dbp: <http://dbpedia.org/property/>\r\n"
				+ "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
				+ "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
				+ "\r\n"
				+ "SELECT DISTINCT ?site ?name ?hasDescription ?hasAdministrativeDivision \r\n"
				+ "WHERE {\r\n"
				+ "     {?site rdf:type yago:WikicatIslandsOfVietnam.}\r\n"
				+ "     UNION {?site rdf:type yago:WikicatCavesOfVietnam.}\r\n"
				+ "     UNION {?site rdf:type yago:WikicatRiversOfVietnam.}\r\n"
				+ "     UNION {?site rdf:type yago:WikicatLakesOfVietnam.}\r\n"
				+ "     UNION {?site rdf:type yago:WikicatNationalParksOfVietnam.}\r\n"
				+ "     ?site rdfs:label ?name.\r\n"
				+ "     ?site rdfs:comment ?hasDescription.\r\n"
				+ "     ?site dbp:location ?hasAdministrativeDivision.\r\n"
				+ "     FILTER (lang(?name) = 'en')\r\n"
				+ "     FILTER (lang(?hasDescription) = 'en')\r\n"
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
		
		DataProcessor.writeToFile(m, NaturalSite.TARGETFILE);
		qexec.close();
	}
	
	public Model processQuery(QuerySolution qs) throws IOException {
		Model localModel = ModelFactory.createDefaultModel();
		String name = qs.getLiteral("name").toString().replace(" ", "_");
		String hasDescription = qs.getLiteral("hasDescription").toString();
		String hasAdministrativeDivision = DataProcessor.processImpl(qs, "hasAdministrativeDivision");

		NaturalSite nt = new NaturalSite(name, hasDescription, hasAdministrativeDivision);
			
		localModel.createResource(VNTOURISM.URI + nt.getName(), VNTOURISM.HeritageSite)
				.addLiteral(VNTOURISM.hasDescription, nt.getHasDescription())
				.addLiteral(VNTOURISM.hasAdministrativeDivision, nt.getHasAdministrativeDivision());
		
		return localModel;
	}
		
}
