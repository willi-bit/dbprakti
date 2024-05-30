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
}
