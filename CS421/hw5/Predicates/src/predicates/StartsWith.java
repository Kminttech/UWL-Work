package predicates;

public class StartsWith implements Predicate<String> {

    private String start;

    public StartsWith(String s){
        start = s;
    }

    @Override
    public boolean accepts(String o) {
        if(o.length() >= start.length()){
            return o.substring(0,start.length()).equals(start);
        }else {
            return false;
        }
    }
}
