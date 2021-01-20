// camel-k: language=java property-file=application.properties
// camel-k: dependency=github:keunlee:camel-k-starter-workbench

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloToKnative extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(HelloToKnative.class);

    public void configure() throws Exception {
        from("timer:refresh?period=5000&fixedRate=true")
        .setBody()
        .simple("Hello World ${header.firedTime}")
        .log("${body}")
        .to("knative:channel/messages");
    }
}
