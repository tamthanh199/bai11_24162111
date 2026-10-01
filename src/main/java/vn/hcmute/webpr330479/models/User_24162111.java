package vn.hcmute.webpr330479.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "Users", schema = "dbo")
public class User_24162111 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int id;

    @Column(name = "username", columnDefinition = "NVARCHAR(50)")
    private String username;

    @Column(name = "email", columnDefinition = "NVARCHAR(100)")
    private String email;

    @Column(name = "fullname", columnDefinition = "NVARCHAR(50)")
    private String fullName;

    @Column(name = "password", columnDefinition = "NVARCHAR(50)")
    private String password;

    @Column(name = "images", columnDefinition = "NVARCHAR(500)")
    private String images;

    @Column(name = "phone", columnDefinition = "NVARCHAR(20)")
    private String phone;

    @Column(name = "status")
    private int status = 1;

    @Column(name = "code", columnDefinition = "NVARCHAR(50)")
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId")
    private UserRole_24162111 role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sellerId")
    private Seller_24162111 seller;

    public User_24162111() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserRole_24162111 getRole() {
        return role;
    }

    public void setRole(UserRole_24162111 role) {
        this.role = role;
    }

    public Seller_24162111 getSeller() {
        return seller;
    }

    public void setSeller(Seller_24162111 seller) {
        this.seller = seller;
    }

    @Transient
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    @Transient
    public boolean isSeller() {
        return hasRole("SELLER");
    }

    @Transient
    public boolean isUser() {
        return hasRole("USER");
    }

    @Transient
    public String getDisplayName() {
        return fullName == null || fullName.isBlank() ? username : fullName;
    }

    private boolean hasRole(String name) {
        return role != null
                && role.getRoleName() != null
                && role.getRoleName().equalsIgnoreCase(name);
    }
}
