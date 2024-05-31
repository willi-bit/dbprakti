import javax.xml.catalog.Catalog;
import java.sql.*;
import java.util.*;

public class DatabaseImporter {

    private final String url;
    private final String user;
    private final String password;

    public DatabaseImporter(String url, String user, String pw) {
        this.url = url;
        this.user = user;
        this.password = pw;
    }

    public void InsertCategories(List<Map<Category, List<String>>> Data){

        if (Data == null){return;}
        String insertCategorySQL = "INSERT INTO category (name, categoryid) VALUES (?,?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertCategorySQL)){
            List<Category> subCategories = new ArrayList<>();
            for (Map<Category, List<String>> entry : Data) {
                for (Map.Entry<Category, List<String>> categoryEntry : entry.entrySet()) {
                    Category category = categoryEntry.getKey();
                    if(category.parent == null){
                        preparedStatement.setString(1, category.name);
                        preparedStatement.setString(2, category.id);
                        preparedStatement.addBatch();
                    } else {
                        subCategories.add(category);
                    }
                }
            }
            preparedStatement.executeBatch();

            for (Category category : subCategories) {
                InsertSubCategories(category);
            }

        } catch(SQLException e){
           System.out.println(e.getMessage());
        }

    }

    public void InsertSubCategories(Category category){

        String insertCategorySQL = "INSERT INTO subcategory (name, parentcategoryid, subcategoryid) VALUES (?,?,?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertCategorySQL)){

            preparedStatement.setString(1, category.name);
            preparedStatement.setString(2, category.parent);
            preparedStatement.setString(3, category.id);

            preparedStatement.execute();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public void InsertStore(Store store){

        if (store == null){return;}
        String insertStoresSQL = "INSERT INTO store (name, address, storeid) VALUES (?, ?, ?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertStoresSQL)){

            preparedStatement.setString(1, store.name);
            preparedStatement.setString(2, store.address);
            preparedStatement.setString(3, store.id);

            preparedStatement.execute();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void InsertBook(Book book){

        if (book == null){return;}
        String insertBookSQL = "INSERT INTO book (author, pages, releasedate, isbn, publisher, productid) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertBookSQL)){

            preparedStatement.setString(1, book.author);
            preparedStatement.setInt(2, book.pages);
            preparedStatement.setDate(3, book.releaseDate);
            preparedStatement.setString(4, book.ISBN);
            preparedStatement.setString(5, book.publisher);
            preparedStatement.setString(6, book.id);

            preparedStatement.execute();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void InsertDVD(DVD dvd){

        if (dvd == null){return;}
        String insertDVDSQL = "INSERT INTO dvd (format, length, regioncode, actors, creator, director, productid) VALUES (?,?,?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertDVDSQL)){

            preparedStatement.setString(1, dvd.format);
            preparedStatement.setInt(2, dvd.length);
            if (dvd.regionCode != null){
                preparedStatement.setInt(3, dvd.regionCode);
            } else {
                preparedStatement.setNull(3, java.sql.Types.INTEGER);
            }
            preparedStatement.setString(4, dvd.actors);
            preparedStatement.setString(5, dvd.creator);
            preparedStatement.setString(6, dvd.director);
            preparedStatement.setString(7, dvd.id);

            preparedStatement.execute();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void InsertCD(CD cd){

        if (cd == null){return;}
        String insertCDSQL = "INSERT INTO cd (artist, label, releasedate, titlelist, productid) VALUES (?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertCDSQL)){

            preparedStatement.setString(1, cd.artist);
            preparedStatement.setString(2, cd.label);
            preparedStatement.setDate(3, cd.releaseDate);
            preparedStatement.setString(4, cd.titleList);
            preparedStatement.setString(5, cd.id);

            preparedStatement.execute();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void InsertProduct(Product product){

        if (product == null){return;}
        String insertProductSQL = "INSERT INTO product (title, rating, rank, productnr, picture, category, productid) VALUES (?,?,?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)){

            preparedStatement.setString(1, product.title);
            preparedStatement.setFloat(2, product.rating);
            if (product.rank != null){
                preparedStatement.setInt(3, product.rank);
            } else {
                preparedStatement.setNull(3, java.sql.Types.INTEGER);
            }
            preparedStatement.setString(4, product.nr);
            preparedStatement.setString(5, product.picture);
            preparedStatement.setString(6, product.category);
            preparedStatement.setString(7, product.id);

            preparedStatement.execute();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void InsertCatalog(ProductCatalog catalog){

        if (catalog == null){return;}
        String insertCatalogSQL = "INSERT INTO productcatalog (store, product, price, available, condition) VALUES (?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertCatalogSQL)){
            preparedStatement.setString(1, catalog.storeId);
            preparedStatement.setString(2, catalog.productId);
            if(catalog.price != null){
                preparedStatement.setFloat(3, catalog.price);
            } else {
                preparedStatement.setNull(3, java.sql.Types.FLOAT);
            }
            preparedStatement.setBoolean(4, catalog.isAvailable);
            preparedStatement.setString(5, catalog.condition);

            preparedStatement.execute();


        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public void InsertReview(Review review){
        if (review == null){return;}
        String insertReviewSQL = "INSERT INTO review (reviewid, customer, product, stars, summary, review, helpful, username) VALUES (?,?,?,?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertReviewSQL)){

            preparedStatement.setString(1, review.id);
            preparedStatement.setString(2, review.customer);
            preparedStatement.setString(3, review.product);
            preparedStatement.setInt(4, review.stars);
            preparedStatement.setString(5, review.summary);
            preparedStatement.setString(6, review.review);
            preparedStatement.setInt(6, review.helpful);
            preparedStatement.setString(7, review.username);

            preparedStatement.execute();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
