// camel-k: language=java property-file=application.properties
// camel-k: dependency=camel-amqp
// camel-k: dependency=github:keunlee:camel-k-starter-workbench

import org.apache.camel.BindToRegistry;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.ConnectionFactory;
import org.apache.qpid.jms.JmsConnectionFactory;

public class HelloToAmqp extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(HelloToAmqp.class);

    @PropertyInject("messaging.broker.url.amqp")
    String messagingBrokerUrl;

    @BindToRegistry
    public ConnectionFactory connectionFactory() {
        return new JmsConnectionFactory(messagingBrokerUrl);
    }

    @Override
    public void configure() throws Exception {
        from("timer:refresh?period=5000&fixedRate=true")
        .setBody()
        .simple("Hello World ${header.firedTime}")
        .log("${body}")
        .to("amqp:topic:example?exchangePattern=InOnly");
    }
}
