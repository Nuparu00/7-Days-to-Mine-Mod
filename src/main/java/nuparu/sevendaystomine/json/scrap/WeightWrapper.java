package nuparu.sevendaystomine.json.scrap;

import org.apache.commons.lang3.math.Fraction;

public class WeightWrapper {

    public static final WeightWrapper ZERO = new WeightWrapper(0);

    private Number weight;

    @Override
    public String toString() {
        return weight.toString();
    }

    public WeightWrapper(Number weight){
        this.weight = weight;
    }

    public boolean isFraction(){
        return weight instanceof Fraction;
    }

    public Fraction asFraction(){
        return weight instanceof Fraction ? (Fraction) weight : Fraction.getFraction((double) weight);
    }

    public double asDouble(){
        return weight instanceof Double ? (double) weight : ((Fraction) weight).doubleValue();
    }
    public Number asNumber(){
        return weight;
    }
}
