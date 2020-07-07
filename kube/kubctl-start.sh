#!/bin/bash

echo "Setting Up Message Gateway service configuration..."
kubectl create secret generic message-db-secret --from-literal=username=root --from-literal=password=$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 16)
kubectl apply -f messagemysql-configmap.yml

echo
echo "Starting messagemysql..."
kubectl apply -f messagemysql-deployment.yml

messagemysql_pod=""
while [[ ${#messagemysql_pod} -eq 0 ]]; do
    messagemysql_pod=$(kubectl get pods -l tier=messagemysql --template '{{range .items}}{{.metadata.name}}{{"\n"}}{{end}}')
done

messagemysql_status=$(kubectl get pods ${messagemysql_pod} --no-headers -o custom-columns=":status.phase")
while [[ ${messagemysql_status} -ne 'Running' ]]; do
    sleep 1
    messagemysql_status=$(kubectl get pods ${messagemysql_pod} --no-headers -o custom-columns=":status.phase")
done

echo
echo "Starting messagegateway server..."
kubectl apply -f message-gateway-deployment.yml

message_server_pod=""
while [[ ${#message_server_pod} -eq 0 ]]; do
    message_server_pod=$(kubectl get pods -l tier=backend --template '{{range .items}}{{.metadata.name}}{{"\n"}}{{end}}')
done

message_server_status=$(kubectl get pods ${message_server_pod} --no-headers -o custom-columns=":status.phase")
while [[ ${message_server_status} -ne 'Running' ]]; do
    sleep 1
    message_server_status=$(kubectl get pods ${message_server_pod} --no-headers -o custom-columns=":status.phase")
done

echo "Message Gateway server is up and running"
