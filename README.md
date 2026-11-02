## 🌍 Base URL

```
http://localhost:8080
```

---

## 🔐 Xác thực (Authentication)

Tất cả các endpoint *(trừ `/author/**`)* yêu cầu JWT Token trong header:

```
Authorization: Bearer <your_jwt_token>
```

---

## 👤 1. Authentication API

### 📝 Đăng ký (Register)

**POST** `/author/register-user`

#### Request Body

```json
{
  "email": "user@example.com",
  "password": "password123",
  "fullName": "Nguyen Van A",
  "userType": "STUDENT"
}
```

#### Response

```json
{
  "statusCode": 200,
  "message": "Registration successful!",
  "data": {
    "userId": "uuid",
    "email": "user@example.com",
    "fullName": "Nguyen Van A",
    "userType": "STUDENT"
  }
}
```

---

### 🔑 Đăng nhập (Login)

**POST** `/author/login`

#### Request Body

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

#### Response

```json
{
  "statusCode": 200,
  "data": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

## ✅ 2. Tasks API

### ➕ Tạo Task mới

**POST** `/api/tasks`

```json
{
  "title": "Hoàn thành bài tập Toán",
  "description": "Giải 50 bài tập hình học",
  "dueDate": "2025-07-10T17:00:00",
  "priority": "high",
  "status": "pending"
}
```

### 📋 Lấy tất cả Tasks

**GET** `/api/tasks`

### 🔍 Lấy Task theo ID

**GET** `/api/tasks/{taskId}`

### ✏️ Cập nhật Task

**PUT** `/api/tasks/{taskId}`

### ❌ Xóa Task

**DELETE** `/api/tasks/{taskId}`

### ⚙️ Lấy Tasks theo Status

**GET** `/api/tasks/status/{status}`

> Các status hợp lệ: `pending`, `in_progress`, `completed`

### ⚡ Lấy Tasks theo Priority

**GET** `/api/tasks/priority/{priority}`

> Các priority hợp lệ: `low`, `medium`, `high`

### ⏰ Lấy Tasks quá hạn

**GET** `/api/tasks/overdue`

### 📆 Lấy Tasks trong khoảng thời gian

**GET**

```
/api/tasks/date-range?startDate=2025-07-01T00:00:00&endDate=2025-07-31T23:59:59
```

### 🔄 Cập nhật Status của Task

**PATCH** `/api/tasks/{taskId}/status`

```json
{
  "status": "completed"
}
```

---

## 🗓️ 3. Schedules API

### ➕ Tạo Schedule mới

**POST** `/api/schedules`

```json
{
  "title": "Học môn Toán",
  "startTime": "2025-07-08T09:00:00",
  "endTime": "2025-07-08T11:00:00",
  "description": "Buổi học lý thuyết lượng giác",
  "reminder": true
}
```

### 📋 Lấy tất cả Schedules

**GET** `/api/schedules`

### 🔍 Lấy Schedule theo ID

**GET** `/api/schedules/{scheduleId}`

### ✏️ Cập nhật Schedule

**PUT** `/api/schedules/{scheduleId}`

### ❌ Xóa Schedule

**DELETE** `/api/schedules/{scheduleId}`

### 📅 Lấy Schedules hôm nay

**GET** `/api/schedules/today`

### 🚀 Lấy Schedules sắp tới

**GET** `/api/schedules/upcoming`

### 🕓 Lấy Schedules trong khoảng thời gian

**GET**

```
/api/schedules/date-range?startDate=2025-07-01T00:00:00&endDate=2025-07-31T23:59:59
```

---

## 📝 4. Notes API

### ➕ Tạo Note mới

**POST** `/api/notes`

```json
{
  "title": "Ghi chú Toán",
  "content": "# Công thức lượng giác\n- sin^2(x) + cos^2(x) = 1",
  "isShared": true
}
```

### 📋 Lấy tất cả Notes

**GET** `/api/notes`

### 🔍 Lấy Note theo ID

**GET** `/api/notes/{noteId}`

### ✏️ Cập nhật Note

**PUT** `/api/notes/{noteId}`

### ❌ Xóa Note

**DELETE** `/api/notes/{noteId}`

### 🌍 Lấy Notes đã chia sẻ

**GET** `/api/notes/shared`

### 🔎 Tìm kiếm Notes

**GET** `/api/notes/search?keyword=toán`

### 🔄 Toggle trạng thái chia sẻ

**PATCH** `/api/notes/{noteId}/toggle-share`

---

## 💬 5. Community API

### 🧩 Posts

#### ➕ Tạo Post mới

**POST** `/api/community/posts`

```json
{
  "title": "Chia sẻ tài liệu Toán",
  "content": "Tài liệu về công thức lượng giác",
  "noteId": "uuid-of-note"
}
```

#### 📋 Lấy tất cả Posts

**GET** `/api/community/posts`

#### 🔍 Lấy Post theo ID

**GET** `/api/community/posts/{postId}`

#### ✏️ Cập nhật Post

**PUT** `/api/community/posts/{postId}`

#### ❌ Xóa Post

**DELETE** `/api/community/posts/{postId}`

#### 🔎 Tìm kiếm Posts

**GET** `/api/community/posts/search?keyword=toán`

#### 👤 Lấy Posts của tôi

**GET** `/api/community/posts/my-posts`

---

### 💭 Comments

#### ➕ Tạo Comment

**POST** `/api/community/posts/{postId}/comments`

```json
{
  "content": "Cảm ơn, rất hữu ích!"
}
```

#### 📋 Lấy Comments của Post

**GET** `/api/community/posts/{postId}/comments`

#### ❌ Xóa Comment

**DELETE** `/api/community/comments/{commentId}`

---

## 📦 Response Format

### ✅ Success Response

```json
{
  "statusCode": 200,
  "message": "Success message",
  "data": { /* response data */ }
}
```

### ❌ Error Response

```json
{
  "statusCode": 500,
  "message": "Error message",
  "data": null
}
```

---

## 📊 Status Codes

| Code | Meaning               |
| ---- | --------------------- |
| 200  | Success               |
| 201  | Created               |
| 400  | Bad Request           |
| 401  | Unauthorized          |
| 404  | Not Found             |
| 500  | Internal Server Error |

---

## 🔢 Enum Values

| Enum              | Values                                | Description                     |
| ----------------- | ------------------------------------- | ------------------------------- |
| **UserType**      | `STUDENT`, `OFFICE_WORKER`            | Sinh viên / Nhân viên văn phòng |
| **Task Priority** | `low`, `medium`, `high`               | Mức độ ưu tiên                  |
| **Task Status**   | `pending`, `in_progress`, `completed` | Trạng thái công việc            |

---

## 🗄️ Database Schema

Xem chi tiết trong file **`database_schema.sql`**.

**Tables:**

* `Users`
* `Roles`
* `User_Roles`
* `Tasks`
* `Schedules`
* `Notes`
* `Community_Posts`
* `Comments`

---

## 🧪 Testing với Postman

1. **Đăng ký tài khoản**
   `POST http://localhost:8080/author/register-user`

2. **Đăng nhập & lấy token**
   `POST http://localhost:8080/author/login`

3. **Thêm token vào Header**

   ```
   Authorization: Bearer <token_from_step_2>
   ```

4. **Test các endpoint khác**
   → Dùng token để truy cập các API `/api/...`

---

## ⚙️ Notes

* Tất cả UUID sử dụng định dạng **UUID v4**
* Các trường thời gian dùng định dạng **ISO-8601**

  ```
  2025-07-10T17:00:00
  ```
* JWT Token có thời hạn **24 giờ**
* Redis được sử dụng cho **caching**
* RabbitMQ được sử dụng cho **message queue**

---

## 📬 Contact & Support

Nếu có bất kỳ câu hỏi nào, vui lòng:

* 📧 Gửi email hỗ trợ
* 🐛 Tạo issue trên GitHub

---
