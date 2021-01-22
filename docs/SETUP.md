## 3. Deploying the Project

Now that the infrastructure is ready, we can go ahead and deploy the demo project. First, lets switch to the main project:

```oc project camel-k-event-streaming```

We should now check that the operator is installed. To do so, execute the following command on a terminal:

```
oc get csv
```

When Camel K is installed, you should find an entry related to `red-hat-camel-k-operator` in phase `Succeeded`.

### Initial Configuration

Most of the components of the demo use use the `./application.properties` to read the configurations they need to run. This file already comes with expected defaults, so no action should be needed.

### Development Deployment versus Production Deployment

For all of the examples, you can run them in either development or production mode. 

To run in dev mode: 

```kamel run YourCamelRouter.java --dev```

When running in dev mode, you wil be given a stream of the deployments logs until you hit `ctrl-c` to stop deployment instance.

To run in production mode: 

```kamel run YourCamelRouter.java```

When running production mode, you will not be given a stream of the deployments logs. To view and tail those logs you can observe the logs using the `kubectl logs` command. For example:

```kubectl logs --follow your-camel-router-pod```

#### Optional: Configuration Adjustments

*Note*: you can skip this step if you don't want to adjust the configuration

In case you need to adjust the configuration, the following 2 commands present information that will be required to configure the deployment:

```oc get services -n event-streaming-messaging-broker```

```oc get services -n event-streaming-kafka-cluster```

They provide the addresses of the services running on the cluster and can be used to fill in the values on the properties file.

We start by opening the file `./application.properties` and editing the parameters. The content needs to be adjusted to point to the correct addresses of the brokers. It should be similar to this:

```
kafka.bootstrap.address=event-streaming-kafka-cluster-kafka-bootstrap.event-streaming-kafka-cluster:9092
messaging.broker.url=tcp://broker-hdls-svc.event-streaming-messaging-broker:61616
```

#### Creating the Secret

One of the components simulates receiving data from users and, in order to do so, authenticate the users. Because we normally don't want the credentials to be easily
accessible, it simulates checking the access control by reading a secret.

We can push the secret to the cluster using the following command:

```oc create secret generic example-event-streaming-user-reporting --from-file application.properties```

With this configuration secret created on the cluster, we have completed the initial steps to get the demo running.

### Say Hello to AMQ

To publish to AMQ: 

```kamel run HelloToAmq.java --dev```

To recieve from AMQ: 

```kamel run HelloFromAmq.java --dev```

### Say Hello to AMQP

To publish to an AMQP topic: 

```kamel run HelloToAmqp.java --dev```

To recieve from AMQP topic: 

```kamel run HelloFromAmqp.java --dev```

### Say Hello to Kafka

To publish to a Kafka topic: 

```kamel run HelloToKafka.java --dev```

To recieve from a Kafka topic: 

```kamel run HelloFromKafka.java --dev```

### Say Hello to Knative

To publish to a Knative channel: 

```kamel run HelloToKnative.java --dev```

To recieve from a Knative channel: 

```kamel run HelloFromKnative.java --dev```

## 4. Uninstall

To cleanup everything, execute the following command:

```oc delete project camel-k-event-streaming event-streaming-messaging-broker event-streaming-kafka-cluster```