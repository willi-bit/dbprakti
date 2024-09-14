package Entities;

import jakarta.persistence.*;

@Entity
@Table(name="DVD")
public class DVD {

    @Id
    @Column(name = "ProductID")
    public String productId;

    @Column(name="Format")
    public String format;

    @Column(name="Length")
    public Integer length;

    @Column(name="RegionCode")
    public Integer regionCode;

    @ManyToOne
    @JoinColumn(name = "Director", referencedColumnName = "PersonID", foreignKey = @ForeignKey(name = "fk_dvd_director"))
    public Person director;

    @OneToOne
    @JoinColumn(name = "ProductID", referencedColumnName = "ProductID", foreignKey = @ForeignKey(name = "fk_dvd_product"))
    private Product product;

    public DVD() {}

    public DVD(String id, String format, Integer length, Integer regionCode, String actors, String creator, String director) {
        this.productId = id;
        this.format = format;
        this.length = length;
        this.regionCode = regionCode;
    }
}