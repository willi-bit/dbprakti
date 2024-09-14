package Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "DVD_Actor")
public class DVDActor {

    @EmbeddedId
    public DVDActorId id;

    @ManyToOne
    @MapsId("dvdProductId")
    @JoinColumn(name = "DVD_ProductID", referencedColumnName = "ProductID", foreignKey = @ForeignKey(name = "fk_dvd_actor_dvd"))
    public DVD dvd;

    @ManyToOne
    @MapsId("actorId")
    @JoinColumn(name = "Actor_ID", referencedColumnName = "PersonID", foreignKey = @ForeignKey(name = "fk_dvd_actor_person"))
    public Person actor;
}
