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

            preparedStatement.setString(1, Data.get(0).keySet().iterator().next().name);
            preparedStatement.setObject(2, Data.get(0).keySet().iterator().next().parent);
            preparedStatement.setObject(3, Data.get(0).keySet().iterator().next().id);

        } catch(SQLException e){
           System.out.println(e.getMessage());
        }

    }

    public void InsertStores(){

        String insertStoresSQL = "INSERT INTO store (name, address, storeid) VALUES (?, ?, ?)";

        try(Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
            PreparedStatement preparedStatement = connection.prepareStatement(insertStoresSQL)){



        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
