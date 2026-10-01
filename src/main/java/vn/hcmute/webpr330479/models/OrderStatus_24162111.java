package vn.hcmute.webpr330479.models;

public enum OrderStatus_24162111 {
    NEW(0, "Đơn hàng mới"),
    CONFIRMED(1, "Đã xác nhận"),
    PREPARING(2, "Chuẩn bị hàng"),
    SHIPPING(3, "Vận chuyển"),
    DELIVERING(4, "Giao hàng"),
    DELIVERED(5, "Đã giao"),
    CANCELLED(6, "Đơn hàng hủy"),
    RETURNED(7, "Đơn hàng hoàn");

    public static final int ACTIVE_CART = -1;
    private final int code;
    private final String label;

    OrderStatus_24162111(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static OrderStatus_24162111 fromCode(int code) {
        for (OrderStatus_24162111 status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Trạng thái đơn hàng không hợp lệ.");
    }
}
