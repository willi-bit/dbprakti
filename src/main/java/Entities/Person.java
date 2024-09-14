package Entities;

import jakarta.persistence.*;

@Entity
@Table(name="Person")
public class Person {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer personid;
    @Column(name="name", unique = true)
    public String name;

    public Person() {}

    public Person(String name) {
        this.name = name;
    }
}
