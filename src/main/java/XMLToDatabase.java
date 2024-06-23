import org.w3c.dom.*;
import org.w3c.dom.ls.LSOutput;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class XMLToDatabase {

    private Map<Category, List<String>> categories;
    List<ProductSimilars> products = new ArrayList<>();

    private Set<String> personSet = new HashSet<>();

    public Set<String> getPersons(){
        return personSet;
    }

    public Map<Category, List<String>> getCategories() {
        return categories;
    }

    public void main() {
        try {
            // Parse the XML file
            // Uncomment to start parsing categories
            int counto = 0;
            for (ProductSimilars p : products) {
                System.out.println(p.product.title + ": " + p.similars.size());
            }
            startCategoryParsing();
            startLeipzigParsing(this.products);
            startDresdenParsing(this.products);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param products List of products in relation to a list of their similars
     * @throws Exception for parsing the XML DOM, shouldn't happen
     */
    private void startDresdenParsing(List<ProductSimilars> products) throws Exception {
        String dresdenPath = "data/dresden.xml";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(dresdenPath));
            processStore(document.getDocumentElement(), products);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param products List of products in relation to a list of their similars
     * @throws Exception for parsing the XML DOM, shouldn't happen
     */
    private void startLeipzigParsing(List<ProductSimilars> products) throws Exception {
        String leipzigPath = "data/leipzig_transformed.xml";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(leipzigPath));
            processStore(document.getDocumentElement(), products);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * parses xml
     *
     * @param startElement start element of XML DOM tree
     * @param products     List of products in relation to a list of their similars
     * @throws ParseException
     * @throws UnsupportedEncodingException
     */
    private void processStore(Element startElement, List<ProductSimilars> products) throws ParseException, UnsupportedEncodingException {
        DatabaseImporter dbImporter = new DatabaseImporter("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        DVD dvd = null;
        CD cd = null;
        Book book = null;
        List<Book> books = new ArrayList<>();
        List<CD> cds = new ArrayList<>();
        List<DVD> dvds = new ArrayList<>();
        List<ProductCatalog> catalogs = new ArrayList<>();
        NodeList nodes = startElement.getElementsByTagName("item");
        String storeName = startElement.getAttribute("name");
        String storeAddress = startElement.getAttribute("zip") + ", " + startElement.getAttribute("street");
        Store store = new Store(storeName, storeAddress);
        dbImporter.InsertStore(store);
        String datePattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        List<String> similars = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            String name = element.getAttribute("pgroup").trim().split("\\n")[0];
            if (name == null || name.isEmpty() || name.getBytes("utf-8").length == 0) {
                String similarId = element.getAttribute("asin").trim();
                similars.add(similarId);
                continue;
            }
            // case for handling similars in Leipzig
            Element similarBaseElement = (Element) element.getElementsByTagName("similars").item(0);
            NodeList similarElements = similarBaseElement.getElementsByTagName("sim_product");
            if (similarElements.getLength() > 0) {
                for (int j = 0; j < similarElements.getLength(); j++) {
                    Element similarElement = (Element) similarElements.item(j);
                    Element asinElement = (Element) similarElement.getElementsByTagName("asin").item(0);
                    String asin = asinElement.getTextContent().trim();
                    similars.add(asin);
                }
            }
            switch (name) {
                case "DVD":
                    String id = element.getAttribute("asin").trim();

                    Element actorsElement = (Element) element.getElementsByTagName("actors").item(0);
                    NodeList actorElements = actorsElement.getElementsByTagName("actor");
                    String[] actorsArr = new String[actorElements.getLength()];
                    for (int j = 0; j < actorElements.getLength(); j++) {
                        Node actor = actorElements.item(j);
                        String elementName = actor.getTextContent().trim().split("\\n")[0];
                        if(elementName.isEmpty()) {
                            elementName = actor.getAttributes().item(0).getTextContent().trim();
                        }
                        actorsArr[j] = elementName;
                        personSet.add(elementName);
                    }
                    String actors = String.join(", ", actorsArr);

                    Element creatorsElement = (Element) element.getElementsByTagName("creators").item(0);
                    NodeList creatorElements = creatorsElement.getElementsByTagName("creator");
                    String[] creatorsArr = new String[creatorElements.getLength()];
                    for (int j = 0; j < creatorElements.getLength(); j++) {
                        Node creator = creatorElements.item(j);
                        String elementName = creator.getTextContent().trim().split("\\n")[0];
                        if(elementName.isEmpty()) {
                            elementName = creator.getAttributes().item(0).getTextContent().trim();
                        }
                        creatorsArr[j] = elementName;
                        personSet.add(elementName);
                    }
                    String creators = String.join(", ", creatorsArr);

                    String director = element.getElementsByTagName("director").item(0) != null ?
                            element.getElementsByTagName("director").item(0).getTextContent().trim().split("\\n")[0] :
                            "";
                    personSet.add(director);

                    Element dvdspec = (Element) element.getElementsByTagName("dvdspec").item(0);
                    String format = dvdspec.getElementsByTagName("format").item(0).getTextContent().trim().split("\\n")[0];
                    Integer length = !(dvdspec.getElementsByTagName("runningtime").item(0).getTextContent().isEmpty()) ?
                            Integer.parseInt(dvdspec.getElementsByTagName("runningtime").item(0).getTextContent().trim()) :
                            null;
                    Integer regionCode = !(dvdspec.getElementsByTagName("regioncode").item(0).getTextContent().isEmpty()) ?
                            Integer.parseInt(dvdspec.getElementsByTagName("regioncode").item(0).getTextContent().trim().split("\\n")[0]) :
                            null;
                    dvd = new DVD(id, format, length, regionCode, actors, creators, director);
                    dvds.add(dvd);
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
                        String elementName = actor.getTextContent().trim().split("\\n")[0];
                        if(elementName.isEmpty()) {
                            elementName = actor.getAttributes().item(0).getTextContent().trim();
                        }
                        artistsArr[j] = elementName;
                        personSet.add(elementName);
                    }
                    String artist = String.join(", ", artistsArr);
                    if (artist.length() < 2) {
                        artist = null;
                    }

                    Element labelsElement = (Element) element.getElementsByTagName("labels").item(0);
                    NodeList labelElements = labelsElement.getElementsByTagName("label");
                    String[] labelsArr = new String[labelElements.getLength()];
                    for (int j = 0; j < labelElements.getLength(); j++) {
                        Node labelElement = labelElements.item(j);
                        String elementName = labelElement.getTextContent().trim().split("\\n")[0];
                        if(elementName.isEmpty()) {
                            elementName = labelElement.getAttributes().item(0).getTextContent().trim();
                        }
                        labelsArr[j] = elementName;
                    }
                    String label = String.join(", ", labelsArr);


                    Element tracksElement = (Element) element.getElementsByTagName("tracks").item(0);
                    NodeList trackElements = tracksElement.getElementsByTagName("title");
                    String[] tracksArr = new String[trackElements.getLength()];
                    for (int j = 0; j < trackElements.getLength(); j++) {
                        Node loopElement = trackElements.item(j);
                        String elementName = loopElement.getTextContent().trim().split("\\n")[0];
                        if(elementName.isEmpty()) {
                            elementName = loopElement.getAttributes().item(0).getTextContent().trim();
                        }
                        tracksArr[j] = elementName;
                    }
                    String titleList = String.join(", ", tracksArr);


                    Element musicSpec = (Element) element.getElementsByTagName("musicspec").item(0);
                    String dateText = musicSpec.getElementsByTagName("releasedate").item(0).getTextContent().trim();
                    Date date = dateText.isEmpty() ? null : simpleDateFormat.parse(dateText);
                    java.sql.Date actualDate = date == null ? null : new java.sql.Date(date.getTime());
                    cd = new CD(musicId, artist, label, actualDate, titleList);
                    cds.add(cd);
                    break;

                case "Book":
                    String bookTitle = element.getElementsByTagName("title").item(0).getTextContent().trim().split("\\n")[0];
                    String bookId = element.getAttribute("asin").trim();

                    Element authorsElement = (Element) element.getElementsByTagName("authors").item(0);
                    NodeList authorElements = authorsElement.getElementsByTagName("author");
                    String[] authorsArr = new String[authorElements.getLength()];
                    for (int j = 0; j < authorElements.getLength(); j++) {
                        Node loopElement = authorElements.item(j);
                        String elementName = loopElement.getTextContent().trim().split("\\n")[0];
                        if(elementName.isEmpty()) {
                            elementName = loopElement.getAttributes().item(0).getTextContent().trim();
                        }
                        authorsArr[j] = elementName;
                        personSet.add(elementName);

                    }
                    String authorList = String.join(", ", authorsArr);
                    Element bookSpec = (Element) element.getElementsByTagName("bookspec").item(0);
                    Integer pages = bookSpec.getElementsByTagName("pages").item(0).getTextContent().isEmpty() ? null : Integer.parseInt(bookSpec.getElementsByTagName("pages").item(0).getTextContent().trim());

                    Element publishersElement = (Element) element.getElementsByTagName("publishers").item(0);
                    NodeList publisherElements = publishersElement.getElementsByTagName("publisher");
                    String[] publishersArr = new String[publisherElements.getLength()];
                    for (int j = 0; j < publisherElements.getLength(); j++) {
                        Node loopElement = publisherElements.item(j);
                        String elementName = loopElement.getTextContent().trim().split("\\n")[0];
                        if(elementName.isEmpty()) {
                            elementName = loopElement.getAttributes().item(0).getTextContent().trim();
                        }
                        publishersArr[j] = elementName;
                        personSet.add(elementName);
                    }
                    String publisherList = String.join(", ", publishersArr);
                    Element bookDateElement = (Element) bookSpec.getElementsByTagName("publication").item(0);
                    String bookDateText = bookDateElement.getAttribute("date").trim();
                    java.sql.Date bookDate = bookDateText.isEmpty() ? null : new java.sql.Date(simpleDateFormat.parse(bookDateText).getTime());

                    Element isbnElement = (Element) bookSpec.getElementsByTagName("isbn").item(0);
                    String isbn = isbnElement.getAttribute("val").trim();
                    book = new Book(bookId, authorList, publisherList, pages, bookDate, isbn);
                    books.add(book);
                    break;
            }
            // PRODUCT CREATION
            String productId = element.getAttribute("asin").trim().split("\\n")[0];
            String rank = element.getAttribute("salesrank").trim().split("\\n")[0];
            Integer ranking = rank.isEmpty() ? null : Integer.parseInt(rank);
            Element titleElement = (Element) element.getElementsByTagName("title").item(0);
            String title1 = titleElement == null ? null : titleElement.getTextContent().trim().split("\\n")[0];

            Element details = (Element) element.getElementsByTagName("details").item(0);
            String image = details == null ? null : details.getAttribute("image").trim();
            if (!products.isEmpty()) {

                ProductSimilars ps = products.get(products.size() - 1);
                List<String> al = new ArrayList<>(similars);
                ps.similars = al;
            }
            Product product = new Product(productId, title1, 0f, ranking, image);
            List<String> nl = new ArrayList<>();
            ProductSimilars newProduct = new ProductSimilars(product, nl);
            products.add(newProduct);
            Element priceElement = (Element) element.getElementsByTagName("price").item(0);
            // Ist keinen Preis zu haben gültig?
            Float price = priceElement == null ? null : priceElement.getTextContent().trim().isEmpty() ? null : Float.parseFloat(priceElement.getTextContent().trim());
            boolean isAvailable = true;
            if (price == null) {
                isAvailable = false;
            }
            String condition = priceElement == null ? null : priceElement.getAttribute("state").trim();

            ProductCatalog productCatalog = new ProductCatalog(store.id, productId, price, isAvailable, condition);
            similars.clear();
            catalogs.add(productCatalog);
        }

        for(String person : personSet) {
            dbImporter.InsertPerson(new Person(person));
        }
        for(ProductSimilars sim : products){
            dbImporter.InsertProduct(sim);
        }
        for(DVD d : dvds){
            dbImporter.InsertDVD(d);
        }for(CD c : cds){
            dbImporter.InsertCD(c);
        }
        for(Book b : books){
            dbImporter.InsertBook(b);
        }
        for(ProductCatalog cat : catalogs){
            dbImporter.InsertCatalog(cat);
        }
    }

    private void startCategoryParsing() {
        String categoryPath = "data/categories.xml";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(categoryPath));
            document.getDocumentElement().normalize();
            this.categories = processCategories(document.getDocumentElement(), null, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * parses xml
     *
     * @param element
     * @param parentCategoryId id of parent category
     * @param count
     * @return map of categories and associated sub-categories
     */
    private Map<Category, List<String>> processCategories(Element element, String parentCategoryId, int count) {
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
            counter++;
        }
        return map;
    }

    private List<String> processItems(Element categoryElement) {
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
