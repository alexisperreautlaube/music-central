apiVersion: v1
kind: Service
metadata:
  name: mc-server-service
  labels:
    app: mc-server
spec:
  selector:
    app: mc-server
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
  name: mc-server
  labels:
    app: mc-server
spec:
  selector:
    matchLabels:
      app: mc-server
  replicas: 1
  template:
    metadata:
      labels:
        app: mc-server
    spec:
      containers:
        - name: mc-server-service
          image: 192.168.1.163:32037/docker.io/mc/mc-server:version
          ports:
          - containerPort: 8080
            name: service-port

---

apiVersion: v1
kind: Endpoints
metadata:
  name: mc-server
  labels:
    app: mc-server
subsets:
  - addresses:
      - ip: 10.244.1.172
    ports:
      - name: http-8081
        port: 8080
