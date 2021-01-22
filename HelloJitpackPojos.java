// camel-k: language=java property-file=application.properties
// camel-k: dependency=github:keunlee/camel-k-starter-workbench:main-SNAPSHOT

import java.text.MessageFormat;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.integration.common.TestClass001;
import com.redhat.integration.common.TestClass002;

public class HelloJitpackPojos extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(HelloJitpackPojos.class);

    public void configure() throws Exception {
        TestClass001 t1 = new TestClass001();
        t1.setValue("RED");

        TestClass002 t2 = new TestClass002();
        t2.setValue("BLUE");

        String formattedValue = 
            MessageFormat.format("{0} & {1}", t1.getValue(), t2.getValue()).toString();

        from("timer:refresh?period=5000&fixedRate=true")
        .setBody()
        .simple("Hello " + formattedValue + " ${header.firedTime}")
        .log("${body}");
    }
}
