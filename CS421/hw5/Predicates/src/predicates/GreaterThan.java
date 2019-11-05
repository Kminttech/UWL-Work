package predicates;

public class GreaterThan<T extends Comparable> implements Predicate<T> {

    private Comparable ref;

    public GreaterThan(T c){
        ref = c;
    }

    @Override
    public boolean accepts(T o) {
        return o.compareTo(ref) > 0;
    }
}