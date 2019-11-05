/**
 * Simple class for storing data about vehicles.
 * 
 * @author M. Allen
 * @author Kevin Minter
 */
public class Vehicle
{
    private final String vehicleMake;
    private final String model;
    private final int year;
    private int mileage;

    /**
     * Constructor stores inputs for this Vehicle instance.
     * 
     * @param make Make of vehicle.
     * @param mod Model of vehicle.
     * @param yr Model year of vehicle.
     * @param miles Current mileage on vehicle.
     */
    public Vehicle( String make, String mod, int yr, int miles )
    {
	vehicleMake = make;
	model = mod;
	year = yr;
	mileage = miles;
    }

    /**
     * @return Make of this vehicle.
     */
    public String getMake()
    {
	return vehicleMake;
    }

    /**
     * @return Model of this vehicle.
     */
    public String getModel()
    {
	return model;
    }

    /**
     * @return Model year of this vehicle.
     */
    public int getYear()
    {
	return year;
    }

    /**
     * @return Mileage of this vehicle.
     */
    public int getMiles()
    {
	return mileage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
	return year + " " + vehicleMake + " " + model + " (" + mileage + ")";
    }
    
    @Override
    public boolean equals(Object o){
    	if(o instanceof Vehicle){
    		Vehicle temp = (Vehicle) o;
    		if(temp.getMake().equals(vehicleMake) && temp.getModel().equals(model) && temp.getYear() == year){
    			return true;
    		}
    	}
    	return false;
    }
    
    @Override
    public int hashCode(){
    	String codeString = vehicleMake + model + year;
    	return codeString.hashCode();
    }
}