package classes;

public class Composition {
    private final String name;
    private final String artists;
    private final String albums;
    private final String uuid;

    public Composition (String name, String artists,
                        String albums, String uuid
    ) {
        this.name = name;
        this.artists = artists;
        this.albums = albums;
        this.uuid = uuid;
    }

    public String toString() {
        return artists + " - " + name + " | " + albums + " |";
    }
}
