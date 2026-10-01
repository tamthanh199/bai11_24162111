**24162111 - Doan Thanh Tam**

**Bài tập 11**

Giỏ hàng, thanh toán COD và lịch sử đặt hàng cho vai trò USER.

**Cấu hình trước khi chạy**

Project sử dụng Java 26, Maven, Tomcat 10.1, JPA/Hibernate, JSP/JSTL, SiteMesh 3 và SQL Server.

Kiểm tra cấu hình SQL Server trong file:

`src/main/resources/META-INF/persistence.xml`

Điều chỉnh `jakarta.persistence.jdbc.url`, `jakarta.persistence.jdbc.user` và `jakarta.persistence.jdbc.password` theo SQL Server trên máy chạy project. Có thể cấu hình bằng biến môi trường `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.

Database sử dụng: `De05OnlineDB`.

Nếu dùng chức năng gửi OTP qua email, cấu hình thêm `MAIL_USERNAME` và `MAIL_PASSWORD`.

**Database**

- Database đã có từ bài kiểm tra: chạy `database/01_upgrade_cart_cod.sql`.
- Cài mới: chạy `database/de05_online.sql`, sau đó chạy `database/01_upgrade_cart_cod.sql`.
- File `de05_online.sql` xóa và tạo lại database, chỉ dùng khi cài mới.

Các bảng giỏ hàng và đơn hàng: `Cart`, `CartItem`.

**Chạy project**

Đường dẫn trên máy sử dụng cổng 8082:

http://localhost:8082/de05_24162111/home

Nếu Tomcat dùng cổng khác, thay `8082` bằng cổng đã cấu hình.

Tài khoản USER trong dữ liệu mẫu: `user01 / 123456`.

**Các chức năng**

- Thêm sản phẩm vào giỏ hàng từ trang chủ và trang chi tiết.
- Gộp số lượng khi thêm lại cùng sản phẩm.
- Cập nhật số lượng, xóa từng sản phẩm và xóa toàn bộ giỏ hàng.
- Giới hạn số lượng từ 1 đến 99 và không vượt tồn kho.
- Thanh toán đơn hàng bằng COD.
- Kiểm tra thông tin người nhận, số điện thoại và địa chỉ giao hàng.
- Lưu thông tin đơn hàng, tổng tiền và trừ tồn kho khi đặt hàng thành công.
- Xem lịch sử đặt hàng và chi tiết đơn hàng của tài khoản đang đăng nhập.
- Lọc lịch sử theo 8 trạng thái, phân trang 6 đơn mỗi trang.

**Đường dẫn chức năng**

| Chức năng | Đường dẫn |
|---|---|
| Trang chủ | http://localhost:8082/de05_24162111/home |
| Danh sách sản phẩm | http://localhost:8082/de05_24162111/products |
| Đăng nhập | http://localhost:8082/de05_24162111/login |
| Đăng xuất | http://localhost:8082/de05_24162111/logout |
| Giỏ hàng | http://localhost:8082/de05_24162111/cart |
| Thanh toán COD | http://localhost:8082/de05_24162111/checkout |
| Lịch sử đặt hàng | http://localhost:8082/de05_24162111/orders |
| Lọc đơn hàng mới | http://localhost:8082/de05_24162111/orders?status=0 |

**Trạng thái đơn hàng**

| `Cart.status` | Trạng thái |
|---|---|
| -1 | Giỏ hàng đang hoạt động |
| 0 | Đơn hàng mới |
| 1 | Đã xác nhận |
| 2 | Chuẩn bị hàng |
| 3 | Vận chuyển |
| 4 | Giao hàng |
| 5 | Đã giao |
| 6 | Đơn hàng hủy |
| 7 | Đơn hàng hoàn |

Chạy `database/02_demo_order_status.sql` để lấy mã đơn. Làm theo hướng dẫn trong file để đổi trạng thái từ 0 đến 7, sau đó tải lại trang lịch sử hoặc chi tiết đơn hàng. Script đổi trạng thái không tự hoàn kho.

**Phân quyền**

USER phải đăng nhập để sử dụng giỏ hàng, thanh toán COD và xem lịch sử đặt hàng. Mỗi tài khoản chỉ được thao tác giỏ hàng và xem đơn hàng của mình.
