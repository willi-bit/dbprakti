import java.util.UUID;

public class Category {
    String name;
    UUID id;
    UUID parent;

    public Category(String categoryName, UUID categoryId, UUID categoryParent) {
        name = categoryName;
        id = categoryId;
        parent = categoryParent;
    }
}
