apiVersion: v1
kind: Service
metadata:
  name: mc-ui-service
  labels:
    app: mc-ui
spec:
  selector:
    app: mc-ui
  type: NodePort
  ports:
  - name: http-3000
    nodePort: 32031
    port: 3000
    protocol: TCP
    targetPort: 3000

---

apiVersion: apps/v1
kind: Deployment
metadata:
  name: mc-ui
  labels:
    app: mc-ui
spec:
  selector:
    matchLabels:
      app: mc-ui
  replicas: 1
  template:
    metadata:
      labels:
        app: mc-ui
    spec:
      containers:
        - name: mc-ui-service
          image: 192.168.1.163:32037/docker.io/mc/mc-ui:version
          ports:
          - containerPort: 3000
            name: service-port

---

apiVersion: v1
kind: Endpoints
metadata:
  name: mc-ui
  labels:
    app: mc-ui
subsets:
  - addresses:
      - ip: 10.244.1.182
    ports:
      - name: http-3000
        port: 3000
