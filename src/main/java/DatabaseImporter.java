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

        String insertCategorySQL = "INSERT INTO category (name, parentcategoryid, categoryid) VALUES (?,?,?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertCategorySQL)){

            for (Map<Category, List<String>> entry : Data) {
                for (Map.Entry<Category, List<String>> categoryEntry : entry.entrySet()) {
                    Category category = categoryEntry.getKey();
                    preparedStatement.setString(1, category.name);
                    preparedStatement.setObject(2, category.parent);
                    preparedStatement.setObject(3, category.id);
                    preparedStatement.addBatch();
                }
            }
            preparedStatement.executeBatch();

        } catch(SQLException e){
           System.out.println(e.getMessage());
        }

    }

    public void InsertStore(Store store){

        String insertStoresSQL = "INSERT INTO store (name, address, storeid) VALUES (?, ?, ?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertStoresSQL)){

            preparedStatement.setString(1, store.name);
            preparedStatement.setString(2, store.address);
            preparedStatement.setObject(3, store.id);

            preparedStatement.execute();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void InsertBook(Book book){

        String insertBookSQL = "INSERT INTO book (author, pages, releasedate, isbn, publisher, productid) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertBookSQL)){

            preparedStatement.setString(1, book.author);
            preparedStatement.setInt(2, book.pages);
            preparedStatement.setDate(3, book.releaseDate);
            preparedStatement.setString(4, book.ISBN);
            preparedStatement.setString(5, book.publisher);
            preparedStatement.setObject(6, book.id);

            preparedStatement.execute();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void InsertDVD(DVD dvd){

        String insertDVDSQL = "INSERT INTO dvd (format, length, regioncode, actors, creator, director, productid) VALUES (?,?,?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertDVDSQL)){

            preparedStatement.setString(1, dvd.format);
            preparedStatement.setInt(2, dvd.length);
            preparedStatement.setInt(3, dvd.regionCode);
            preparedStatement.setString(4, dvd.actors);
            preparedStatement.setString(5, dvd.creator);
            preparedStatement.setString(6, dvd.director);
            preparedStatement.setObject(7, dvd.id);

            preparedStatement.execute();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void InsertCD(CD cd){

        String insertCDSQL = "INSERT INTO cd (artist, label, releasedate, titlelist, productid) VALUES (?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertCDSQL)){

            preparedStatement.setString(1, cd.artist);
            preparedStatement.setString(2, cd.label);
            preparedStatement.setDate(3, cd.releaseDate);
            preparedStatement.setString(4, cd.titleList);
            preparedStatement.setObject(5, cd.id);

            preparedStatement.execute();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void InsertProduct(Product product){

        String insertProductSQL = "INSERT INTO product (title, rating, rank, productnr, picture, category, productid) VALUES (?,?,?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)){

            preparedStatement.setString(1, product.title);
            preparedStatement.setFloat(2, product.rating);
            preparedStatement.setInt(3, product.rank);
            preparedStatement.setString(4, product.nr);
            preparedStatement.setString(5, product.picture);
            preparedStatement.setObject(6, product.category);
            preparedStatement.setObject(7, product.id);

            preparedStatement.execute();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void InsertCatalog(Store store, List<Product> catalog){

        String insertCatalogSQL = "INSERT INTO productcatalog (store, product, price, available, condition) VALUES (?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertCatalogSQL)){

            InsertStore(store);
            for(Product product : catalog){
                InsertProduct(product);
                preparedStatement.setObject(1, product.id);
                preparedStatement.setObject(2, store.id);
                preparedStatement.setFloat(3, (float)10.0);
                preparedStatement.setBoolean(4, true);
                preparedStatement.setString(5, "good");

                preparedStatement.execute();
            }

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public void InsertReview(Review review){

        String insertReviewSQL = "INSERT INTO review (reviewid, customer, product, stars, summary, review, helpful, username) VALUES (?,?,?,?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertReviewSQL)){

            preparedStatement.setObject(1, review.id);
            preparedStatement.setObject(2, review.customer);
            preparedStatement.setObject(3, review.product);
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
