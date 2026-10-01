-- Database đã có: chạy file này để nâng cấp giỏ hàng và COD.
-- Cài mới: chạy de05_online.sql trước, sau đó chạy file này.
USE [De05OnlineDB];
GO
SET XACT_ABORT ON;
BEGIN TRANSACTION;

IF OBJECT_ID(N'dbo.Cart', N'U') IS NULL OR OBJECT_ID(N'dbo.CartItem', N'U') IS NULL
    THROW 51000, N'Chưa có bảng Cart/CartItem. Hãy tạo database gốc trước.', 1;

IF COL_LENGTH(N'dbo.Cart', N'recipientName') IS NULL
    ALTER TABLE dbo.Cart ADD recipientName NVARCHAR(100) NULL;
IF COL_LENGTH(N'dbo.Cart', N'recipientPhone') IS NULL
    ALTER TABLE dbo.Cart ADD recipientPhone NVARCHAR(20) NULL;
IF COL_LENGTH(N'dbo.Cart', N'shippingAddress') IS NULL
    ALTER TABLE dbo.Cart ADD shippingAddress NVARCHAR(500) NULL;
IF COL_LENGTH(N'dbo.Cart', N'orderNote') IS NULL
    ALTER TABLE dbo.Cart ADD orderNote NVARCHAR(500) NULL;
IF COL_LENGTH(N'dbo.Cart', N'paymentMethod') IS NULL
    ALTER TABLE dbo.Cart ADD paymentMethod NVARCHAR(10) NULL;
IF COL_LENGTH(N'dbo.Cart', N'totalAmount') IS NULL
    ALTER TABLE dbo.Cart ADD totalAmount DECIMAL(19, 2) NULL;

IF COL_LENGTH(N'dbo.CartItem', N'productName') IS NULL
    ALTER TABLE dbo.CartItem ADD productName NVARCHAR(200) NULL;
IF COL_LENGTH(N'dbo.CartItem', N'productCode') IS NULL
    ALTER TABLE dbo.CartItem ADD productCode BIGINT NULL;
IF COL_LENGTH(N'dbo.CartItem', N'productImage') IS NULL
    ALTER TABLE dbo.CartItem ADD productImage NVARCHAR(500) NULL;

ALTER TABLE dbo.CartItem ALTER COLUMN unitPrice DECIMAL(19, 2) NULL;

-- Dùng dynamic SQL vì các cột vừa được thêm trong cùng batch.
EXEC sys.sp_executesql N'
    UPDATE i
    SET productName = COALESCE(i.productName, p.productName),
        productCode = COALESCE(i.productCode, p.productCode),
        productImage = COALESCE(i.productImage, p.images)
    FROM dbo.CartItem i
    JOIN dbo.Product p ON p.productId = i.productId;

    UPDATE c
    SET totalAmount = COALESCE(c.totalAmount, t.totalAmount, 0),
        paymentMethod = COALESCE(c.paymentMethod, N''COD'')
    FROM dbo.Cart c
    OUTER APPLY (
        SELECT SUM(i.unitPrice * i.quantity) AS totalAmount
        FROM dbo.CartItem i WHERE i.cartId = c.cartId
    ) t
    WHERE c.status BETWEEN 0 AND 7;
';

UPDATE dbo.Cart SET status = -1 WHERE status IS NULL;

IF EXISTS (
    SELECT userId FROM dbo.Cart WHERE status = -1 AND userId IS NOT NULL
    GROUP BY userId HAVING COUNT(*) > 1
)
    THROW 51001, N'Có nhiều giỏ đang hoạt động của cùng user. Kiểm tra dữ liệu trước khi nâng cấp.', 1;

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'UX_Cart_ActiveUser' AND object_id = OBJECT_ID(N'dbo.Cart'))
    CREATE UNIQUE INDEX UX_Cart_ActiveUser ON dbo.Cart(userId)
    WHERE status = -1 AND userId IS NOT NULL;

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'UX_CartItem_Product' AND object_id = OBJECT_ID(N'dbo.CartItem'))
    CREATE UNIQUE INDEX UX_CartItem_Product ON dbo.CartItem(cartId, productId)
    WHERE cartId IS NOT NULL AND productId IS NOT NULL;

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'IX_Cart_UserStatusDate' AND object_id = OBJECT_ID(N'dbo.Cart'))
    CREATE INDEX IX_Cart_UserStatusDate ON dbo.Cart(userId, status, buyDate DESC);

IF OBJECT_ID(N'dbo.CK_Cart_Status', N'C') IS NULL
    ALTER TABLE dbo.Cart WITH CHECK ADD CONSTRAINT CK_Cart_Status CHECK (status BETWEEN -1 AND 7);

IF OBJECT_ID(N'dbo.CK_CartItem_Quantity', N'C') IS NULL
    ALTER TABLE dbo.CartItem WITH CHECK ADD CONSTRAINT CK_CartItem_Quantity CHECK (quantity BETWEEN 1 AND 99);

IF OBJECT_ID(N'dbo.CK_CartItem_UnitPrice', N'C') IS NULL
    ALTER TABLE dbo.CartItem WITH CHECK ADD CONSTRAINT CK_CartItem_UnitPrice CHECK (unitPrice >= 0);

COMMIT TRANSACTION;
GO

SELECT N'Nâng cấp giỏ hàng và COD hoàn tất.' AS Result;
