import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;

public class XMLToDatabase {

    String leipzigPath = "data/leipzig_transformed.xml";
    String reviewPath = "data/reviews.csv";
    public static void main() {

        try {
            // Parse the XML file
            // Uncomment to start parsing categories
            // startCategoryParsing();
            startDresdenParsing();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void startDresdenParsing() {
        String dresdenPath = "data/dresden.xml";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(dresdenPath));
            document.getDocumentElement().normalize();
            processDresden(document.getDocumentElement());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private static void processDresden(Element startElement) {
        NodeList nodes = startElement.getChildNodes();
        System.out.println("hekjhedhkj");
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(new String(nodes.item(i).getTextContent()));
        }
    }
    private static void startCategoryParsing() {
        String categoryPath = "data/categories.xml";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(categoryPath));
            document.getDocumentElement().normalize();
            processCategories(document.getDocumentElement(), null);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /*
    {<category:
        name: String
        parentCategory: UUID,
        id: UUID
    ,
    {item: String}>,
    }
     */
    private static List<Map<Category, List<String>>> processCategories(Element element, UUID parentCategoryId) {
        NodeList categories = element.getElementsByTagName("category");
        List<Map<Category, List<String>>> mapList = new ArrayList<>();
        for (int i = 0; i < categories.getLength(); i++) {
            Map<Category, List<String>> map = new HashMap<>();
            Element categoryElement = (Element) categories.item(i);
            NodeList nl = categoryElement.getElementsByTagName("category");
            String categoryName = categoryElement.getTextContent().trim().split("\\n")[0]; // Extract the category name
            UUID categoryId = UUID.randomUUID();
            Category category = new Category(categoryName, categoryId, parentCategoryId);
            List<String> list = processItems(categoryElement);
            map.put(category, list);
            mapList.add(map);
            if (nl.getLength() > 0) {
                for (int j = 0; j < nl.getLength(); j++) {
                    Element newCategory = (Element) nl.item(j);
                    processCategories(newCategory, categoryId);
                }
            }
        }
        for(Map<Category, List<String>> map : mapList) {
                System.out.println(map.keySet().iterator().next().name + ": " + map.values().iterator().next().toString());
        }
        return mapList;
    }

    private static List<String> processItems(Element categoryElement) {
        NodeList items = categoryElement.getElementsByTagName("item");
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < items.getLength(); i++) {
            Element itemElement = (Element) items.item(i);
            String itemCode = itemElement.getTextContent().trim();
            list.add(itemCode);
        }
        return list;
    }

}
