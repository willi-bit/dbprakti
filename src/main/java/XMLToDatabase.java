import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class XMLToDatabase {

    String leipzigPath = "data/leipzig_transformed.xml";
    String reviewPath = "data/reviews.csv";
    public static void main() {

        try {
            // Parse the XML file
            // Uncomment to start parsing categories
            startCategoryParsing();
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
            processDresden(document.getDocumentElement());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private static void processDresden(Element startElement) throws ParseException {
        DatabaseImporter dbImporter = new DatabaseImporter("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        DVD dvd = null; CD cd = null; Book book = null;
        NodeList nodes = startElement.getElementsByTagName("item");
        String storeName = startElement.getAttribute("name");
        String storeAddress = startElement.getAttribute("zip") + ", " + startElement.getAttribute("street");
        Store store = new Store(storeName, storeAddress);
        String datePattern = "yyyy-MM-dd";
        SimpleDateFormat  simpleDateFormat = new SimpleDateFormat(datePattern);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            String name = element.getAttribute("pgroup").trim().split("\\n")[0];
            switch (name) {
                case "DVD":
                    String id =  element.getAttribute("asin").trim();

                    Element actorsElement = (Element) element.getElementsByTagName("actors").item(0);
                    NodeList actorElements = actorsElement.getElementsByTagName("actor");
                    String[] actorsArr = new String[actorElements.getLength()];
                    for (int j = 0; j < actorElements.getLength(); j++) {
                        Node actor = actorElements.item(j);
                        actorsArr[j] = actor.getTextContent().trim().split("\\n")[0];
                    }
                    String actors = String.join(", ", actorsArr);

                    Element creatorsElement = (Element) element.getElementsByTagName("creators").item(0);
                    NodeList creatorElements = creatorsElement.getElementsByTagName("creator");
                    String[] creatorsArr = new String[creatorElements.getLength()];
                    for (int j = 0; j < creatorElements.getLength(); j++) {
                        Node creator = creatorElements.item(j);
                        creatorsArr[j] = creator.getTextContent().trim().split("\\n")[0];
                    }
                    String creators = String.join(", ", creatorsArr);

                    String director =  element.getElementsByTagName("director").item(0) != null ?
                            element.getElementsByTagName("director").item(0).getTextContent().trim().split("\\n")[0] :
                            "";

                    Element dvdspec = (Element) element.getElementsByTagName("dvdspec").item(0);
                    String format = dvdspec.getElementsByTagName("format").item(0).getTextContent().trim().split("\\n")[0];
                    Integer length = !(dvdspec.getElementsByTagName("runningtime").item(0).getTextContent().isEmpty()) ?
                            Integer.parseInt(dvdspec.getElementsByTagName("runningtime").item(0).getTextContent().trim()) :
                            null;
                    Integer regionCode = !(dvdspec.getElementsByTagName("regioncode").item(0).getTextContent().isEmpty()) ?
                            Integer.parseInt(dvdspec.getElementsByTagName("regioncode").item(0).getTextContent().trim().split("\\n")[0]) :
                            null;
                    dvd = new DVD(id, format, length, regionCode, actors, creators, director);
                    break;

                case "Music":
                    String pGroup = element.getAttribute("pgroup").trim().split("\\n")[0];
                    String musicTitle = element.getElementsByTagName("title").item(0).getTextContent().trim().split("\\n")[0];
                    String musicId = element.getAttribute("asin").trim().split("\\n")[0];
                    Element artistsElement = (Element) element.getElementsByTagName("artists").item(0);
                    NodeList artistElements = artistsElement.getElementsByTagName("artist");
                    String[] artistsArr = new String[artistElements.getLength()];
                    for (int j = 0; j < artistElements.getLength(); j++) {
                        Node actor = artistElements.item(j);
                        artistsArr[j] = actor.getTextContent().trim();
                    }
                    String artist = String.join(", ", artistsArr);

                    Element labelsElement = (Element) element.getElementsByTagName("labels").item(0);
                    NodeList labelElements = labelsElement.getElementsByTagName("label");
                    String[] labelsArr = new String[labelElements.getLength()];
                    for (int j = 0; j < labelElements.getLength(); j++) {
                        Node labelElement = labelElements.item(j);
                        labelsArr[j] = labelElement.getTextContent().trim();
                    }
                    String label = String.join(", ", labelsArr);

                    Element tracksElement = (Element) element.getElementsByTagName("tracks").item(0);
                    NodeList trackElements = tracksElement.getElementsByTagName("title");
                    String[] tracksArr = new String[trackElements.getLength()];
                    for (int j = 0; j < trackElements.getLength(); j++) {
                        Node loopElement = trackElements.item(j);
                        tracksArr[j] = loopElement.getTextContent().trim();
                    }
                    String titleList = String.join(", ", tracksArr);

                    Element musicSpec = (Element) element.getElementsByTagName("musicspec").item(0);
                    String dateText = musicSpec.getElementsByTagName("releasedate").item(0).getTextContent().trim();
                    Date date = dateText.isEmpty() ? null : simpleDateFormat.parse(dateText);
                    java.sql.Date actualDate = date == null ? null : new java.sql.Date(date.getTime());
                    cd = new CD(musicId, artist, label, actualDate, titleList);
                    break;

                case "Book":
                    String bookTitle = element.getElementsByTagName("title").item(0).getTextContent().trim().split("\\n")[0];
                    String bookId = element.getAttribute("asin").trim();

                    Element authorsElement = (Element) element.getElementsByTagName("authors").item(0);
                    NodeList authorElements = authorsElement.getElementsByTagName("author");
                    String[] authorsArr = new String[authorElements.getLength()];
                    for (int j = 0; j < authorElements.getLength(); j++) {
                        Node loopElement = authorElements.item(j);
                        authorsArr[j] = loopElement.getTextContent().trim();
                    }
                    String authorList = String.join(", ", authorsArr);
                    Element bookSpec = (Element) element.getElementsByTagName("bookspec").item(0);
                    Integer pages =  bookSpec.getElementsByTagName("pages").item(0).getTextContent().isEmpty() ? null: Integer.parseInt(bookSpec.getElementsByTagName("pages").item(0).getTextContent().trim());

                    Element publishersElement = (Element) element.getElementsByTagName("publishers").item(0);
                    NodeList publisherElements = publishersElement.getElementsByTagName("publisher");
                    String[] publishersArr = new String[publisherElements.getLength()];
                    for (int j = 0; j < publisherElements.getLength(); j++) {
                        Node loopElement = publisherElements.item(j);
                        publishersArr[j] = loopElement.getTextContent().trim();
                    }
                    String publisherList = String.join(", ", publishersArr);
                    String bookDateText = bookSpec.getElementsByTagName("publication").item(0).getTextContent().trim();
                    java.sql.Date bookDate = bookDateText.isEmpty() ? null : new java.sql.Date(simpleDateFormat.parse(bookDateText).getTime());

                    Element isbnElement = (Element) bookSpec.getElementsByTagName("isbn").item(0);
                    String isbn = isbnElement.getAttribute("val").trim();
                    book = new Book(bookId, authorList, publisherList, pages, bookDate, isbn);
                    break;
            }
            // PRODUCT CREATION
            String productId = element.getAttribute("asin").trim().split("\\n")[0];
            String rank = element.getAttribute("salesrank").trim().split("\\n")[0];
            Integer ranking = rank.isEmpty() ? null : Integer.parseInt(rank);
            Element titleElement = (Element) element.getElementsByTagName("title").item(0);
            String title1 = titleElement == null ? null : titleElement.getTextContent().trim().split("\\n")[0];
            //System.out.println(title1);

            Element details = (Element) element.getElementsByTagName("details").item(0);
            String image = details == null ? null : details.getAttribute("image").trim();

            // name == CATEGORY
            Product product = new Product(productId, title1, 0f, ranking, null, image, name);
            Element priceElement = (Element) element.getElementsByTagName("price").item(0);
            // Ist keinen Preis zu haben gültig?
            Float price = priceElement == null ? null : priceElement.getTextContent().trim().isEmpty() ? null : Float.parseFloat(priceElement.getTextContent().trim());
            boolean isAvailable = true;
            if (price == null) {
                isAvailable = false;
            }
            String condition = priceElement == null? null : priceElement.getAttribute("state").trim();

            ProductCatalog productCatalog = new ProductCatalog(storeName + storeAddress, productId, price, isAvailable, condition);
            /*
            dbImporter.InsertProduct(product);
            switch (name){
                case "DVD":
                    dbImporter.InsertDVD(dvd);
                    break;
                case "CD":
                    dbImporter.InsertCD(cd);
                    break;
                case "Bool":
                    dbImporter.InsertBook(book);
                    break;
            }
            dbImporter.InsertCatalog(productCatalog);*/
        }
    }
    private static void startCategoryParsing() {
        String categoryPath = "data/categories.xml";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(categoryPath));
            document.getDocumentElement().normalize();
            //test();
            processCategories(document.getDocumentElement(), null, 0);
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
    private static void test() throws IOException, SAXException, ParserConfigurationException {
        String xml = "<add job=\"351\">\n" +
                "    <tag>foobar</tag>\n" +
                "    <tag>foobar2</tag>\n" +
                "</add>";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        ByteArrayInputStream bis = new ByteArrayInputStream(xml.getBytes());
        Document doc = db.parse(bis);
        Node n = doc.getFirstChild();
        NodeList nl = n.getChildNodes();
        Node an,an2;

        for (int i=0; i < nl.getLength(); i++) {
            an = nl.item(i);
            if(an.getNodeType()==Node.ELEMENT_NODE) {
                NodeList nl2 = an.getChildNodes();

                for(int i2=0; i2<nl2.getLength(); i2++) {
                    an2 = nl2.item(i2);
                    // DEBUG PRINTS
                    System.out.println(an2.getNodeName() + ": type (" + an2.getNodeType() + "):");
                    if(an2.hasChildNodes()) System.out.println(an2.getFirstChild().getTextContent());
                    if(an2.hasChildNodes()) System.out.println(an2.getFirstChild().getNodeValue());
                    System.out.println(an2.getTextContent());
                    System.out.println(an2.getNodeValue());
                }
            }
        }
    }
    private static Map<Category, List<String>> processCategories(Element element, String parentCategoryId, int count) {
        System.out.println("MAINCAT");
        NodeList categories = element.getElementsByTagName("category");
        Map<Category, List<String>> map = new HashMap<>();
        for (int i = 0; i < categories.getLength(); i++) {
            Element categoryElement = (Element) categories.item(i);

            Node doubleHead = categoryElement.getParentNode().getParentNode();
            String categoryName = categoryElement.getTextContent().trim().split("\\n")[0]; // Extract the category name
            String categoryId = UUID.randomUUID().toString();
            Category category = null;
            if (doubleHead.getNodeType() == Node.DOCUMENT_NODE) {
                category = new Category(categoryName, categoryId, null, categoryElement);
            } else {
                Element parentElement = (Element) categoryElement.getParentNode();
                for (Map.Entry<Category, List<String>> mapElement : map.entrySet()) {
                    Element comparedElement = mapElement.getKey().node;
                    if (comparedElement == parentElement) {
                        String parentId = mapElement.getKey().id;
                        category = new Category(categoryName, categoryId, parentId, categoryElement);
                    }
                }
            }

            List<String> list = processItems(categoryElement);
            map.put(category, list);
        }
        int counter = 0;
        for (Map.Entry<Category, List<String>> mapElement : map.entrySet()) {
            if (counter > 20) break;
            System.out.println(mapElement.getKey().name + ": " + mapElement.getValue());
            counter++;
        }
            /*
            if (nl.getLength() >  0) {
                for (int j = 0; j < nl.getLength(); j++) {
                    Element newCategory = (Element) nl.item(j);
                    if (newCategory.getParentNode() != categoryElement) {
                        count++;
                        //System.out.println("Vati: " + newCategory.getParentNode().getTextContent() + "? " + ca);
                        continue;
                    }
                    List<Map<Category, List<String>>> returnValue = proccessSubCategory(mapList, newCategory, categoryId, count);
                    mapList.addAll(returnValue);
                }
            }
             */
        /*
        for(Map<Category, List<String>> map : mapList) {
                System.out.println(map.keySet().iterator().next().name + ": " + map.values().iterator().next().toString());
        }
        */
        return map;
    }

    private static List<Map<Category, List<String>>> proccessSubCategory(List<Map<Category, List<String>>> currentList, Element element, String parentCategoryId, int count) {
        List<Map<Category, List<String>>> mapList = new ArrayList<>();
        Element children = (Element) element.getChildNodes();
        NodeList categories = children.getElementsByTagName("category");
        for (int i = 0; i < categories.getLength(); i++) {
            Map<Category, List<String>> map = new HashMap<>();
            // Item Liste wird nicht benötigt, aber gleiche Struktur wird genutzt
            Element categoryElement = (Element) categories.item(i);
            String categoryId = UUID.randomUUID().toString();
            Element childrenCategories = (Element) element.getChildNodes();
            NodeList furtherCategories = childrenCategories.getElementsByTagName("category");
            String categoryName = categoryElement.getTextContent().trim().split("\\n")[0];
            Category category = new Category(categoryName, categoryId, parentCategoryId);
            List<String> list = new ArrayList<>();
            map.put(category, list);
            mapList.add(map);
            for (int j = 0; j < furtherCategories.getLength(); j++) {
                count++;
                Element newCategory = (Element) furtherCategories.item(j);
                if (newCategory.getParentNode() != categoryElement) {
                    continue;
                }
            }
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
