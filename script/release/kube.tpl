apiVersion: v1
kind: Service
metadata:
  name: mc-msg-consumer-service
  labels:
    app: mc-msg-consumer
spec:
  selector:
    app: mc-msg-consumer
  type: NodePort
  ports:
  - name: http-8080
    nodePort: 32030
    port: 8080
    protocol: TCP
    targetPort: 8080

---

apiVersion: apps/v1
kind: Deployment
metadata:
  name: mc-msg-consumer
  labels:
    app: mc-msg-consumer
spec:
  selector:
    matchLabels:
      app: mc-msg-consumer
  replicas: 1
  template:
    metadata:
      labels:
        app: mc-msg-consumer
    spec:
      containers:
        - name: mc-msg-consumer-service
          image: 192.168.1.163:32037/docker.io/mc/mc-msg-consumer:version
          ports:
          - containerPort: 8080
            name: service-port

---

apiVersion: v1
kind: Endpoints
metadata:
  name: mc-msg-consumer
  labels:
    app: mc-msg-consumer
subsets:
  - addresses:
      - ip: 10.244.1.172
    ports:
      - name: http-8081
        port: 8080
