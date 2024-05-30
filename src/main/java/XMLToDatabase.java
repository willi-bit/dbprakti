import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.sql.*;
import java.util.*;

public class XMLToDatabase {

    public static void main(String[] args) {
        String xmlFilePath = "data/categories.xml";
        // String jdbcUrl = "jdbc:postgresql://localhost:5432/your_database";
        String username = "your_username";
        String password = "your_password";

        try {
            // Parse the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(xmlFilePath));
            document.getDocumentElement().normalize();

            // Process the XML and insert data into the database
            processCategories(document.getDocumentElement(), null);
        } catch (Exception e) {
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
    private static List<Map<Category, List<String>>> processCategories(Element element, UUID parentCategoryId) throws SQLException {
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

    private static List<String> processItems(Element categoryElement) throws SQLException {
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