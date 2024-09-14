package Entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DVDActorId implements Serializable {

    @Column(name = "DVD_ProductID")
    public String dvdProductId;

    @Column(name = "Actor_ID")
    public int actorId;

    public DVDActorId() {}

    public DVDActorId(String dvdProductId, int actorId) {
        this.dvdProductId = dvdProductId;
        this.actorId = actorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DVDActorId that = (DVDActorId) o;
        return actorId == that.actorId &&
                Objects.equals(dvdProductId, that.dvdProductId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dvdProductId, actorId);
    }

}