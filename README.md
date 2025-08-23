Tóm tắt API
Dự án này cung cấp một bộ API RESTful để quản lý người dùng và xác thực. 
api gốc: 
  http://localhost:8080

api không cần token để authen:
  "/api/users/create",
  "/auth/token",
  "/auth/logout",
  "/auth/refresh",
  "/auth/introspect"


1. API Xác thực (/auth)
Các API này chịu trách nhiệm xử lý việc đăng nhập, kiểm tra và làm mới token.

POST /auth/token
  
  Chức năng: Xác thực người dùng và cấp token truy cập (access token) và token làm mới (refresh token)
  RequestBody: { "email": "...", "password": "..." }
  Phản hồi: Cung cấp thông tin token đã cấp - đăng nhập
  
POST /auth/introspect
  
  Chức năng: Kiểm tra tính hợp lệ của một token truy cập
  RequestBody: { "token": "..." }
  Phản hồi: Trả về trạng thái hợp lệ của token (true hoặc false)
  
POST /auth/refresh
  
  Chức năng: Dùng refresh token để nhận một cặp token mới
  RequestBody: { "token": "..." }
  Phản hồi: Cung cấp cặp token mới
    
POST /auth/logout
  
  Chức năng: Vô hiệu hóa token, thực hiện việc đăng xuất người dùng
  RequestBody: { "token": "..." }
  Phản hồi: Không có nội dung


2. API Quản lý Người dùng (/api/users)
Các API này cho phép quản lý tài khoản người dùng và vai trò.

POST /api/users/create
  
  Chức năng: Tạo một tài khoản người dùng mới.
  RequestBody: { email, password, fullName, role ("ORGANIZER", "VOLUNTEER") }
  Phản hồi: Trả về thông tin của người dùng vừa được tạo.
  
GET /api/users
  
  Chức năng: Lấy danh sách tất cả người dùng trong hệ thống
  Phản hồi: Trả về một danh sách các đối tượng người dùng
  
POST /api/users/change-role
  
  Chức năng: Thay đổi vai trò (role) của một người dùng
  RequestBody: { "email": "...", "newRole": "..." }
  Phản hồi: Không có nội dung.
  
DELETE /api/users/delete
  
  Chức năng: Xóa một tài khoản người dùng.
  RequestBody: { "email": "..." }
  Phản hồi: Không có nội dung.

Cấu trúc phản hồi chung (ApiResponse):

JSON

{
  "code": 1000,
  "message": "...",
  "result": {}
}
code: Mã trạng thái của phản hồi (1000 cho thành công).

message: Mô tả chi tiết (thường là k hiển thị khi thành công).

result: Dữ liệu kết quả của API.
