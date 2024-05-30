import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.sql.*;
import java.util.*;

public class DatabaseImporter {

    private List<Map<Category, List<String>>> Data;

    public DatabaseImporter(List<Map<Category, List<String>>> Data) {
        this.Data = Data;
    }
}
