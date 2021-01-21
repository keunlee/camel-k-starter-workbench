// camel-k: language=java property-file=application.properties

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloFromKafka extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(HelloFromKafka.class);

    public void configure() throws Exception {
        from("kafka:sample-data?brokers={{kafka.bootstrap.address}}")
        .log("${body}");
    }
}
