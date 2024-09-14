package Entities;

import jakarta.persistence.*;

@Entity
@Table(name="ProductCategories")
public class ProductCategories {

    @EmbeddedId
    public ProductCategoriesId id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "Product", referencedColumnName = "ProductID", foreignKey = @ForeignKey(name = "fk_product"))
    public Product product;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "Category", referencedColumnName = "CategoryID", foreignKey = @ForeignKey(name = "fk_category"))
    public Category category;
}
