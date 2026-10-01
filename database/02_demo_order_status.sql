-- Chạy SELECT để lấy mã đơn. Phần UPDATE bên dưới đang được comment.
USE [De05OnlineDB];
GO

SELECT c.cartId, u.username, c.buyDate, c.status, c.totalAmount, c.paymentMethod
FROM dbo.Cart c
JOIN dbo.Users u ON u.userId = c.userId
WHERE c.status BETWEEN 0 AND 7
ORDER BY c.buyDate DESC;

/* Sao chép đúng cartId từ kết quả SELECT, bỏ comment và chạy từng lần:

DECLARE @OrderId NVARCHAR(50) = N'CART-THAY-BANG-MA-DON-THAT';
DECLARE @Status INT = 0;

-- 0: Đơn hàng mới       1: Đã xác nhận
-- 2: Chuẩn bị hàng      3: Vận chuyển
-- 4: Giao hàng          5: Đã giao
-- 6: Đơn hàng hủy       7: Đơn hàng hoàn

IF @Status NOT BETWEEN 0 AND 7
    THROW 51002, N'Trạng thái chỉ từ 0 đến 7.', 1;

UPDATE dbo.Cart
SET status = @Status
WHERE cartId = @OrderId AND status BETWEEN 0 AND 7;

IF @@ROWCOUNT <> 1
    THROW 51003, N'Không tìm thấy đúng một đơn hàng đã đặt.', 1;

SELECT cartId, status, totalAmount FROM dbo.Cart WHERE cartId = @OrderId;
*/

-- Sau khi đổi trạng thái, tải lại /orders. Script không tự hoàn kho.
