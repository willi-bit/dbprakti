package Entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="Store", uniqueConstraints = @UniqueConstraint(columnNames = {"Name", "Address"}))
public class Store {

    @Id
    @Column(name = "StoreID")
    public String storeId;

    @Column(name="Name")
    public String name;

    @Column(name="Address")
    public String address;

    public Store() {}

    public Store(String name, String address) {
        this.storeId = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
    }
}
