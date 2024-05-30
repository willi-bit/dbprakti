public class main {
    public static void main(String[] args) {
        System.out.println("STARTING");
        try {
            //importData.importDataFromFile("data/categories.xml");
            XMLToDatabase xmlToDatabase = new XMLToDatabase();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
