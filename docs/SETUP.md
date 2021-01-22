# Preparing the Cluster

These examples can be run on any OpenShift 4.3+ cluster or a local development instance (such as [CRC](https://github.com/code-ready/crc)). Ensure that you have a cluster available and login to it using the OpenShift `oc` command line tool.

You need to create a new project named `camel-k-event-streaming` for running this example. This can be done directly from the OpenShift web console or by executing the command `oc new-project camel-k-event-streaming` on a terminal window.

## Installing the Camel K Operator - Openshift - Operator Hub

You need to install the Camel K operator in the `camel-k-event-streaming` project. To do so, go to the OpenShift 4.x web console, login with a cluster admin account and use the OperatorHub menu item on the left to find and install **"Red Hat Integration - Camel K"**. You will be given the option to install it globally on the cluster or on a specific namespace.
If using a specific namespace, make sure you select the `camel-k-event-streaming` project from the dropdown list.
This completes the installation of the Camel K operator (it may take a couple of minutes).

When the operator is installed, from the OpenShift Help menu ("?") at the top of the WebConsole, you can access the "Command Line Tools" page, where you can download the **"kamel"** CLI, that is required for running this example. The CLI must be installed in your system path.

Refer to the **"Red Hat Integration - Camel K"** documentation for a more detailed explanation of the installation steps for the operator and the CLI.

## Installing the AMQ Streams Operator

This example uses AMQ Streams, Red Hat's data streaming platform based on Apache Kafka.
We want to install it on a new project named `event-streaming-kafka-cluster`. 

You need to create the `event-streaming-kafka-cluster` project from the OpenShift web console or by executing the command `oc new-project event-streaming-kafka-cluster` on a terminal window. 

Now, we can go to the OpenShift 4.x WebConsole page, use the OperatorHub menu item on the left hand side menu and use it to find and install **"Red Hat Integration - AMQ Streams"**.
This will install the operator and may take a couple minutes to install.

## Installing the AMQ Broker Operator

The installation of the AMQ Broker follows the same isolation pattern as the AMQ Streams one. We will deploy it in a separate project and will
instruct the operator to deploy a broker according to the configuration.

You need to create the `event-streaming-messaging-broker` project from the OpenShift web console or by executing the command `oc new-project event-streaming-messaging-broker` on a terminal window. 

Now, we can go to the OpenShift 4.x WebConsole page, use the OperatorHub menu item on the left hand side menu and use it to find and install **"Red Hat Integration - AMQ Broker"**.
This will install the operator and may take a couple minutes to install.

## Installing OpenShift Serverless

This demo also needs OpenShift Serverless (Knative) installed and working.

Go to the OpenShift 4.x WebConsole page, use the OperatorHub menu item on the left hand side then find and install **"OpenShift Serverless"** 
from a channel that best matches your OpenShift version.

The operator installation page reports links to the documentation where you can find information about **additional steps** that must
be done in order to have OpenShift serverless completely installed into your cluster.

Make sure you follow all the steps in the documentation before continuing to the next section.

# Requirements

**OpenShift CLI ("oc")**

The OpenShift CLI tool ("oc") will be used to interact with the OpenShift cluster.

**Connection to an OpenShift cluster**

In order to execute this demo, you will need to have an OpenShift cluster with the correct access level, the ability to create projects and install operators as well as the Apache Camel K CLI installed on your local system.

**Apache Camel K CLI ("kamel")**

Apart from the support provided by the VS Code extension, you also need the Apache Camel K CLI ("kamel") in order to
access all Camel K features.

**Knative installed on the OpenShift cluster**

The cluster also needs to have Knative installed and working. Refer to steps above for information on how to install it in your cluster.

## Optional Requirements

The following requirements are optional. They don't prevent the execution of the demo, but may make it easier to follow.

**VS Code Extension Pack for Apache Camel**

The VS Code Extension Pack for Apache Camel by Red Hat provides a collection of useful tools for Apache Camel K developers,
such as code completion and integrated lifecycle management. They are **recommended** for the tutorial, but they are **not**
required.

You can install it from the VS Code Extensions marketplace.

# 1. Creating the AMQ Streams Cluster

We switch to the `event-streaming-kafka-cluster` project to create the Kafka cluster:

```oc project event-streaming-kafka-cluster```

The next step is to use the operator to create an AMQ Streams cluster. This can be done with the command:

```oc create -f infra/kafka/kafka-cluster.yaml```

Depending on how large your OpenShift cluster is, this may take a little while to complete. Let's run this command and wait until the cluster is up and running.

```oc wait kafka/event-streaming-kafka-cluster --for=condition=Ready --timeout=600s```

You can can check the state of the cluster by running the following command:

```oc get kafkas -n event-streaming-kafka-cluster event-streaming-kafka-cluster```

Once the AMQ Streams cluster is created. We can proceed to the creation of the AMQ Streams topics:

```oc apply -f infra/kafka/clusters/topics/```

```oc get kafkatopics```

At this point, if all goes well, we should see our AMQ Streams cluster up and running with several topics.

# 2. Creating the AMQ Broker Cluster

To switch to the `event-streaming-messaging-broker` project, run the following command:

```oc project event-streaming-messaging-broker```

Having already the operator installed and running on the project, we can proceed to create the broker instance:

```oc create -f infra/messaging/amq-broker-instance.yaml```

We can use the `oc get activemqartermis` command to check if the AMQ Broker instance is created:

```oc get activemqartemises```

If it was successfully created, then we can create the addresses and queues required for the demo to run:

```oc apply -f infra/messaging/addresses```

# 3. Deploying the Project

Now that the infrastructure is ready, we can go ahead and deploy the demo project. First, lets switch to the main project:

```oc project camel-k-event-streaming```

We should now check that the operator is installed. To do so, execute the following command on a terminal:

```
oc get csv
```

When Camel K is installed, you should find an entry related to `red-hat-camel-k-operator` in phase `Succeeded`.

## Initial Configuration

Most of the components of the demo use use the `./application.properties` to read the configurations they need to run. This file already comes with expected defaults, so no action should be needed.

### Optional: Configuration Adjustments

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

### Creating the Secret

One of the components simulates receiving data from users and, in order to do so, authenticate the users. Because we normally don't want the credentials to be easily
accessible, it simulates checking the access control by reading a secret.

We can push the secret to the cluster using the following command:

```oc create secret generic example-event-streaming-user-reporting --from-file application.properties```

With this configuration secret created on the cluster, we have completed the initial steps to get the demo running.

# 4. Uninstall

To cleanup everything, execute the following command:

```oc delete project camel-k-event-streaming event-streaming-messaging-broker event-streaming-kafka-cluster```