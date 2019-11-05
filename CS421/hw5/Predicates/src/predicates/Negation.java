package predicates;

public class Negation<T> implements Predicate<T> {

    private Predicate<T> orignal;

    public Negation(Predicate<T> o){
        orignal = o;
    }

    @Override
    public boolean accepts(T o) {
        return !orignal.accepts(o);
    }
}