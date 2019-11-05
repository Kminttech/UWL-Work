/**
 * Data object for heap-sorting lab.
 *
 * @author M. Allen
 * @author YOUR NAME HERE
 */
public class IPDatum implements Comparable
{
    private String address;
    private int upload;
    private int download;
    private boolean downSort;
    
    /**
     * Simple constructor to store data.
     *
     * @param a IP address string.
     * @param up Upload usage.
     * @param down Download usage.
     */
    public IPDatum( String a, int up, int down )
    {
        address = a;
        upload = up;
        download = down;
    }
    
    public IPDatum( String a, int up, int down, boolean downSort )
    {
        address = a;
        upload = up;
        download = down;
        this.downSort = downSort;
    }
    
    /**
     * @return IP address.
     */
    public String getAddress()
    {
        return address;
    }
    
    /**
     * @return Upload usage.
     */
    public int getUpload()
    {
        return upload;
    }
    
    /**
     * @return Download usage.
     */
    public int getDownload()
    {
        return download;
    }
    
    /*
     * Overrides Object method.
     */
    public String toString()
    {
        return "<" + address + "> Up: " + upload + ", Down: " + download;
    }
    
    public void setSortDownload(){
    	downSort = true;
    }
    
    public void setSortUpload(){
    	downSort = false;
    }
    
	@Override
	public int compareTo(Object arg0) {
		IPDatum comp = (IPDatum) arg0;
		if(downSort){
			if(this.download > comp.download){
				return 1;
			}else if(this.download < comp.download){
				return -1;
			}
			return 0;
		}else{
			if(this.upload > comp.upload){
				return 1;
			}else if(this.upload < comp.upload){
				return -1;
			}
			return 0;
		}
	}
}
