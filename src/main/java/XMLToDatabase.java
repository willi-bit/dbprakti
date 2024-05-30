import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
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
            processDresden(document.getDocumentElement());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private static void processDresden(Element startElement) throws ParseException {
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
                    String title = element.getElementsByTagName("title").item(0).getTextContent().trim().split("\\n")[0];

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
                    DVD dvd = new DVD(id, format, length, regionCode, actors, creators, director);
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
                    CD cd = new CD(musicId, artist, label, actualDate, titleList);
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
                    Book book = new Book(bookId, authorList, publisherList, pages, bookDate, isbn);
                    break;
            }
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
