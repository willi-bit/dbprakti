import java.util.UUID;

public class Category {
    String name;
    String id;
    String parent;

    public Category(String categoryName, String categoryId, String categoryParent) {
        name = categoryName;
        id = categoryId;
        parent = categoryParent;
    }
}
