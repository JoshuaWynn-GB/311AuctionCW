import java.io.Serializable;
public class AuctionItem implements Serializable{
    private int itemID;
    private String itemTitle;
    private String itemDescription;
    //Creates a constructor to construct an aunction item object
    public AuctionItem(int iID, String iTitle, String iDescription)
    {
        itemID = iID;
        itemTitle = iTitle;
        itemDescription = iDescription;
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

    
    
}
