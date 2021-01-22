### Development Deployment versus Production Deployment

For all of the examples, you can run them in either development or production mode. 

To run in dev mode: 

```kamel run YourCamelRouter.java --dev```

When running in dev mode, you wil be given a stream of the deployments logs until you hit `ctrl-c` to stop deployment instance.

To run in production mode: 

```kamel run YourCamelRouter.java```

When running production mode, you will not be given a stream of the deployments logs. To view and tail those logs you can observe the logs using the `kubectl logs` command. For example:

```kubectl logs --follow your-camel-router-pod```

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