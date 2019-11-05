import predicates.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class PredicateUtilities {

    public static Collection filter(Predicate p, Collection in) {
        ArrayList ret = new ArrayList();
        for(Object o : in){
            if(p.accepts(o)){
                ret.add(o);
            }
        }
        return ret;
    }
}
