package Entities;

import jakarta.persistence.*;
import org.w3c.dom.Element;

@Entity
@Table(name = "Category", uniqueConstraints = @UniqueConstraint(columnNames = {"Name", "ParentCategory"}))
public class Category {

    @Id
    @Column(name = "CategoryID")
    public String categoryId;
    @Column(name="name")
    public String name;
    @Column(name = "parentCategory")
    public String parentCategory;

    public Element node;

    public Category() {}

    public Category(String categoryName, String categoryId, String categoryParent, Element node) {
        name = categoryName;
        categoryId = categoryId;
        parentCategory = categoryParent;
        this.node = node;
    }
    public Category(String categoryName, String categoryId, String categoryParent) {
        name = categoryName;
        categoryId = categoryId;
        parentCategory = categoryParent;
        this.node = null;
    }
}
