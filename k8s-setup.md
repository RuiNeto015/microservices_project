# Setup k8s env

Before executing the continuous deployment pipeline all of the following steps must be performed.

#### Launch minikube (click [here](https://minikube.sigs.k8s.io/docs/start/) if you don't have minikube)
```bat 
> minikube start --kubernetes-version=v1.28.3
```

#### Deploy metrics server and rabbitMQ
```bat
> cd k8s
> kubectl apply -f ./metrics-server.yml
> kubectl apply -f ./rabbitmq.yml
```

#### Deploy backup service
```bat
> cd backup-service
> mvn install
> mvn compile
> mvn package
> docker build -t backup-msvc:1.0 .
> docker build -t postgres:backup -f .\DockerfilePostgresql .
> minikube image load backup-msvc:1.0
> minikube image load postgres:backup
> kubectl apply -f ./k8s/deployment.yml 
```

#### Deploy products service
```bat
> cd products-service
> mvn install
> mvn compile
> mvn package
> docker build -t products-msvc:1.0 .
> docker build -t postgres:products -f .\DockerfilePostgresql .
> minikube image load products-msvc:1.0
> minikube image load postgres:products
> kubectl apply -f ./k8s/deployment.yml 
```

Perform the same steps for each MS... ;)

#### Open a tunnel for each MS

```
> minikube service lb-products-msvc
> minikube service lb-votes-msvc
...
```

#### Pipeline trigger payload example

Don't forget to:
- launch jenkins
- create a new pipeline project
- create a repo ssh key on jenkins with the id 'arqsoft_ssh_key'
- setup generic webhook trigger


`http://user:api-key@host:port/generic-webhook-trigger/invoke`


```json
{
    "branch": "dev", // checkout branch
    "service": "products-service" // MS to perform the deployment
}
```
