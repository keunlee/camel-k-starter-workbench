// camel-k: language=java property-file=application.properties

import org.apache.camel.BindToRegistry;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.src.main.java.com.redhat.integration.common.TestClass001;

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
        TestClass001 t = new TestClass001();
        t.setValue("setting values");

        from("amqp:topic:example?exchangePattern=InOnly&connectionFactory=connectionFactory")
        .log("${body}");
    }
}
