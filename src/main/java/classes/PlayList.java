package classes;

import java.util.List;

public class PlayList<Composition> extends LinkedList<Composition>{
    public PlayList () {
    }

    public PlayList(List<Composition> values) {
        super(values);
    }
}
