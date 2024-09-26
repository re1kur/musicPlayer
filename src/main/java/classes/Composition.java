package classes;

public class Composition {
    private final int id;
    private final String name;
    private final String artists;
    private final String albums;
    private final String uuid;

    public Composition (int id, String name, String artists,
                        String albums, String uuid
    ) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.albums = albums;
        this.uuid = uuid;
    }

    public String toString() {
        return artists + " - " + name + " | " + albums + " |";
    }

    public String getUuid () {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getArtists() {
        return artists;
    }

    public int getId() {
        return id;
    }

    public String getAlbums() {
        return albums;
    }
}
