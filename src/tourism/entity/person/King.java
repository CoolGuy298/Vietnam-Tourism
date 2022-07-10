package tourism.entity.person;

import java.io.IOException;

import tourism.entity.IQueryable;
import tourism.processor.DataProcessor;

public class King extends Person implements IQueryable{
	private String hasReignFrom;
	private String hasReignTo;
	private String hasSuccessor;
	private static final String TARGETFILE = "kings.txt";
	
	public King() { 
		super();
	}
	
	public King(String name, String hasBorn, String hasBornAt,
			String hasDied, String hasReignFrom, String hasReignTo,
			String hasSuccessor) {
		super(name, hasBorn, hasBornAt, hasDied);
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

	public void query() throws IOException {
		String queryString = "prefix dbp: <http://dbpedia.org/property/>\r\n"
				+ "prefix dbo: <http://dbpedia.org/ontology/>\r\n"
				+ "prefix dbr: <http://dbpedia.org/resource/>\r\n"
				+ "SELECT DISTINCT ?king ?name (str(?born) AS ?hasBorn) ?hasBornAt (str(?died) AS ?hasDied) (str(?from) AS ?hasReignFrom) (str(?to) AS ?hasReignTo) ?hasSuccessor\r\n"
				+ "WHERE {\r\n"
				+ "     {<http://dbpedia.org/resource/List_of_monarchs_of_Vietnam> dbo:wikiPageWikiLink ?king}\r\n"
				+ "     {?king dbo:wikiPageWikiLink dbr:List_of_monarchs_of_Vietnam.}\r\n"
				+ "     ?king dbp:fullName ?name.\r\n"
				+ "     ?king dbp:birthDate ?born.\r\n"
				+ "     ?king dbp:birthPlace ?hasBornAt.\r\n"
				+ "     ?king dbp:deathDate ?died.\r\n"
				+ "     ?king dbo:activeYearsStartYear ?from.\r\n"
				+ "     ?king dbo:activeYearsEndYear ?to.\r\n"
				+ "     ?king dbo:successor ?hasSuccessor.\r\n"
				+ "}";
		
		DataProcessor.QueryToFile(queryString, King.TARGETFILE, new King());
		
	}
}
