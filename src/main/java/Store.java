import java.util.UUID;

public class Store {
    UUID id;
    String name;
    String address;

    public Store(String name, String address) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.address = address;
    }
}
