#!/bin/bash

kubectl delete secret message-db-secret
kubectl delete -f messagemysql-configmap.yml
kubectl delete -f messagemysql-deployment.yml
kubectl delete -f message-gateway-deployment.yml
