package vn.hcmute.webpr330479.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "UserRoles", schema = "dbo")
public class UserRole_24162111 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleId")
    private int id;

    @Column(name = "roleName", columnDefinition = "NVARCHAR(50)")
    private String roleName;

    public UserRole_24162111() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
