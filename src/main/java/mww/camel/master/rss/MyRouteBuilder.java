
package mww.camel.master.rss;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.Main;

/**
 * A Camel Router
 *
 * @version $
 */
public class MyRouteBuilder extends RouteBuilder {

    /**
     * A main() so we can easily run these routing rules in our IDE
     * @throws Exception 
     */
    public static void main(String... args) throws Exception {
        Main.main(args);
    }

    /**
     * Lets configure the Camel routing rules using Java code...
     */
    public void configure() {

    	from("rss:http://www.reddit.com/r/pics/.rss?splitEntries=true&filter=true&consumer.delay=300000").marshal().rss()
    		.setBody(header("camelrssfeed")).process(new RssProcessor()).to("mock:deleteme");
    }
}
