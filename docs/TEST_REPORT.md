# Kết quả kiểm tra — 01/10/2026

## Môi trường

- Amazon Corretto JDK 26.0.2, Maven 3.9.11.
- Hibernate 7.4.6.Final, SiteMesh 3.2.1, JSP/JSTL Jakarta của project gốc.
- Embedded Tomcat 10.1.60; H2 2.3.232 trong chế độ tương thích MSSQLServer.
- Cấu hình production gốc giữ nguyên. Database kiểm thử tách riêng ở `tests/src/test/resources/META-INF/persistence.xml` và chỉ dùng trong test.

## Build và automated tests

```sh
mvn clean package
mvn -f tests/pom.xml test
```

Build WAR thành công với Java 26. **15 tests, 0 failure, 0 error**.

| Nhóm | Tình huống | Kết quả |
| --- | --- | --- |
| Giỏ hàng | Thêm, gộp dòng, sửa số lượng, xóa một dòng, xóa toàn bộ; thao tác giỏ không trừ kho | Đạt |
| Giới hạn | Số lượng 0, âm, lớn hơn 99, vượt tồn kho, số thập phân, chuỗi, số vượt kiểu int, null | Đạt |
| COD | Đơn mới, thông tin giao hàng, tổng BigDecimal, trừ kho, giỏ mới trống | Đạt |
| Chống trùng | Gửi lại cùng form; hai request đồng thời cùng một giỏ | Đạt, chỉ một đơn và một lần trừ kho |
| Giá sản phẩm | Đổi giá sau màn hình xác nhận; không tạo đơn nếu chưa xem lại | Đạt |
| Snapshot | Đổi tên/giá sản phẩm sau khi đã đặt; đơn vẫn giữ tên/giá đã chốt | Đạt |
| Tồn kho | Giảm kho hoặc ngừng bán sau khi thêm giỏ | Đạt, không tạo đơn sai |
| Atomicity | Giỏ có hai sản phẩm, một sản phẩm thiếu hàng | Đạt, rollback cả đơn |
| Mua đồng thời | Hai USER mua sản phẩm cuối cùng | Đạt, chỉ một người thành công, tồn kho bằng 0 |
| Quyền sở hữu | Sửa/xóa giỏ, thanh toán, xem đơn và chi tiết của tài khoản khác | Đạt, bị từ chối |
| Tài khoản | USER bị khóa trong database dù còn session | Đạt, service từ chối |
| Validation | Giỏ trống và thông tin giao hàng sai; điện thoại dạng +84 hợp lệ | Đạt |
| Lịch sử | Đổi database lần lượt đủ 8 trạng thái; filter, nhãn và số đếm cập nhật | Đạt |
| Phân trang | 7 đơn: trang 1 có 6, trang 2 có 1; mới nhất trước | Đạt |
| HTTP/JSP | Đăng nhập → thêm giỏ → checkout → chi tiết → lịch sử; render thật JSP/JSTL/SiteMesh | Đạt |
| CSRF/phương thức | POST thiếu CSRF bị 403, GET vào URL thay đổi giỏ bị 405 | Đạt |
| URL lỗi | Trạng thái ngoài phạm vi bị 400, đơn không thuộc tài khoản bị 404 | Đạt |
| Escaping | Tên sản phẩm/ghi chú chứa HTML được hiển thị như văn bản | Đạt |

Các dòng trong bảng là tình huống kiểm tra; một test có thể xác minh nhiều tình huống.

## Kiểm tra giao diện thực tế

Đã chạy trình duyệt Chromium với Playwright qua luồng đăng nhập, thêm giỏ, xác nhận COD và xem lịch sử. Đã quan sát ảnh giỏ hàng, checkout, chi tiết đơn ở màn hình desktop và lịch sử ở chiều rộng 390px. Trang lịch sử mobile có chiều rộng nội dung 390px, không tràn ngang. Các fragment JSP khai báo UTF-8; test HTTP xác minh nút “Thêm vào giỏ” hiển thị đúng tiếng Việt.

Ảnh minh họa nằm trong `docs/screenshots/`. Dữ liệu ảnh là dữ liệu kiểm thử.

## Cần xác minh trên máy nộp bài

Chưa có SQL Server thật của người dùng trong môi trường kiểm thử, nên không khẳng định đã chạy migration trên database đó. Trước khi nộp, chạy `database/01_upgrade_cart_cod.sql` trên **De05OnlineDB** hiện có, rồi thực hiện luồng demo trong README để xác minh JDBC, quyền SQL Server và cấu hình Tomcat trên máy của bạn. Script trạng thái mẫu ở `database/02_demo_order_status.sql` mặc định chỉ SELECT.

Tests H2 xác minh logic, transaction và luồng web; không thay thế việc chạy SQL Server thật. Không tạo commit hay đẩy GitHub trong quá trình kiểm thử này.
