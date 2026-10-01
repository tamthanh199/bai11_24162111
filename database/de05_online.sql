-- Chỉ dùng khi cài mới: file này xóa và tạo lại De05OnlineDB.
-- Sau khi chạy, tiếp tục chạy 01_upgrade_cart_cod.sql.
USE master;
GO
IF DB_ID('De05OnlineDB') IS NOT NULL
BEGIN
    ALTER DATABASE De05OnlineDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE De05OnlineDB;
END
GO
CREATE DATABASE De05OnlineDB;
GO
USE De05OnlineDB;
GO

CREATE TABLE UserRoles (
    roleId INT IDENTITY(1,1) PRIMARY KEY,
    roleName NVARCHAR(50) NULL
);

CREATE TABLE Seller (
    sellerId INT IDENTITY(1,1) PRIMARY KEY,
    sellername NVARCHAR(50) NULL,
    images NVARCHAR(500) NULL,
    status INT NULL
);

CREATE TABLE Users (
    userId INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) NULL,
    email NVARCHAR(100) NULL,
    fullname NVARCHAR(50) NULL,
    password NVARCHAR(50) NULL,
    images NVARCHAR(500) NULL,
    phone NVARCHAR(20) NULL,
    status INT NULL,
    code NVARCHAR(50) NULL,
    roleId INT NULL,
    sellerId INT NULL,
    CONSTRAINT FK_Users_Role FOREIGN KEY(roleId) REFERENCES UserRoles(roleId),
    CONSTRAINT FK_Users_Seller FOREIGN KEY(sellerId) REFERENCES Seller(sellerId)
);
CREATE UNIQUE INDEX UX_Users_Username ON Users(username) WHERE username IS NOT NULL;
CREATE UNIQUE INDEX UX_Users_Email ON Users(email) WHERE email IS NOT NULL;

CREATE TABLE Category (
    categoryId INT IDENTITY(1,1) PRIMARY KEY,
    categoryName NVARCHAR(200) NULL,
    images NVARCHAR(500) NULL,
    status INT NULL
);

CREATE TABLE Product (
    productId INT IDENTITY(1,1) PRIMARY KEY,
    productName NVARCHAR(200) NULL,
    productCode BIGINT NULL,
    categoryId INT NULL,
    description NVARCHAR(500) NULL,
    price FLOAT NULL,
    amount INT NULL,
    stock INT NULL,
    images NVARCHAR(500) NULL,
    wishlist INT NULL,
    status INT NULL,
    createDate DATE NULL,
    sellerId INT NULL,
    CONSTRAINT FK_Product_Category FOREIGN KEY(categoryId) REFERENCES Category(categoryId),
    CONSTRAINT FK_Product_Seller FOREIGN KEY(sellerId) REFERENCES Seller(sellerId)
);

CREATE TABLE Cart (
    cartId NVARCHAR(50) PRIMARY KEY,
    userId INT NULL,
    buyDate DATETIME NULL,
    status INT NULL,
    CONSTRAINT FK_Cart_User FOREIGN KEY(userId) REFERENCES Users(userId)
);

CREATE TABLE CartItem (
    cartItemId NVARCHAR(50) PRIMARY KEY,
    quantity INT NULL,
    unitPrice FLOAT NULL,
    productId INT NULL,
    cartId NVARCHAR(50) NULL,
    CONSTRAINT FK_CartItem_Product FOREIGN KEY(productId) REFERENCES Product(productId),
    CONSTRAINT FK_CartItem_Cart FOREIGN KEY(cartId) REFERENCES Cart(cartId)
);
GO

INSERT INTO UserRoles(roleName) VALUES (N'USER'), (N'SELLER'), (N'ADMIN');
INSERT INTO Seller(sellername,images,status) VALUES
(N'Cửa hàng Alpha',N'assets/no-user.svg',1),
(N'Cửa hàng Beta',N'assets/no-user.svg',1),
(N'Cửa hàng Gamma',N'assets/no-user.svg',1);
INSERT INTO Category(categoryName,images,status) VALUES
(N'Laptop',N'assets/no-category.svg',1),
(N'Điện thoại',N'assets/no-category.svg',1),
(N'Phụ kiện',N'assets/no-category.svg',1),
(N'Màn hình',N'assets/no-category.svg',1),
(N'Bàn phím',N'assets/no-category.svg',1),
(N'Chuột',N'assets/no-category.svg',1),
(N'Tai nghe',N'assets/no-category.svg',1);

INSERT INTO Users(username,email,fullname,password,images,phone,status,code,roleId,sellerId) VALUES
(N'admin',N'admin@gmail.com',N'Administrator',N'123456',N'assets/no-user.svg',N'0900000000',1,NULL,3,NULL),
(N'user01',N'user01@gmail.com',N'Người dùng 01',N'123456',N'assets/no-user.svg',N'0900000001',1,NULL,1,NULL),
(N'seller01',N'seller01@gmail.com',N'Chủ shop Alpha',N'123456',N'assets/no-user.svg',N'0900000002',1,NULL,2,1),
(N'seller02',N'seller02@gmail.com',N'Chủ shop Beta',N'123456',N'assets/no-user.svg',N'0900000003',1,NULL,2,2),
(N'seller03',N'seller03@gmail.com',N'Chủ shop Gamma',N'123456',N'assets/no-user.svg',N'0900000004',1,NULL,2,3);

INSERT INTO Product(productName,productCode,categoryId,description,price,amount,stock,images,wishlist,status,createDate,sellerId) VALUES
(N'Laptop học tập A',1001001,1,N'Laptop phù hợp học tập và văn phòng.',15990000,12,12,N'assets/no-product.svg',4,1,'2026-09-20',1),
(N'Điện thoại Alpha X',1001002,2,N'Điện thoại màn hình lớn.',8990000,20,20,N'assets/no-product.svg',7,1,'2026-09-21',1),
(N'Chuột không dây Alpha',1001003,6,N'Chuột không dây gọn nhẹ.',390000,35,35,N'assets/no-product.svg',9,1,'2026-09-22',1),
(N'Laptop Gaming Beta',2002001,1,N'Laptop gaming hiệu năng cao.',27990000,8,8,N'assets/no-product.svg',12,1,'2026-09-19',2),
(N'Màn hình Beta 27',2002002,4,N'Màn hình 27 inch 144Hz.',5290000,16,16,N'assets/no-product.svg',8,1,'2026-09-20',2),
(N'Bàn phím cơ Beta',2002003,5,N'Bàn phím cơ LED RGB.',1290000,28,28,N'assets/no-product.svg',13,1,'2026-09-23',2),
(N'Tai nghe Gamma Pro',3003001,7,N'Tai nghe gaming có micro.',1590000,22,22,N'assets/no-product.svg',10,1,'2026-09-18',3),
(N'Sạc nhanh Gamma 65W',3003002,3,N'Củ sạc nhanh USB-C 65W.',790000,40,40,N'assets/no-product.svg',6,1,'2026-09-21',3),
(N'Điện thoại Gamma Mini',3003003,2,N'Điện thoại nhỏ gọn.',6990000,14,14,N'assets/no-product.svg',5,1,'2026-09-24',3);
GO
