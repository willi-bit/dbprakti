package Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Customer")
public class Customer {

    @Id
    @Column(name = "CustomerID")
    public String customerId;

    @Column(name = "Name")
    public String name;

    @Column(name = "Address")
    public String address;

    @Column(name = "BankAccount")
    public String bankAccount;

}
