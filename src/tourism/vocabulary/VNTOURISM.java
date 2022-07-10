package tourism.vocabulary;

import java.util.Map;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDFS;

public class VNTOURISM {
	public static final String URI = "http://www.semanticweb.org/minhn/ontologies/2021/0/vntourism#";
	public static final String PREFIX = "vntourism";
	
	public static final Map<String, String> PREFIXMAP = Map.of(
			VNTOURISM.PREFIX, VNTOURISM.URI,
			"rdfs", RDFS.getURI()
	);
	
	public static final Resource Festival = ResourceFactory.createResource(URI + "Festival");
	public static final Resource Person = ResourceFactory.createResource(URI + "Person");
	public static final Resource HeritageSite = ResourceFactory.createResource(URI + "HeritageSite");
	
	public static final Property hasBorn = ResourceFactory.createProperty(URI + "hasBorn");
	public static final Property hasBornAt = ResourceFactory.createProperty(URI + "hasBornAt");
	public static final Property hasDied = ResourceFactory.createProperty(URI + "hasDied");
	public static final Property hasReignFrom = ResourceFactory.createProperty(URI + "hasReignFrom");
	public static final Property hasReignTo = ResourceFactory.createProperty(URI + "hasReignTo");
	public static final Property hasSuccessor = ResourceFactory.createProperty(URI + "hasSuccessor");
	public static final Property hasDescription = ResourceFactory.createProperty(URI + "hasDescription");
	public static final Property hasTimeHappen = ResourceFactory.createProperty(URI + "hasTimeHappen");
	public static final Property hasAdministrativeDivision = ResourceFactory.createProperty(URI + "hasAdministrativeDivision");
	public static final Property hasBuildTime = ResourceFactory.createProperty(URI + "hasBuildTime");
}
