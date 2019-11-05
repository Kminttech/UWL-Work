package predicates;

public class And<T> implements Predicate<T> {

    private Predicate[] preds;

    public And(Predicate<T>... p){
        preds = p;
    }

    @Override
    public boolean accepts(T o) {
        for(int i = 0; i < preds.length; i++){
            if(!preds[i].accepts(o)){
                return false;
            }
        }
        return true;
    }
}