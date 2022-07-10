package tourism.entity.event;

import java.io.IOException;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;

import tourism.entity.IQueryable;
import tourism.processor.DataProcessor;
import tourism.vocabulary.VNTOURISM;

public class Festival extends Event implements IQueryable {
	private static final String TARGETFILE = "festivals.txt";

	public Festival() {
		
	}

	public Festival(String name, String hasDescription, String hasTimeHappen) {
		super(name, hasDescription, hasTimeHappen);
	}

	public void queryToFile() throws IOException {
		String queryString = "prefix yago: <http://dbpedia.org/class/yago/>\r\n"
				+ "prefix dbp: <http://dbpedia.org/property/>\r\n"
				+ "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"
				+ "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"
				+ "\r\n"
				+ "SELECT DISTINCT ?festival ?name ?hasDescription (str(?begin) AS ?hasTimeHappen)\r\n"
				+ "WHERE {\r\n"
				+ "     {?festival rdf:type yago:WikicatFestivalsInVietnam.}\r\n"
				+ "     ?festival rdfs:label ?name.\r\n"
				+ "     ?festival rdfs:comment ?hasDescription.\r\n"
				+ "     OPTIONAL {?festival dbp:date ?begin.}\r\n"
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
		
		DataProcessor.writeToFile(m, Festival.TARGETFILE);
		qexec.close();
	}
	
	public Model processQuery(QuerySolution qs) throws IOException {
		Model localModel = ModelFactory.createDefaultModel();
		String name = qs.getLiteral("name").toString().replace(" ", "_");
		String hasDescription = qs.getLiteral("hasDescription").toString();
		Literal hasTimeHappenLiteral = qs.getLiteral("hasTimeHappen");
		String hasTimeHappen = "";
		if (hasTimeHappenLiteral != null)
			hasTimeHappen = hasTimeHappenLiteral.toString();
		Festival f = new Festival(name, hasDescription, hasTimeHappen);
			
		localModel.createResource(VNTOURISM.URI + f.getName(), VNTOURISM.Festival)
			.addLiteral(VNTOURISM.hasDescription, f.getHasDescription())
			.addLiteral(VNTOURISM.hasTimeHappen, f.getHasTimeHappen());
		
		return localModel;
	}

	
}
