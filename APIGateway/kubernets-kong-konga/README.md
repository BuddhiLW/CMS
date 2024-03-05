# Setup - Kong(a) with kubernetes

## Requirements:
- minikube (or alternative clusters)
- kubernetes
- docker
- kind (Kubernetes in Docker)

```bash
minikube start
```

``` bash
kubectl apply -f \
  https://github.com/kubernetes-sigs/gateway-api/releases/download/v1.0.0/standard-install.yaml
```

``` bash
kind create cluster -n cms
```

``` bash
kubectl cluster-info --context kind-cms
kubectl config use-context kind-cms
```

We can take some approaches here. Namely, we will choose `Gloo` API Gateway.

Install it, first,
``` bash
curl -sL https://run.solo.io/gloo/install | GLOO_VERSION=v2.0.0-beta1 sh
export PATH=$HOME/.gloo/bin:$PATH
```

``` bash
glooctl install --gateway
glooctl check
kubectl rollout status deployment/gloo-proxy-http -n gloo-system
```

``` bash
kubectl port-forward deployment/gloo-proxy-http -n gloo-system 8080:8080 &
```

``` bash
kubectl apply -f https://raw.githubusercontent.com/solo-io/solo-blog/main/gateway-api-tutorial/05-workload-svcs.yaml
kubectl get pods -n my-workload
kubectl apply -f https://raw.githubusercontent.com/solo-io/solo-blog/main/gateway-api-tutorial/06-workload-route.yaml
```

Finally, hit the endpoint configured:

``` bash
curl -is -H "Host: api.example.com" http://localhost:8080/api/my-workload
```

You should get something of the sort:

``` bash
Handling connection for 8080
HTTP/1.1 200 OK
vary: Origin
date: Tue, 05 Mar 2024 23:35:46 GMT
content-length: 294
content-type: text/plain; charset=utf-8
x-envoy-upstream-service-time: 0
server: envoy

{
  "name": "my-workload-v1",
  "uri": "/api/my-workload",
  "type": "HTTP",
  "ip_addresses": [
    "10.244.0.8"
  ],
  "start_time": "2024-03-05T23:35:46.299228",
  "end_time": "2024-03-05T23:35:46.299292",
  "duration": "63.967µs",
  "body": "Hello From My Workload (v1)!",
  "code": 200
}
```

