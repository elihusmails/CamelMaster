package mww.camel.master.twitter.stalker;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;

public class TwitterStalkerProcessor implements Processor {

	private static final Logger log = LoggerFactory.getLogger(TwitterStalkerProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {

		Status tweet = exchange.getIn().getBody(Status.class);
		if( tweet.getGeoLocation() != null ){
			log.info( "Tweet[{}] = {}", tweet.getUser().getScreenName(), tweet.getText());
			log.info("\tLatitude {}. Longitude {}", 
					tweet.getGeoLocation().getLatitude(), 
					tweet.getGeoLocation().getLongitude());
			log.info("");
		}
	}
}
