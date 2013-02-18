package mww.camel.master.mail.processor;

import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailProcessor implements Processor {

	private static final Logger log = LoggerFactory.getLogger(MailProcessor.class);
	
	public void process(Exchange exchange) throws Exception {

		// the API is a bit clunky so we need to loop
		Message message = exchange.getIn();
		//log.info( message.getHeaders().toString());
		log.info("From:    " + message.getHeader("From"));
		log.info("To:      " + message.getHeader("To"));
		log.info("Subject: " + message.getHeader("Subject"));
		
		String bodyFile = "/Users/elihusmails/tmpmail/" + 
		exchange.getExchangeId() + ".body";
		FileOutputStream bodyStream = new FileOutputStream(bodyFile);
		bodyStream.write( ((String)message.getBody()).getBytes() );
		bodyStream.flush();
		bodyStream.close();
		log.info("Wrote body to " + bodyFile);
		
		
		Set<String> attachmentNames = message.getAttachmentNames();
		for( String attachmentName : attachmentNames ){
			log.info("Attachment name = " + attachmentName);
		}
		
		Map<String, DataHandler> attachments = message.getAttachments();
		if (attachments.size() > 0) {
			for (String name : attachments.keySet()) {
				DataHandler dh = attachments.get(name);
				
				// get the file name
				String filename = "/Users/elihusmails/tmpmail/" + dh.getName();

				// get the content and convert it to byte[]
				//byte[] data = exchange.getContext().getTypeConverter().convertTo(byte[].class, dh.getInputStream());

				// write the data to a file
				FileOutputStream out = new FileOutputStream(filename);
				log.info("Writing attachment out to " + filename);
				byte[] buffer = new byte[1024];
				int count = 0;
				while( (count = dh.getInputStream().read(buffer)) > 0){
				
					out.write(buffer, 0, count);
					out.flush();
				}
				
				out.close();
			}
		}
	}
}
