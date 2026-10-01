package vn.hcmute.webpr330479.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "Category", schema = "dbo")
public class Category_24162111 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    private int id;

    @Column(name = "categoryName", columnDefinition = "NVARCHAR(200)")
    private String name;

    @Column(name = "images", columnDefinition = "NVARCHAR(500)")
    private String images;

    @Column(name = "status")
    private int status = 1;

    public Category_24162111() {}

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
