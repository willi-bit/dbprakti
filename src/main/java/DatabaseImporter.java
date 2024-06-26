import java.sql.*;
import java.util.*;
import java.io.*;

public class DatabaseImporter {

    private final String url;
    private final String user;
    private final String password;
    public Map<String, Integer> ErrorCount = new HashMap<>();

    public File errors = new File("data/errors.txt");
    public FileWriter writer;

    /**
     * Constructor
     *
     * @param url  local postgres database url
     * @param user local postgres user
     * @param pw   local postgres pw
     */
    public DatabaseImporter(String url, String user, String pw) {
        this.url = url;
        this.user = user;
        this.password = pw;
        try {
            errors.delete();
            errors.createNewFile();
            writer = new FileWriter(errors, true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertCategory(Category category) {
        String insertCategorySQL = "INSERT INTO category (name, categoryid, parentcategory) VALUES (?,?,?)";
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertCategorySQL)) {
            if (category.parent != null) {
                preparedStatement.setString(1, category.name);
                preparedStatement.setString(2, category.id);
                preparedStatement.setString(3, category.parent);
            } else {
                preparedStatement.setString(1, category.name);
                preparedStatement.setString(2, category.id);
                preparedStatement.setNull(3, Types.VARCHAR);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            try {
                writer.write(e.getMessage());
                if (!ErrorCount.containsKey(e.getSQLState())) {
                    ErrorCount.put(e.getSQLState(), 1);
                } else {
                    ErrorCount.put(e.getSQLState(), ErrorCount.get(e.getSQLState()) + 1);
                }
            } catch (IOException ioe) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * inserts productcategories relation
     *
     * @param category category
     * @param products all products with category
     */
    public void InsertProductCategoryRelation(Category category, List<String> products) {

        String insertRelationSQL = "INSERT INTO productcategories (product, category) VALUES (?,?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertRelationSQL)) {

            for (String product : products) {

                preparedStatement.setString(1, product);
                preparedStatement.setString(2, category.id);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();

        } catch (SQLException e) {
            try {
                writer.write(e.getMessage());
                if (!ErrorCount.containsKey(e.getSQLState())) {
                    ErrorCount.put(e.getSQLState(), 1);
                } else {
                    ErrorCount.put(e.getSQLState(), ErrorCount.get(e.getSQLState()) + 1);
                }
            } catch (IOException ioe) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * inserts store
     *
     * @param store store
     */
    public void InsertStore(Store store) {

        String insertStoresSQL = "INSERT INTO store (name, address, storeid) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertStoresSQL)) {

            preparedStatement.setString(1, store.name);
            preparedStatement.setString(2, store.address);
            preparedStatement.setString(3, store.id);

            preparedStatement.execute();

        } catch (SQLException e) {
            try {
                writer.write(e.getMessage());
                if (!ErrorCount.containsKey(e.getSQLState())) {
                    ErrorCount.put(e.getSQLState(), 1);
                } else {
                    ErrorCount.put(e.getSQLState(), ErrorCount.get(e.getSQLState()) + 1);
                }
            } catch (IOException ioe) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void InsertPerson(Person person){

        String insertPersonSQL = "INSERT INTO person (name) VALUES (?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL)){

            preparedStatement.setString(1, person.name);
            preparedStatement.executeUpdate();

        } catch(SQLException e){
            try{
                writer.write(e.getMessage());
                if (!ErrorCount.containsKey(e.getSQLState())){
                    ErrorCount.put(e.getSQLState(), 1);
                } else {
                    ErrorCount.put(e.getSQLState(), ErrorCount.get(e.getSQLState()) + 1);
                }
            }catch(IOException ioe){
                System.out.println(e.getMessage());
            }
        }
    }

    public int getPersonId(String name){

        String getPersonIdSQL = "SELECT personid FROM person WHERE name = ?";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(getPersonIdSQL)){

            preparedStatement.setString(1, name);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return resultSet.getInt("personid");
                }
            }
        } catch(SQLException e){
            try{
                writer.write(e.getMessage());
                if (!ErrorCount.containsKey(e.getSQLState())){
                    ErrorCount.put(e.getSQLState(), 1);
                } else {
                    ErrorCount.put(e.getSQLState(), ErrorCount.get(e.getSQLState()) + 1);
                }
            }catch(IOException ioe){
                System.out.println(e.getMessage());
            }
        }
        return 0;
    }

    /**
     * inserts book
     *
     * @param book book
     */
    public void InsertBook(Book book) {

        String insertBookSQL = "INSERT INTO book (author, pages, releasedate, isbn, publisher, productid) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertBookSQL)) {

            preparedStatement.setInt(1, getPersonId(book.author));
            if (book.pages != null) {
                preparedStatement.setInt(2, book.pages);
            } else {
                preparedStatement.setNull(2, java.sql.Types.INTEGER);
            }
            preparedStatement.setDate(3, book.releaseDate);
            preparedStatement.setString(4, book.ISBN);
            preparedStatement.setString(5, book.publisher);
            preparedStatement.setString(6, book.id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            try {
                writer.write(e.getMessage());
                if (!ErrorCount.containsKey(e.getSQLState())) {
                    ErrorCount.put(e.getSQLState(), 1);
                } else {
                    ErrorCount.put(e.getSQLState(), ErrorCount.get(e.getSQLState()) + 1);
                }
            } catch (IOException ioe) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * inserts dvd
     *
     * @param dvd dvd
     */
    public void InsertDVD(DVD dvd) {

        String insertDVDSQL = "INSERT INTO dvd (format, length, regioncode, actors, creator, director, productid) VALUES (?,?,?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertDVDSQL)) {

            preparedStatement.setString(1, dvd.format);
            if (dvd.length != null) {
                preparedStatement.setInt(2, dvd.length);
            } else {
                preparedStatement.setNull(2, java.sql.Types.INTEGER);
            }
            if (dvd.regionCode != null) {
                preparedStatement.setInt(3, dvd.regionCode);
            } else {
                preparedStatement.setNull(3, java.sql.Types.INTEGER);
            }
            String[] actorsSplit = dvd.actors.split(",");
            Integer[] actorIds = new Integer[actorsSplit.length];
            for(int i = 0; i < actorIds.length; i++){
                actorIds[i] = getPersonId(actorsSplit[i]);
            }
            Array actors = connection.createArrayOf("integer", actorIds);
            preparedStatement.setArray(4, actors);
            preparedStatement.setInt(5, getPersonId(dvd.creator));
            preparedStatement.setInt(6, getPersonId(dvd.director));
            preparedStatement.setString(7, dvd.id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            try {
                writer.write(e.getMessage());
                if (!ErrorCount.containsKey(e.getSQLState())) {
                    ErrorCount.put(e.getSQLState(), 1);
                } else {
                    ErrorCount.put(e.getSQLState(), ErrorCount.get(e.getSQLState()) + 1);
                }
            } catch (IOException ioe) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * inserts cd
     *
     * @param cd cd
     */
    public void InsertCD(CD cd) {

        String insertCDSQL = "INSERT INTO cd (artist, label, releasedate, titlelist, productid) VALUES (?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertCDSQL)) {

            preparedStatement.setInt(1, getPersonId(cd.artist));
            preparedStatement.setString(2, cd.label);
            preparedStatement.setDate(3, cd.releaseDate);
            preparedStatement.setString(4, cd.titleList);
            preparedStatement.setString(5, cd.id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            try {
                writer.write(e.getMessage());
                if (!ErrorCount.containsKey(e.getSQLState())) {
                    ErrorCount.put(e.getSQLState(), 1);
                } else {
                    ErrorCount.put(e.getSQLState(), ErrorCount.get(e.getSQLState()) + 1);
                }
            } catch (IOException ioe) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * inserts product
     *
     * @param entry product + all similar products
     */
    public void InsertProduct(ProductSimilars entry) {

        String insertProductSQL = "INSERT INTO product (title, rating, rank, picture, productid) VALUES (?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)) {

            Product product = entry.product;
            preparedStatement.setString(1, product.title);
            preparedStatement.setFloat(2, product.rating);
            if (product.rank != null) {
                preparedStatement.setInt(3, product.rank);
            } else {
                preparedStatement.setNull(3, java.sql.Types.INTEGER);
            }
            preparedStatement.setString(4, product.picture);
            preparedStatement.setString(5, product.id);
            preparedStatement.executeUpdate();

            InsertSimilarProducts(entry);

        } catch (SQLException e) {
            try {
                writer.write(e.getMessage());
                if (!ErrorCount.containsKey(e.getSQLState())) {
                    ErrorCount.put(e.getSQLState(), 1);
                } else {
                    ErrorCount.put(e.getSQLState(), ErrorCount.get(e.getSQLState()) + 1);
                }
            } catch (IOException ioe) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * inserts similarproduct relation
     *
     * @param entry product + all similar products
     */
    public void InsertSimilarProducts(ProductSimilars entry) {

        String insertProductSQL = "INSERT INTO similarproduct (product1, product2) VALUES (?,?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)) {

            for (String similarProduct : entry.similars) {
                preparedStatement.setString(1, entry.product.id);
                preparedStatement.setString(2, similarProduct);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            try {
                writer.write(e.getMessage());
                if (!ErrorCount.containsKey(e.getSQLState())) {
                    ErrorCount.put(e.getSQLState(), 1);
                } else {
                    ErrorCount.put(e.getSQLState(), ErrorCount.get(e.getSQLState()) + 1);
                }
            } catch (IOException ioe) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * inserts productcatalog
     *
     * @param catalog productcatalog
     */
    public void InsertCatalog(ProductCatalog catalog) {

        String insertCatalogSQL = "INSERT INTO productcatalog (store, product, price, available, condition) VALUES (?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertCatalogSQL)) {

            preparedStatement.setString(1, catalog.storeId);
            preparedStatement.setString(2, catalog.productId);
            if (catalog.price != null) {
                preparedStatement.setFloat(3, catalog.price);
            } else {
                preparedStatement.setNull(3, java.sql.Types.FLOAT);
            }
            preparedStatement.setBoolean(4, catalog.isAvailable);
            preparedStatement.setString(5, catalog.condition);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            try {
                writer.write(e.getMessage());
                if (!ErrorCount.containsKey(e.getSQLState())) {
                    ErrorCount.put(e.getSQLState(), 1);
                } else {
                    ErrorCount.put(e.getSQLState(), ErrorCount.get(e.getSQLState()) + 1);
                }
            } catch (IOException ioe) {
                System.out.println(e.getMessage());
            }
        }

    }

    /**
     * inserts review
     *
     * @param review review
     */
    public void InsertReviews(Review review) {

        String insertREviewsSQL = "INSERT INTO review (reviewid, product, stars, summary, review, helpful, username, customer) VALUES (?,?,?,?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertREviewsSQL)) {

            preparedStatement.setString(1, review.id);
            preparedStatement.setString(2, review.product);
            preparedStatement.setInt(3, review.stars);
            preparedStatement.setString(4, review.summary);
            preparedStatement.setString(5, review.content);
            preparedStatement.setInt(6, review.helpful);
            preparedStatement.setString(7, review.username);
            preparedStatement.setNull(8, java.sql.Types.VARCHAR);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            try {
                writer.write(e.getMessage());
                if (!ErrorCount.containsKey(e.getSQLState())) {
                    ErrorCount.put(e.getSQLState(), 1);
                } else {
                    ErrorCount.put(e.getSQLState(), ErrorCount.get(e.getSQLState()) + 1);
                }
            } catch (IOException ioe) {
                System.out.println(e.getMessage());
            }
        }
    }
}
