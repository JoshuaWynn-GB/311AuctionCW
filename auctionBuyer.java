import java.io.Serializable;

public class auctionBuyer implements Serializable{
    private int buyerID;
    private String buyerName;

    public auctionBuyer(int bID, String bName)
    {
        buyerID = bID;
        buyerName = bName;
    }

    public int getBuyerID()
    {
        return buyerID;
    }

    public String getBuyerName()
    {
        return buyerName;
    }

    
}
