#!/bin/bash
set -e

echo "Deploying Student Management System to Kubernetes..."

kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml

echo "Deployment complete."
echo "Access the app at: http://$(minikube ip):30080"
