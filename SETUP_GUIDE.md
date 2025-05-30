# 🐳 Triển Khai Hệ Thống Microservices với Docker Swarm

Hướng dẫn từng bước để build và triển khai hệ thống sử dụng Docker Compose.

---

## 📦 Build Dịch Vụ (đứng ở thư mục gốc source code)

### Product Service
```bash
docker build -t docker-user/product-svc:latest -f product/Dockerfile .
```
```bash
docker push docker-user/product-svc:latest
```

### WebConfig Service
```bash
docker build -t docker-user/web-config-svc:latest -f webconfig/Dockerfile .
```
```bash
docker push docker-user/web-config-svc:latest
```

### Auth Service
```bash
docker build -t docker-user/auth-svc:latest -f auth/Dockerfile .
```
```bash
docker push docker-user/auth-svc:latest
```

---

## ⚙️ Tạo cấu hình các dịch vụ(trên server)

### Cấu trúc thư mục
```bash
.
├── docker-compose.yml
├── .env
├── product/
│   ├── Dockerfile
│   └── .env
├── auth/
│   ├── Dockerfile
│   └── .env
├── webconfig/
│   ├── Dockerfile
│   └── .env
├── mysql_init_scripts/
│   └── create_database.sql
```

### docker-compose.yml
```bash
services:
  redis:
    image: redis:7.4.1
    ports:
      - '6379:6379'
  database:
    image: mysql:8.0
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 5s
      timeout: 20s
      retries: 5
    ports:
      - '3306:3306'
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_USER: ${MYSQL_USER}
      MYSQL_PASSWORD: ${MYSQL_PASSWORD}
      MYSQL_DATABASE: product
    volumes:
      - mysql_data:/var/lib/mysql
      - ./mysql_init_scripts:/docker-entrypoint-initdb.d
  minio:
    image: quay.io/minio/minio
    ports:
      - '9000:9000'
      - '9001:9001'
    environment:
      MINIO_ROOT_USER: admin
      MINIO_ROOT_PASSWORD: 123456a@
    volumes:
      - minio_data:/data
    command: server /data --console-address ":9001"
  product-service:
    restart: unless-stopped
    build:
      context: .
      dockerfile: product/Dockerfile
    env_file:
      - product/.env
    ports:
      - 8081:8080
    depends_on:
      database:
        condition: service_healthy
  webconfig-service:
    restart: unless-stopped
    build:
      context: .
      dockerfile: webconfig/Dockerfile
    env_file:
      - webconfig/.env
    ports:
      - 8082:8080
    depends_on:
      database:
        condition: service_healthy
  auth-service:
    restart: unless-stopped
    build:
      context: .
      dockerfile: auth/Dockerfile
    env_file:
      - auth/.env
    ports:
      - 8083:8080
    depends_on:
      - database
volumes:
  mysql_data:
  minio_data:
```
### .env
```bash
MYSQL_ROOT_PASSWORD=YOUR_ROOT_PASSWORD
MYSQL_USER=YOUR_DB_USER
MYSQL_PASSWORD=YOUR_DB_PASSWORD
MYSQL_DATABASE=default

MINIO_ROOT_USER=YOUR_MINIO_USER
MINIO_ROOT_PASSWORD=YOUR_MINIO_PASS
STORAGE_PUBLIC_HOT=${DOMAIN}/storage
SECRET_KEY=YOUR_SECRET_KEY
```

### product/.env
```bash
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:mysql://database:3306/product
DB_USER=root
DB_PASS=${MYSQL_ROOT_PASSWORD}
SHOW_SQL=true
DB_POOL_SIZE=20
DB_SHOW_SQL=true

REDIS_ENABLE=false
REDIS_NODES=redis:6379

STORAGE_ENABLED=true
STORAGE_HOST=http:minio:9000
STORAGE_USER=${MINIO_ROOT_USER}
STORAGE_PASS=${MINIO_ROOT_PASSWORD}
STORAGE_PUBLIC_HOST=${STORAGE_PUBLIC_HOT}

```

### auth/.env
```bash
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:mysql://database:3306/auth
DB_USER=root
DB_PASS=${MYSQL_ROOT_PASSWORD}
SHOW_SQL=true
DB_POOL_SIZE=10
DB_SHOW_SQL=true

REDIS_ENABLE=false
REDIS_NODES=redis:6379

STORAGE_ENABLED=true
STORAGE_HOST=http:minio:9000
STORAGE_USER=${MINIO_ROOT_USER}
STORAGE_PASS=${MINIO_ROOT_PASSWORD}
STORAGE_PUBLIC_HOST=${STORAGE_PUBLIC_HOT}

INIT_USER=YOUR_ADMIN_USER
INIT_PASS=YOUR_ADMIN_PASSWORD
```

### webconfig/.env
```bash
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:mysql://database:3306/webconfig
DB_USER=root
DB_PASS=${MYSQL_ROOT_PASSWORD}
SHOW_SQL=true
DB_POOL_SIZE=10
DB_SHOW_SQL=true

REDIS_ENABLE=false
REDIS_NODES=redis:6379

STORAGE_ENABLED=true
STORAGE_HOST=http:minio:9000
STORAGE_USER=${MINIO_ROOT_USER}
STORAGE_PASS=${MINIO_ROOT_PASSWORD}
STORAGE_PUBLIC_HOST=${STORAGE_PUBLIC_HOT}
```

### mysql_init_scripts/create_database.sql
```bash
create database if not exists product;
create database if not exists webconfig;
create database if not exists auth;
```
---

## Start các dịch vụ
```bash
docker-compose up -d
```

## 🌐 Cấu Hình Gateway (NGINX)

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

        location ~ ^/storage(.*) {
            proxy_pass http://minio:9000$1$is_args$args;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
        }
        
        location ~ ^/api/auth(.*) {
            proxy_pass http://auth-service:8080/api/v1$1$is_args$args;
            proxy_http_version 1.1;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
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

