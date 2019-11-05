package predicates;

import java.awt.*;

public class SimilarColor implements Predicate<Color> {

    private Color comp;

    public SimilarColor(Color c){
        comp = c;
    }

    @Override
    public boolean accepts(Color o) {
        return (Math.abs(comp.getRed() - o.getRed()) + Math.abs(comp.getGreen() - o.getGreen()) + Math.abs(comp.getBlue() - o.getBlue())) <= 30;
    }
}
