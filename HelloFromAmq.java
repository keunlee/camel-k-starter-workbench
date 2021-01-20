// camel-k: language=java property-file=application.properties
// camel-k: dependency=mvn:org.apache.activemq:artemis-jms-client:2.11.0.redhat-00005 
// camel-k: dependency=github:keunlee:camel-k-starter-workbench

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.sjms2.Sjms2Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloFromAmq extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(HelloFromAmq.class);

    @PropertyInject("messaging.broker.url")
    String messagingBrokerUrl;

    public void configure() throws Exception {
        Sjms2Component sjms2Component = new Sjms2Component();
        sjms2Component.setConnectionFactory(new ActiveMQConnectionFactory(messagingBrokerUrl));
        getContext().addComponent("sjms2", sjms2Component);

        from("sjms2://queue:notifications?ttl={{messaging.ttl.notifications}}")
        .log("${body}");
    }
}
