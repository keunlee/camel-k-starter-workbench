// camel-k: language=java property-file=application.properties
// camel-k: dependency=github:keunlee:camel-k-starter-workbench

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloFromKnative extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(HelloFromKnative.class);

    public void configure() throws Exception {
        from("knative:channel/messages")
        .log("${body}");
    }
}
