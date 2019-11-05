package predicates;

import java.util.List;

public class Subset<T> implements Predicate<List<T>> {

    private List<T> fullList;

    public Subset(List<T> full){
        fullList = full;
    }

    @Override
    public boolean accepts(List<T> o) {
        return fullList.containsAll(o);
    }
}