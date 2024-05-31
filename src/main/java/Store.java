import java.util.UUID;

public class Store {
    String id;
    String name;
    String address;

    public Store(String name, String address) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
    }
}
