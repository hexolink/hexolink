docker build -t noxior/authentication-service:latest -t noxior/authentication-service:$SHA -f ./authentication-service/Dockerfile ./authentication-service
docker push noxior/authentication-service:latest
docker push noxior/authentication-service:$SHA
kubectl apply -f k8s
kubectl set image deployment/authentication-service-deployment authentication-service=noxior/authentication-service:$SHA
