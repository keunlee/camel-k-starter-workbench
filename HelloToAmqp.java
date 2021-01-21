// camel-k: language=java property-file=application.properties

import org.apache.camel.BindToRegistry;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.qpid.jms.JmsConnectionFactory;

public class HelloToAmqp extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(HelloToAmqp.class);

    @PropertyInject("messaging.broker.url.amqp")
    String messagingBrokerUrl;

    @BindToRegistry
    public JmsConnectionFactory connectionFactory() throws Exception {
        return new JmsConnectionFactory(messagingBrokerUrl);
    }

    @Override
    public void configure() throws Exception {
        from("timer:refresh?period=5000&fixedRate=true")
        .setBody()
        .simple("Hello World ${header.firedTime}")
        .log("${body}")
        .to("amqp:topic:example?exchangePattern=InOnly&connectionFactory=connectionFactory");
    }
}
