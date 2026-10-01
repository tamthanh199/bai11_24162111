package vn.hcmute.webpr330479.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "Seller", schema = "dbo")
public class Seller_24162111 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sellerId")
    private int id;

    @Column(name = "sellername", columnDefinition = "NVARCHAR(50)")
    private String name;

    @Column(name = "images", columnDefinition = "NVARCHAR(500)")
    private String images;

    @Column(name = "status")
    private int status = 1;

    public Seller_24162111() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
