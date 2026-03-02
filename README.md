# Employee Management System

Hệ thống quản lý nhân viên xây dựng bằng **Spring Boot 4**, hỗ trợ giao diện web (Thymeleaf) và REST API với xác thực JWT.

<img width="1863" height="519" alt="image" src="https://github.com/user-attachments/assets/a9c4cd32-b924-4fbc-b836-c7e2a5f2d010" />

---

## 📋 Mô tả hệ thống

### Tổng quan

Ứng dụng quản lý nhân viên và phòng ban trong một tổ chức, cung cấp hai lớp giao diện:

- **Web UI** — giao diện Thymeleaf dành cho người dùng cuối (form login / session)
- **REST API** — endpoint `/api/**` dành cho tích hợp bên ngoài (xác thực JWT)

### Công nghệ sử dụng

| Thành phần | Công nghệ |
|---|---|
| Framework | Spring Boot 4.0.2 |
| Ngôn ngữ | Java 21 |
| Cơ sở dữ liệu | PostgreSQL 15 |
| ORM | Spring Data JPA / Hibernate |
| Giao diện | Thymeleaf + Spring Security Extras |
| Xác thực | Spring Security, JWT (JJWT 0.12.6) |
| Migration DB | Flyway |
| Cache | Caffeine (in-memory) |
| Mapping | ModelMapper |
| Monitoring | Spring Boot Actuator |
| Tiện ích | Lombok, Bean Validation |
| Build | Maven |

### Cấu trúc module

```
src/main/java/com/example/employee_management/
├── config/       # SecurityConfig, CacheConfig, JwtAuthFilter, JwtUtil, AppConfig
├── controller/   # Web controllers (Thymeleaf): Auth, Department, Employee
│   └── api/      # REST API controllers: Auth, Department, Employee, Statistics
├── dto/          # Data Transfer Objects
├── entity/       # JPA Entities: Employee, Department, User, Role
├── exception/    # Global exception handling
├── repository/   # Spring Data JPA repositories
├── scheduler/    # Scheduled tasks (SystemHealthScheduler)
└── service/      # Business logic
```

### Các tính năng chính

- **Quản lý Phòng ban**: thêm, sửa, xoá, liệt kê phòng ban
- **Quản lý Nhân viên**: thêm, sửa, xoá, tìm kiếm, xem thống kê theo phòng ban
- **Xác thực & Phân quyền**:
  - Hai vai trò: `ROLE_ADMIN` và `ROLE_USER`
  - Web: đăng nhập qua form, duy trì session
  - API: đăng nhập lấy JWT token, gửi kèm header `Authorization: Bearer <token>`
- **Cache**: dữ liệu phòng ban và nhân viên được cache bằng Caffeine
- **Database Migration**: Flyway tự động chạy migration khi ứng dụng khởi động
- **Scheduled Task**: log trạng thái hệ thống mỗi 30 giây
- **Actuator**: kiểm tra health, metrics, cache tại `/actuator`

### Phân quyền API

| Endpoint | ROLE_USER | ROLE_ADMIN |
|---|:---:|:---:|
| `POST /api/auth/login` | ✅ | ✅ |
| `POST /api/auth/register` | ✅ | ✅ |
| `GET /api/employees/**` | ✅ | ✅ |
| `GET /api/departments/**` | ✅ | ✅ |
| `GET /api/employees/statistics/**` | ❌ | ✅ |
| `POST / PUT / DELETE` (mọi endpoint) | ❌ | ✅ |

### Schema cơ sở dữ liệu

```
departments  (id, name, description)
employees    (id, employee_code, name, email, department_id)
users        (id, username, password, role)
```

---

## 🚀 Hướng dẫn chạy hệ thống

### Yêu cầu

- **Java 21+**
- **Maven 3.9+** (hoặc dùng `./mvnw` đi kèm)
- **Docker & Docker Compose** (để chạy PostgreSQL)

---

### 🛠️ Môi trường DEV (Development)

Chạy trên máy local với PostgreSQL qua Docker Compose, cổng **5000**.

#### Bước 1 — Tạo file `.env`

Tạo file `.env` tại thư mục gốc dự án:

```dotenv
POSTGRES_DB=employee_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
DB_PORT=5432
```

#### Bước 2 — Khởi động PostgreSQL

```bash
docker compose up -d
```

#### Bước 3 — Chạy ứng dụng

```bash
./mvnw spring-boot:run
```

> Profile `dev` đã được đặt mặc định trong `application.properties`.
> Flyway sẽ tự động tạo bảng khi ứng dụng khởi động lần đầu.

#### Truy cập (DEV)

| Giao diện | URL |
|---|---|
| Web UI / Đăng nhập | http://localhost:5000/login |
| REST API | http://localhost:5000/api |
| Actuator | http://localhost:5000/actuator |

#### Tắt môi trường DEV

```bash
docker compose down
```

---

### 🏭 Môi trường PROD (Production)

Chạy trên server với PostgreSQL riêng, cổng **8080**.

#### Bước 1 — Set biến môi trường

Các biến sau **bắt buộc** phải được cấu hình trên server (không có giá trị mặc định):

```bash
export SPRING_PROFILES_ACTIVE=prod

export DB_HOST=<địa_chỉ_db_server>
export DB_PORT=5432
export POSTGRES_DB=<tên_database>
export POSTGRES_USER=<db_user>
export POSTGRES_PASSWORD=<mật_khẩu_db>

# Bắt buộc thay bằng secret key mạnh (tối thiểu 32 ký tự)
export JWT_SECRET=<secret_key_ngẫu_nhiên_dài>
# Thời gian hết hạn token (ms), mặc định 24 giờ
export JWT_EXPIRATION_MS=86400000
```

#### Bước 2 — Build JAR

```bash
./mvnw clean package -DskipTests
```

File JAR được tạo tại: `target/employee-management-0.0.1-SNAPSHOT.jar`

#### Bước 3 — Chạy ứng dụng

```bash
java -jar target/employee-management-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod
```

#### Truy cập (PROD)

| Giao diện | URL |
|---|---|
| Web UI / Đăng nhập | http://\<server\>:8080/login |
| REST API | http://\<server\>:8080/api |
| Actuator | http://\<server\>:8080/actuator |

#### Lưu ý Production

- Log ghi ra file `logs/employee-management.log`, tự động xoay vòng khi đạt 10 MB, giữ tối đa 30 file.
- SQL query không in ra log (`show-sql=false`).
- Connection pool HikariCP tối đa 20 kết nối.
- Đảm bảo PostgreSQL đã tạo sẵn database trước khi chạy; Flyway sẽ tự chạy migration.

---

## 🔑 Sử dụng REST API

### Đăng nhập lấy JWT token

```bash
curl -X POST http://localhost:5000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "password"}'
```

### Gọi API với JWT token

```bash
curl http://localhost:5000/api/employees \
  -H "Authorization: Bearer <token>"
```
