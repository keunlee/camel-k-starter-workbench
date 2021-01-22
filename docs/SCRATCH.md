## Installing the Camel K Operator - CLI

### Openshift

```kamel install```

### Local Kubernetes Clusteer (i.e. Kind/K3D) using Docker Container Image Repository

```bash
# login to docker
docker login docker.io

# create a secret
kubectl create secret generic container-registry-secret --from-file ~/.docker/config.json
```

```bash
kamel install --maven-repository https://maven.repository.redhat.com/ga --maven-repository  https://jitpack.io --registry docker.io --organization <docker.io.username> --registry-secret container-registry-secret
 ```