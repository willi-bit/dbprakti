import org.w3c.dom.Element;

import java.util.UUID;

public class Category {
    String name;
    String id;
    String parent;
    Element node;

    public Category(String categoryName, String categoryId, String categoryParent, Element node) {
        name = categoryName;
        id = categoryId;
        parent = categoryParent;
        this.node = node;
    }
    public Category(String categoryName, String categoryId, String categoryParent) {
        name = categoryName;
        id = categoryId;
        parent = categoryParent;
        this.node = null;
    }
}
