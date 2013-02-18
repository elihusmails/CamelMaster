package mww.camel.master.twitter.stalker;

import org.apache.camel.builder.RouteBuilder;

public class TwitterStalkerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		String consumerKey = "6kzfKAkmRRcyDlIanbh4Q";
		String consumerKeySecret = "S8MTY7VvD0VIMnW4IphOyfjwDkuLazLXy7PZJkruJrM";
		String accessToken = "798604-y6HDhj4VLlWfhhN0bHHX9mZ1P0SxhhOLf6dnsNrhdo";
		String accessTokenSecret = "vbe0V4DfgyxRZ7NwiXFTew16BzpIfDg84Xl4sKRFcQ";
		String keywords = "utica";
		
		from("twitter://streaming/filter?type=polling&delay=2&keywords=" + keywords +  
				"&consumerKey=" + consumerKey + 
				"&consumerSecret=" + consumerKeySecret + 
				"&accessToken=" + accessToken +
				"&accessTokenSecret=" + accessTokenSecret)
				.process( new TwitterStalkerProcessor());
				
						
	}
}
