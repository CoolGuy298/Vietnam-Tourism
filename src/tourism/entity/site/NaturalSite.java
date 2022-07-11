package tourism.entity.site;

import java.io.IOException;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDFS;

import tourism.entity.IQueryable;
import tourism.processor.DataProcessor;
import tourism.vocabulary.VNTOURISM;

public class NaturalSite extends TouristSite implements IQueryable{
	private static final String TARGETFILE = "naturalsites.txt";
	
	public NaturalSite() {
		
	}
	
	public NaturalSite(String name, String label, String hasDescription, String hasAdministrativeDivision) {
		super(name, label, hasDescription, hasAdministrativeDivision);
	}
	
	public String queryString() {
		return "prefix yago: <http://dbpedia.org/class/yago/>\r\n"
				+ "prefix dbp: <http://dbpedia.org/property/>\r\n"
				+ "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
				+ "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
				+ "\r\n"
				+ "SELECT DISTINCT ?site ?label ?hasDescription ?hasAdministrativeDivision \r\n"
				+ "WHERE {\r\n"
				+ "     {?site rdf:type yago:WikicatIslandsOfVietnam.}\r\n"
				+ "     UNION {?site rdf:type yago:WikicatCavesOfVietnam.}\r\n"
				+ "     UNION {?site rdf:type yago:WikicatRiversOfVietnam.}\r\n"
				+ "     UNION {?site rdf:type yago:WikicatLakesOfVietnam.}\r\n"
				+ "     UNION {?site rdf:type yago:WikicatNationalParksOfVietnam.}\r\n"
				+ "     ?site rdfs:label ?label.\r\n"
				+ "     ?site rdfs:comment ?hasDescription.\r\n"
				+ "     ?site dbp:location ?hasAdministrativeDivision.\r\n"
				+ "     FILTER (lang(?label) = 'en')\r\n"
				+ "     FILTER (lang(?hasDescription) = 'en')\r\n"
				+ "}";
	}
	
	public void queryToFile() throws IOException {
		String queryString = this.queryString();
		
		QueryExecution qexec = DataProcessor.getQueryConnection(queryString);
		ResultSet results = qexec.execSelect();
		
		Model m = ModelFactory.createDefaultModel();
		m.setNsPrefixes(VNTOURISM.PREFIXMAP);
		
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			NaturalSite nt = (NaturalSite) this.getObjectFromQuery(qs);
			Model temp = this.getModelInstance(nt);
			m = m.union(temp);
		}
		
		DataProcessor.writeToFile(m, NaturalSite.TARGETFILE);
		qexec.close();
	}
	
	public IQueryable getObjectFromQuery(QuerySolution qs) throws IOException {
		String name = DataProcessor.processImpl(qs, "site");
		String label = qs.getLiteral("label").toString();
		String hasDescription = qs.getLiteral("hasDescription").toString();
		String hasAdministrativeDivision = DataProcessor.processImpl(qs, "hasAdministrativeDivision");
		
		return new NaturalSite(name, label, hasDescription, hasAdministrativeDivision);
	}
	
	public Model getModelInstance(IQueryable queryableEntity) {
		NaturalSite nt = (NaturalSite) queryableEntity;
		Model localModel = ModelFactory.createDefaultModel();
		localModel.createResource(VNTOURISM.URI + nt.getName(), VNTOURISM.HeritageSite)
				.addLiteral(RDFS.label, nt.getLabel())
				.addLiteral(VNTOURISM.hasDescription, nt.getHasDescription())
				.addLiteral(VNTOURISM.hasAdministrativeDivision, nt.getHasAdministrativeDivision());
		
		return localModel;
	}
		
}
