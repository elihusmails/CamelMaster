package mww.camel.master.rss;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeedImpl;

public class RssProcessor implements Processor {

	private Logger log = LoggerFactory.getLogger(RssProcessor.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {

		Message msg = exchange.getIn();
		SyndFeedImpl feed = (SyndFeedImpl)msg.getBody();
		
		log.info( feed.getDescription() );
		
		List<SyndEntryImpl> entries = feed.getEntries();
		for( SyndEntryImpl entry : entries ){

			SyndContent content = entry.getDescription();
			
			try{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true); // never forget this!
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(new ByteArrayInputStream(content.getValue().getBytes()));
				
				XPathFactory xpathFactory = XPathFactory.newInstance();
				XPath xpath = xpathFactory.newXPath();
				XPathExpression expr = xpath.compile("//a/@href[contains(.,'imgur')]");
				Object result = expr.evaluate(doc, XPathConstants.NODESET);

				NodeList nodes = (NodeList) result;
				for (int i = 0; i < nodes.getLength(); i++) {
					log.info( "NODE == " + nodes.item(i).getNodeValue()); 
					
					URL url = new URL(nodes.item(i).getNodeValue());
					File out = new File("/Volumes/TERABYTE/data/" + url.getFile());
					log.info("writing data to " + out.getAbsolutePath());
					
					URLConnection conn = url.openConnection();
					InputStream in = conn.getInputStream();
					byte[] buffer = new byte[8192];
					int read = 0;
					FileOutputStream fos = new FileOutputStream(out);
					while( (read = in.read(buffer)) > 0 ){
						
						fos.write(buffer, 0, read);
					}
					
					fos.flush();
					fos.close();
					
					in.close();
				}
				
			}catch(IOException ex){
				log.error("I/O error " + ex.getMessage());
			}catch(SAXParseException sax){
				log.error("Error processing HTML -> " + sax.getMessage());
			}
		}
	}

}
