import java.util.Date;
import java.util.UUID;

public class CD {
    public String id;
    public String artist;
    public String label;
    public java.sql.Date releaseDate;
    public String titleList;

    public CD(String id, String artist, String label, Date releaseDate, String titleList) {
        this.id = id;
        this.artist = artist;
        this.label = label;
        this.releaseDate = new java.sql.Date(releaseDate.getTime());
        this.titleList = titleList;
    }
    public CD(String id, String artist, String label, java.sql.Date releaseDate, String titleList) {
        this.id = id;
        this.artist = artist;
        this.label = label;
        this.releaseDate = releaseDate;
        this.titleList = titleList;
    }
}
