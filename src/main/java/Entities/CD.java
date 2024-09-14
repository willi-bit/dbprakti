package Entities;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name="CD")
public class CD {

    @Id
    @Column(name = "ProductID")
    public String productId;

    @Column(name="Label", nullable = false)
    public String label;

    @Column(name="ReleaseDate", nullable = false)
    @Temporal(TemporalType.DATE)
    public java.sql.Date releaseDate;

    @Column(name="TitleList", nullable = false)
    public String titleList;

    @OneToOne
    @JoinColumn(name = "ProductID", referencedColumnName = "ProductID", foreignKey = @ForeignKey(name = "fk_cd_product"))
    public Product product;

    public CD() {}

    public CD(String id, String artist, String label, Date releaseDate, String titleList) {
        this.productId = id;
        this.label = label;
        this.releaseDate = new java.sql.Date(releaseDate.getTime());
        this.titleList = titleList;
    }
    public CD(String id, String artist, String label, java.sql.Date releaseDate, String titleList) {
        this.productId = id;
        this.label = label;
        this.releaseDate = releaseDate;
        this.titleList = titleList;
    }
}
