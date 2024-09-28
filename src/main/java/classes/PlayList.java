package classes;

import java.util.List;
/*
Класс плейлиста, дочерний класс LinkedList
 */
public class PlayList<Composition> extends LinkedList<Composition> {
    public PlayList() {}
    /*
    Конструктор
     */
    public PlayList(List<Composition> values) {
        super(values);
    }
}
