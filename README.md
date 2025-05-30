# 🐳 Triển Khai Hệ Thống Microservices với Docker Swarm

Hướng dẫn từng bước để build và triển khai hệ thống sử dụng Docker Swarm.

---

## 📦 Build Dịch Vụ

### Product Service
```bash
docker build -t product-svc:latest -f product/Dockerfile .
```

### WebConfig Service
```bash
docker build -t web-config-svc:latest -f webconfig/Dockerfile .
```

### Auth Service
```bash
docker build -t auth-svc:latest -f auth/Dockerfile .
```

---

## ⚙️ Khởi Tạo Docker Swarm & Network

### Init Docker Swarm
```bash
docker swarm init
```

### Tạo Network Overlay
```bash
docker network create --driver=overlay --attachable globalnet
```

---

## 🌐 Cấu Hình Gateway (NGINX)

### Cấu trúc thư mục NGINX:
```
nginx/
├── html/
│   └── index.html
├── nginx.conf
└── vhost/
```

### Cấu hình `nginx.conf`
```nginx
worker_processes auto;

error_log /var/log/nginx/error.log warn;
pid /var/run/nginx.pid;

events {
    worker_connections 1024;
}

http {
    resolver 127.0.0.11 valid=30s;
    include /etc/nginx/mime.types;
    default_type application/octet-stream;

    access_log /var/log/nginx/access.log;
    sendfile on;
    keepalive_timeout 65;

    include /etc/nginx/vhost/*.conf;

    server {
        listen 80;
        server_name localhost;

        root /usr/share/nginx/html;

        location ~ ^/api/product(.*) {
            proxy_pass http://product-service:8080/api/v1$1$is_args$args;
            proxy_http_version 1.1;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }

        location ~ ^/api/webconfig(.*) {
            proxy_pass http://webconfig-service:8080/api/v1$1$is_args$args;
            proxy_http_version 1.1;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }
        
        location ~ ^/api/auth(.*) {
            proxy_pass http://auth-service:8080/api/v1$1$is_args$args;
            proxy_http_version 1.1;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }

        location ~ ^/storage(.*) {
            proxy_pass http://minio:9000$1$is_args$args;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
        }

        location ~ ^/ui-storage(.*) {
            proxy_pass http://minio:9001$1$is_args$args;
        }

        location / {
            index index.html index.htm;
        }
    }
}
```

### Khởi chạy NGINX Gateway
```bash
docker service create \
  --name gateway \
  --publish 80:80 \
  --network globalnet \
  --mount type=bind,source=$(pwd)/nginx/html,target=/usr/share/nginx/html,readonly \
  --mount type=bind,source=$(pwd)/nginx/nginx.conf,target=/etc/nginx/nginx.conf,readonly \
  --mount type=bind,source=$(pwd)/nginx/vhost,target=/etc/nginx/vhost,readonly \
  nginx:alpine
```

---

## 🗄️ Cài Đặt MySQL

### Tạo thư mục lưu trữ:
```bash
mkdir database
```

### Khởi chạy MySQL:
```bash
docker service create \
  --name mysql \
  --network globalnet \
  --env MYSQL_ROOT_PASSWORD={{YOUR_PASSWORD}} \
  --mount type=bind,source=$(pwd)/database,target=/var/lib/mysql \
  mysql:8.0
```

---

## ☁️ Cài Đặt MinIO

### Tạo thư mục lưu trữ:
```bash
mkdir minio
```

### Khởi chạy MinIO:
```bash
docker service create \
  --name minio \
  --publish 9000:9000 \
  --publish 9001:9001 \
  --network globalnet \
  --env MINIO_ROOT_USER={{YOUR_USER}} \
  --env MINIO_ROOT_PASSWORD={{YOUR_PASSWORD}} \
  --mount type=bind,source=$(pwd)/minio,target=/data \
  quay.io/minio/minio:latest server /data --console-address ":9001"
```

> Truy cập MinIO UI: http://localhost:9001  
> Đăng nhập

### Tạo bucket `public` và cập nhật policy:
```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Principal": {
                "AWS": ["*"]
            },
            "Action": ["s3:GetObject"],
            "Resource": ["arn:aws:s3:::public/*"]
        }
    ]
}
```

---

## 🧩 Khởi Chạy Product Service
```bash
docker service create \
  --name product-service \
  --network globalnet \
  --env DB_URL=jdbc:mysql://mysql:3306/product \
  --env DB_USER=root \
  --env DB_PASS={{YOUR_PASS}} \
  --env DB_POOL_SIZE=50 \
  --env STORAGE.ENABLED=true \
  --env STORAGE.HOST=http://minio:9000 \
  --env STORAGE.PUBLIC_HOST={{SERVER_DOMAIN}}/storage \
  --env STORAGE_USER={{YOUR_USER}} \
  --env STORAGE_PASS={{YOUR_PASS}} \
  product-svc:latest
```

---

## ⚙️ Khởi Chạy WebConfig Service
```bash
docker service create \
  --name webconfig-service \
  --network globalnet \
  --env DB_URL=jdbc:mysql://mysql:3306/webconfig \
  --env DB_USER=root \
  --env DB_PASS={{YOUR_PASS}} \
  --env DB_POOL_SIZE=50 \
  --env DB_SHOW_SQL=false \
  --env REDIS_ENABLE=false \
  --env STORAGE.ENABLED=true \
  --env STORAGE.HOST=http://minio:9000 \
  --env STORAGE.PUBLIC_HOST=http://{{SERVER_DOMAIN}}/storage \
  --env STORAGE_USER={{YOUR_USER}} \
  --env STORAGE_PASS={{YOUR_PASS}} \
  web-config-svc:latest
```

---

## ✅ Hệ Thống Sẵn Sàng!

- Product API: `http://localhost/api/product/...`
- WebConfig API: `http://localhost/api/webconfig/...`
- Truy cập file public MinIO: `http://localhost/storage/public/...`
- Giao diện MinIO: `http://localhost:9001`
