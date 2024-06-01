import java.io.*;
import java.sql.*;
import java.util.*;
import org.apache.ibatis.jdbc.ScriptRunner;

public class main {
    public static void main(String[] args) {
        System.out.println("STARTING");
        try {

            Connection dbConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
            ScriptRunner dbCreator = new ScriptRunner(dbConnection);
            Reader scriptReader = new BufferedReader(new FileReader("src/database/createDatabase.sql"));
            dbCreator.runScript(scriptReader);

            XMLToDatabase xmlToDatabase = new XMLToDatabase();
            DatabaseImporter dbImporter = new DatabaseImporter("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");

            xmlToDatabase.main();
            Map<Category, List<String>> categories = xmlToDatabase.getCategories();

            dbImporter.InsertCategories(categories);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
