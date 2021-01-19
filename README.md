# camel-k-starter-workbench

## Modeline Github Dependencies

Github Dependencies in Modeline have the following pattern: 

1)

```java
// camel-k: dependency=github:account:repository-name
```

2) The modeline dependency assumes that the branch is "master". If you do not have a "master" branch, the modeline dependency will not work. To get around this issue, create a "master" branch. See `HelloToKafka.java` as an example. 