package classes;

/*
Класс композиции для хранения ее в Node
 */
public class Composition {
    private final int id;
    private final String name;
    private final String artists;
    private final String albums;
    private final String uuid;

    /*
    Конструктор
     */
    public Composition(
            int id, String name, String artists, String albums, String uuid) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.albums = albums;
        this.uuid = uuid;
    }

    /*
    Метод для получения строки в виде свойств композиции
     */
    public String toString() {
        return artists + " - " + name + " | " + albums + " |";
    }

    /*
    Метод-геттер для получения уникального айди музыкальной дорожки композиции
     */
    public String getUuid() {
        return uuid;
    }

    /*
    Метод-геттер для получения названия композиции
     */
    public String getName() {
        return name;
    }

    /*
    Метод-геттер для получения артистов композиции
     */
    public String getArtists() {
        return artists;
    }

    /*
    Метод-геттер для получения id композиции
     */
    public int getId() {
        return id;
    }

    /*
    Метод-геттер для получения альбомов композиции
     */
    public String getAlbums() {
        return albums;
    }
}
