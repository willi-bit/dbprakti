package Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "OrderDetail")
public class OrderDetail {

    @EmbeddedId
    private OrderDetailId id;

    @ManyToOne
    @MapsId("customerOrderId")
    @JoinColumn(name = "CustomerOrder", referencedColumnName = "OrderID", foreignKey = @ForeignKey(name = "fk_order_detail_customer_order"))
    private CustomerOrder customerOrder;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "Product", referencedColumnName = "ProductID", foreignKey = @ForeignKey(name = "fk_order_detail_product"))
    private Product product;

    @Column(name = "Quantity", nullable = false)
    private int quantity;

    public OrderDetailId getId() {
        return id;
    }

    public void setId(OrderDetailId id) {
        this.id = id;
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
