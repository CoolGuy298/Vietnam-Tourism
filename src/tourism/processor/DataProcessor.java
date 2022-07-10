package tourism.processor;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;

import tourism.entity.IQueryable;
import tourism.entity.event.Festival;
import tourism.entity.person.King;
import tourism.entity.site.*;
import tourism.vocabulary.VNTOURISM;

import java.io.FileOutputStream;
import java.io.IOException;

public class DataProcessor {
	public static final String DIRECTORY = "target/";
	public static final String OUTPUTFORMAT = "Turtle";
	
	public static void QueryToFile(String queryString, String targetFile, IQueryable entity) throws IOException {
		Query query = QueryFactory.create(queryString);
		@SuppressWarnings("deprecation")
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
		
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(DataProcessor.DIRECTORY + targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Model m = ModelFactory.createDefaultModel();
		m.setNsPrefix(VNTOURISM.PREFIX, VNTOURISM.URI);
		
		try {
			ResultSet results = qexec.execSelect();
			for (; results.hasNext();) {
				QuerySolution qs = results.next();
				Model temp = DataProcessor.processQueryResult(qs, entity);
				m = m.union(temp);
				
			}
		}
		finally {
			qexec.close();
		}
		
		m.write(outputStream, DataProcessor.OUTPUTFORMAT);
		outputStream.close();
	}
	
	private static String processImpl(QuerySolution qs, String prop) throws IOException {
		String outputProp = "";
		RDFNode propNode = qs.get(prop);
		if (propNode.isLiteral())
			outputProp = propNode.asLiteral().getString();
		else if (propNode.isResource())
			outputProp = propNode.asResource().getLocalName();
		else
			throw new IOException();
		
		return outputProp;
	}
	
	private static Model processQueryResult(QuerySolution qs, IQueryable entity) throws IOException {
		Model local = ModelFactory.createDefaultModel();
		
		if (entity instanceof King) {
			King k = (King) entity;
			String name = qs.getLiteral("name").toString().replace(" ", "_");
			String hasBorn = qs.getLiteral("hasBorn").toString();
			String hasBornAt = DataProcessor.processImpl(qs, "hasBornAt");
			String hasDied = qs.getLiteral("hasDied").toString();
			String hasReignFrom = qs.getLiteral("hasReignFrom").toString();
			String hasReignTo = qs.getLiteral("hasReignTo").toString();
			String hasSuccessor = DataProcessor.processImpl(qs, "hasSuccessor");
				
			k = new King(name, hasBorn, hasBornAt, hasDied, hasReignFrom, hasReignTo, hasSuccessor);
				
			local.createResource(VNTOURISM.URI + k.getName(), VNTOURISM.Person)
				.addLiteral(VNTOURISM.hasBorn, k.getHasBorn())
				.addLiteral(VNTOURISM.hasBornAt, k.getHasBornAt())
				.addLiteral(VNTOURISM.hasDied, k.getHasDied())
				.addLiteral(VNTOURISM.hasReignFrom, k.getHasReignFrom())
				.addLiteral(VNTOURISM.hasReignTo, k.getHasReignTo())
				.addLiteral(VNTOURISM.hasSuccessor, k.getHasSuccessor());
		}
		else if (entity instanceof Festival) {
			Festival f = (Festival) entity;
			String name = qs.getLiteral("name").toString().replace(" ", "_");
			String hasDescription = qs.getLiteral("hasDescription").toString();
			Literal hasTimeHappenLiteral = qs.getLiteral("hasTimeHappen");
			String hasTimeHappen = "";
			if (hasTimeHappenLiteral != null)
				hasTimeHappen = hasTimeHappenLiteral.toString();
			f = new Festival(name, hasDescription, hasTimeHappen);
			
			local.createResource(VNTOURISM.URI + f.getName(), VNTOURISM.Festival)
				.addLiteral(VNTOURISM.hasDescription, f.getHasDescription())
				.addLiteral(VNTOURISM.hasTimeHappen, f.getHasTimeHappen());
		}
		else if (entity instanceof Building) {
			Building b = (Building) entity;
			String name = qs.getLiteral("name").toString().replace(" ", "_");
			String hasAdministrativeDivision = DataProcessor.processImpl(qs, "hasAdministrativeDivision");
			Literal hasBuildTimeLiteral = qs.getLiteral("hasBuildTime");
			String hasBuildTime = "";
			if (hasBuildTimeLiteral != null)
				hasBuildTime = hasBuildTimeLiteral.toString();
			b = new Building(name, hasAdministrativeDivision, hasBuildTime);
			
			local.createResource(VNTOURISM.URI + b.getName(), VNTOURISM.HeritageSite)
				 .addLiteral(VNTOURISM.hasAdministrativeDivision, b.getHasAdministrativeDivision())
				 .addLiteral(VNTOURISM.hasBuildTime, b.getHasBuildTime());
		}
		
		return local;
	}
}
