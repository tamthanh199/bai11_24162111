package vn.hcmute.webpr330479.models;

import vn.hcmute.webpr330479.services.ShopException_24162111;

public class ShippingInfo_24162111 {
    private final String name;
    private final String phone;
    private final String address;
    private final String note;

    public ShippingInfo_24162111(String name, String phone, String address, String note) {
        this.name = clean(name);
        this.phone = clean(phone);
        this.address = clean(address);
        this.note = clean(note);
    }

    public void validate() {
        if (name.length() < 2 || name.length() > 100) {
            throw new ShopException_24162111("Tên người nhận phải từ 2 đến 100 ký tự.");
        }
        if (!phone.matches("(?:0[0-9]{9}|\\+84[0-9]{9})")) {
            throw new ShopException_24162111(
                    "Số điện thoại gồm 10 số bắt đầu bằng 0, hoặc +84 và 9 số.");
        }
        if (address.length() < 10 || address.length() > 500) {
            throw new ShopException_24162111("Địa chỉ giao hàng phải từ 10 đến 500 ký tự.");
        }
        if (note.length() > 500) {
            throw new ShopException_24162111("Ghi chú tối đa 500 ký tự.");
        }
    }

    private static String clean(String value) {
        return value == null ? "" : value.trim();
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getNote() {
        return note;
    }
}
