package mww.camel.master.twitter.stalker;

import java.util.Properties;

import org.apache.camel.builder.RouteBuilder;

public class TwitterStalkerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		Properties properties = new Properties();
		properties.load( this.getClass().getClassLoader().getResourceAsStream("twitter.key.props"));
		
		
		from("twitter://streaming/filter?type=polling&delay=2&keywords=" + properties.getProperty("keyword") +  
				"&consumerKey=" + properties.getProperty("consumerKey") + 
				"&consumerSecret=" + properties.getProperty("consumerKeySecret") + 
				"&accessToken=" + properties.getProperty("accessToken") +
				"&accessTokenSecret=" + properties.getProperty("accessTokenSecret"))
				.process( new TwitterStalkerProcessor());
				
						
	}
}
