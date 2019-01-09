package edu.uiowa.slis.DocumentContentExtractor;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.microsoft.OfficeParser;
import org.apache.tika.parser.microsoft.OfficeParserConfig;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;

// ContentExtractor class
public class DocumentContentExtractor {
	private static Map<String, String> metadataMap; // Used to store metadata fields from files
	boolean returnMeta;	// specifies whether metadata or content is extracted
	private String urlString;	// specifies source url to extract content from, could be local file or web(http://)
	
	public DocumentContentExtractor(String urlString, boolean returnMeta) {
		this.urlString  = urlString;
		this.returnMeta = returnMeta;
	}

	// To return metadata fields
	private Map<String, String> getAvailableMetaDataFields(final Metadata metadata) {
		metadataMap = new HashMap<>();
	    for(int i = 0; i <metadata.names().length; i++) {
	        String name = metadata.names()[i];
	        System.out.println(name + " : " + metadata.get(name));
	        metadataMap.put(name, metadata.get(name));
	    }
	    return metadataMap;
	}
	
	// Print content or metadata based on returnMeta value from urlString
	void printContent(){
		
		try{
			InputStream in = null;
			
			// differentiates input stream based upon source is local or web
			if(urlString.startsWith("https://")){
				URL url = new URL(urlString);  
				in = url.openStream();
			}else{
				in = new FileInputStream(new File(urlString));
			}
			BodyContentHandler handler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			ParseContext pcontext = new ParseContext();
			String fileType = urlString.substring(urlString.lastIndexOf(".")+1, urlString.length());
			
			// differentiates parser for pdfs and office files
			if(fileType.equals("pdf")){
				PDFParserConfig config = new PDFParserConfig();
				config.setSortByPosition(true);
				PDFParser pdfparser = new PDFParser();
				pdfparser.setPDFParserConfig(config);
				System.out.println("Parsing PDF to TEXT...");
				pdfparser.parse(in, handler, metadata, pcontext);
			}else if(fileType.contains("ppt") || fileType.contains("doc") || fileType.contains("xls")){
				OfficeParserConfig config = new OfficeParserConfig();
				
				// further differentiates for office versions before 2007 and after 2007
				if(fileType.length() == 4){
					OOXMLParser  officeparser = new OOXMLParser ();	// 2007 and after office files
					officeparser.parse(in, handler, metadata, pcontext);
				}else{
					OfficeParser officeparser = new OfficeParser(); // office files before 2007
					officeparser.parse(in, handler, metadata, pcontext);
				}
				System.out.println("Parsing Office files to TEXT...");
				
			}
			String resultText = "";
			
			// prints metadata or content
			if(returnMeta){
				resultText = metadata.toString();
				metadataMap = getAvailableMetaDataFields(metadata);
				for(Entry<String, String> entry:metadataMap.entrySet()){
					System.out.println(entry.getKey() + " : " + entry.getValue());
				}
			}else{
				resultText = handler.toString();
				System.out.print(resultText);
			}
			 
			
			
			System.out.println("Parsing complete");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

}
