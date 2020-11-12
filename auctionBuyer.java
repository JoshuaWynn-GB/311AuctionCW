import java.io.Serializable;

public class auctionBuyer implements Serializable{
    private int buyerID;
    private String buyerName;
    private String buyerEmail;

    public auctionBuyer(int bID, String bName, String bEmail)
    {
        buyerID = bID;
        buyerName = bName;
        buyerEmail = bEmail;
    }

    public int getBuyerID()
    {
        return buyerID;
    }

    public String getBuyerName()
    {
        return buyerName;
    }

    public String getBuyerEmail()
    {
        return buyerEmail;
    }

    
}
