import java.util.UUID;

public class DVD {
    public String id;
    public String format;
    public Integer length;
    public Integer regionCode;
    public String actors;
    public String director;

    public DVD(String id, String format, Integer length, Integer regionCode, String actors, String director) {
        this.id = id;
        this.format = format;
        this.length = length;
        this.regionCode = regionCode;
        this.actors = actors;
        this.director = director;
    }
}