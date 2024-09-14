package Entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CustomerOrder")
public class CustomerOrder {

    @Id
    @Column(name = "OrderID")
    public String orderId;

    @ManyToOne
    @JoinColumn(name = "Customer", referencedColumnName = "CustomerID", foreignKey = @ForeignKey(name = "fk_customer_order_customer"))
    public Customer customer;

    @Column(name = "Date")
    @Temporal(TemporalType.DATE)
    public Date date;

}
