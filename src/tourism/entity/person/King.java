package tourism.entity.person;

import java.io.IOException;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDFS;

import tourism.entity.IQueryable;
import tourism.processor.DataProcessor;
import tourism.vocabulary.VNTOURISM;

public class King extends Person implements IQueryable{
	private String hasReignFrom;
	private String hasReignTo;
	private String hasSuccessor;
	private static final String TARGETFILE = "kings.txt";
	
	public King() { 
		super();
	}
	
	public King(String name, String label, String hasDescription, String hasBorn, String hasBornAt,
			String hasDied, String hasReignFrom, String hasReignTo,
			String hasSuccessor) {
		super(name, label, hasDescription, hasBorn, hasBornAt, hasDied);
		this.hasReignFrom = hasReignFrom;
		this.hasReignTo = hasReignTo;
		this.hasSuccessor = hasSuccessor;
	}
	
	public String getHasReignFrom() {
		return hasReignFrom;
	}
	public void setHasReignFrom(String hasReignFrom) {
		this.hasReignFrom = hasReignFrom;
	}
	public String getHasReignTo() {
		return hasReignTo;
	}
	public void setHasReignTo(String hasReignTo) {
		this.hasReignTo = hasReignTo;
	}
	public String getHasSuccessor() {
		return hasSuccessor;
	}
	public void setHasSuccessor(String hasSuccessor) {
		this.hasSuccessor = hasSuccessor;
	}
	
	public String queryString() {
		return "prefix dbp: <http://dbpedia.org/property/>\r\n"
				+ "prefix dbo: <http://dbpedia.org/ontology/>\r\n"
				+ "prefix dbr: <http://dbpedia.org/resource/>\r\n"
				+ "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
				+ "SELECT DISTINCT ?king ?label ?hasDescription (str(?born) AS ?hasBorn) ?hasBornAt (str(?died) AS ?hasDied) (str(?from) AS ?hasReignFrom) (str(?to) AS ?hasReignTo) ?hasSuccessor\r\n"
				+ "WHERE {\r\n"
				+ "     {<http://dbpedia.org/resource/List_of_monarchs_of_Vietnam> dbo:wikiPageWikiLink ?king}\r\n"
				+ "     {?king dbo:wikiPageWikiLink dbr:List_of_monarchs_of_Vietnam.}\r\n"
				+ "     ?king rdfs:label ?label.\r\n"
				+ "     ?king rdfs:comment ?hasDescription.\r\n"
				+ "     ?king dbp:birthDate ?born.\r\n"
				+ "     ?king dbp:birthPlace ?hasBornAt.\r\n"
				+ "     ?king dbp:deathDate ?died.\r\n"
				+ "     ?king dbo:activeYearsStartYear ?from.\r\n"
				+ "     ?king dbo:activeYearsEndYear ?to.\r\n"
				+ "     ?king dbo:successor ?hasSuccessor.\r\n"
				+ "     FILTER (lang(?label) = 'en')\r\n"
				+ "     FILTER (lang(?hasDescription) = 'en')\r\n"
				+ "}";
	}

	public void queryToFile() throws IOException {
		String queryString = this.queryString();
		
		// Retrieve connection to obtain the results
		QueryExecution qexec = DataProcessor.getQueryConnection(queryString);
		ResultSet results = qexec.execSelect();
		
		// Container model to store triples
		Model m = ModelFactory.createDefaultModel();
		m.setNsPrefixes(VNTOURISM.PREFIXMAP);
		
		// Iterate through the result set to add on to the container model
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			King k = (King) this.getObjectFromQuery(qs);
			Model temp = this.getModelInstance(k);
			
			// Add triple from obtained local model to the container model
			m = m.union(temp);
		}
		
		DataProcessor.writeToFile(m, King.TARGETFILE);
		
		// Close the connection
		qexec.close();
	}
	
	public IQueryable getObjectFromQuery(QuerySolution qs) throws IOException {
		String name = DataProcessor.processImpl(qs, "king");
		String label = qs.getLiteral("label").toString();
		String hasDescription = qs.getLiteral("hasDescription").toString();
		String hasBorn = qs.getLiteral("hasBorn").toString();
		String hasBornAt = DataProcessor.processImpl(qs, "hasBornAt");
		String hasDied = qs.getLiteral("hasDied").toString();
		String hasReignFrom = qs.getLiteral("hasReignFrom").toString();
		String hasReignTo = qs.getLiteral("hasReignTo").toString();
		String hasSuccessor = DataProcessor.processImpl(qs, "hasSuccessor");
				
		return new King(name, label, hasDescription, hasBorn, hasBornAt, hasDied, hasReignFrom,
				hasReignTo, hasSuccessor);

	}
	
	public Model getModelInstance(IQueryable queryableEntity) {
		King k = (King) queryableEntity;
		// Add triple to the local model
		Model localModel = ModelFactory.createDefaultModel();
		localModel.createResource(VNTOURISM.URI + k.getName(), VNTOURISM.Person)
			.addLiteral(RDFS.label, k.getLabel())
			.addLiteral(VNTOURISM.hasDescription, k.getHasDescription())
			.addLiteral(VNTOURISM.hasBorn, k.getHasBorn())
			.addLiteral(VNTOURISM.hasBornAt, k.getHasBornAt())
			.addLiteral(VNTOURISM.hasDied, k.getHasDied())
			.addLiteral(VNTOURISM.hasReignFrom, k.getHasReignFrom())
			.addLiteral(VNTOURISM.hasReignTo, k.getHasReignTo())
			.addLiteral(VNTOURISM.hasSuccessor, k.getHasSuccessor());
		
		return localModel;
		
	}
}
