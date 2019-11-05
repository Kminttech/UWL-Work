package predicates;

import java.util.List;

public class AcceptsAll<T> implements Predicate<List<T>> {

    private Predicate<T> orignal;

    public AcceptsAll(Predicate<T> o){
        orignal = o;
    }

    @Override
    public boolean accepts(List<T> l) {
        for(T cur : l){
            if(!orignal.accepts(cur)){
                return false;
            }
        }
        return true;
    }
}