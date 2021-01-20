// camel-k: language=java property-file=application.properties
// camel-k: dependency=github:keunlee:camel-k-starter-workbench

import org.apache.camel.BindToRegistry;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.qpid.jms.JmsConnectionFactory;

public class HelloFromAmqp extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(HelloFromAmqp.class);

    @PropertyInject("messaging.broker.url.amqp")
    String messagingBrokerUrl;

    @BindToRegistry
    public JmsConnectionFactory connectionFactory() throws Exception {
        return new JmsConnectionFactory(messagingBrokerUrl);
    }

    @Override
    public void configure() throws Exception {
        from("amqp:topic:example?exchangePattern=InOnly&connectionFactory=connectionFactory")
        .log("${body}");
    }
}
