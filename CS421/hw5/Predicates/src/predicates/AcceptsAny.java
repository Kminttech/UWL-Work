package predicates;

import java.util.List;

public class AcceptsAny<T> implements Predicate<List<T>> {

    private Predicate<T> orignal;

    public AcceptsAny(Predicate<T> o){
        orignal = o;
    }

    @Override
    public boolean accepts(List<T> l) {
        for(T cur : l){
            if(orignal.accepts(cur)){
                return true;
            }
        }
        return false;
    }
}