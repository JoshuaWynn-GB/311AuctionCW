import java.io.Serializable;
public class AuctionItem implements Serializable{
    private int itemID;
    private String itemTitle;
    private String itemDescription;
    private double itemReservePrice;
    private int itemBuyerID;
    private double itemCurrentPrice;
    private int itemClientID;
    //Creates a constructor to construct an aunction item object
    public AuctionItem(int iClientID, int iID, String iTitle, String iDescription, Double iReservePrice)
    {
        itemClientID = iClientID;
        itemID = iID;
        itemTitle = iTitle;
        itemDescription = iDescription;
        itemReservePrice = iReservePrice;
        itemBuyerID = -1;
        itemCurrentPrice = 0.00;
    }

    public int getClientID()
    {
        return itemClientID;
    }

    public int getItemID()
    {
        return itemID;
    }

    public String getItemTitle()
    {
        return itemTitle;
    }

    public String getItemDescription()
    {
        return itemDescription;
    }

    public double getItemReservePrice()
    {
        return itemReservePrice;
    }

    public int getItemBuyerID()
    {
        return itemBuyerID;
    }

    public void setItemBuyerID(int ID)
    {
        itemBuyerID = ID;
    }

    public double getItemCurrentPrice()
    {
        return itemCurrentPrice;
    }

    public void setItemCurrentPrice(double price)
    {
        itemCurrentPrice = price;
    }
    

    
    
}
