package tourism.processor;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class DataProcessor {
	public static final String DIRECTORY = "target/";
	public static final String OUTPUTFORMAT = "Turtle";
	
	
	public static QueryExecution getQueryConnection(String queryString){
		Query query = QueryFactory.create(queryString);
		@SuppressWarnings("deprecation")
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
		return qexec;
	}
	
	public static void writeToFile(Model m, String targetFile) throws IOException {
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(DataProcessor.DIRECTORY + targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		m.write(outputStream, DataProcessor.OUTPUTFORMAT);
		outputStream.close();
	}
	
	public static String processImpl(QuerySolution qs, String prop) throws IOException {
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
	
}
