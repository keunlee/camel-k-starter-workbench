# Running the Examples

## Deployment Modes

For all of the examples, you can run them in either development or production mode. 

To run in dev mode: 

```kamel run YourCamelRouter.java --dev```

When running in dev mode, your deployment will persist until you hit `ctrl-c`, which will stop your deployment instance.

To run in production mode: 

```kamel run YourCamelRouter.java```

When running production mode, your route will be deployed to your cluster. 

## Deployment Logs

To view and tail deployment logs you can observe the logs using the `kubectl logs` command. For example:

```kubectl logs --follow your-camel-router-pod```

## Say Hello to AMQ

To publish to AMQ: 

```kamel run HelloToAmq.java --dev```

To recieve from AMQ: 

```kamel run HelloFromAmq.java --dev```

## Say Hello to AMQP

To publish to an AMQP topic: 

```kamel run HelloToAmqp.java --dev```

To recieve from AMQP topic: 

```kamel run HelloFromAmqp.java --dev```

## Say Hello to Kafka

To publish to a Kafka topic: 

```kamel run HelloToKafka.java --dev```

To recieve from a Kafka topic: 

```kamel run HelloFromKafka.java --dev```

## Say Hello to Knative

This service leverages [knative eventing channels](https://knative.dev/docs/eventing/channels/) to operate. Therefore, we need to create
them on the OpenShift cluster. To do so we can execute the following command:

```oc apply -f infra/knative/channels/messages-channel.yaml```

To publish to a Knative channel: 

```kamel run HelloToKnative.java --dev```

To recieve from a Knative channel: 

```kamel run HelloFromKnative.java --dev```

## POJOs with Jitpack and Maven

### Running the Example

To run the example: 

```kamel run HelloJitpackPojos.java --dev```

### Problem

You want to reference POJOs from separate files w/in your Camel Routes. Prior versions of Camel K, support this feature. The newest release of Camel K (1.3.0) does NOT support this feature. So how do you seperate POJOs from your Camel Routes? 

For additional information on this [issue](https://github.com/apache/camel-k/issues/1821).

### Solution

Checkin your POJOs as a maven project into a Git repository (i.e. Github) and reference the Git repository as a maven dependency in your Camel K Route. 

0. Specify `jitpack.io` as a maven repository with your install of the Camel K operator. See [SETUP.md#add-maven-repositories](./SETUP.md#add-maven-repositories)

1. Add a `jitpack.yml` at the root of your repository. For this project, our maven project is located in the `./models` directory as a maven project.

```yaml
jdk:
- openjdk11
install:
- mvn clean install -f ./model/pom.xml
```
2. Add your Git repository to your Camel Route as a maven dependency. To obtain the maven depdency to, goto: https://jitpack.io/ and enter your Git repository's uri to publish it to generate a maven dependency. 

```xml
<!-- example -->
<dependency>
    <groupId>com.github.keunlee</groupId>
    <artifactId>camel-k-starter-workbench</artifactId>
    <version>main-SNAPSHOT</version>
</dependency>
```

3. Annotate your Camel K Route file with the following dependency: 

```java
// camel-k: dependency=github:keunlee/camel-k-starter-workbench:main-SNAPSHOT

public class YourRoute extends RouteBuilder {...}
```

4. Push your latest changes to your Git repository

5. Run your Camel Route: 

```kamel run YourRoute.java --dev```

### Troubleshooting

If your Camel Route is not picking up new changes on your Jitpack dependency, you can refresh your local Camel K Build Kit. To refresh your Routes Build Kit to accept new changes from your Jitpack dependency, run the following to reset the build kit. 

```kamel rest```

and run your route as you normally would. 