import java.util.HashMap;
import java.util.Set;

import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;
public interface auction 


          extends java.rmi.Remote {



    public AuctionItem createNewListing(int clientID, String item, String desc, double reservePrice)
    throws java.rmi.RemoteException;

    public auctionBuyer createNewBuyer(String Name, String Email)
    throws java.rmi.RemoteException;

    public Set<Integer> getAllKeys()
    throws java.rmi.RemoteException;

    public int generateID(HashMap map)
    throws java.rmi.RemoteException;

    public HashMap<Integer, AuctionItem> getAuctionListingMap()
    throws java.rmi.RemoteException;

    public HashMap<Integer, auctionBuyer> getBuyerMap()
    throws java.rmi.RemoteException;

    public SealedObject getEnSpec(int itemId, SealedObject clientRequest) 
        throws java.rmi.RemoteException;

    public AuctionItem getSpec(int itemId, int clientID) 
        throws java.rmi.RemoteException;

        public auctionBuyer getBuyerSpec(int buyerID)
        throws java.rmi.RemoteException;
    
    public clientRequest decrypt(SealedObject obj)
        throws java.rmi.RemoteException;
    
    public SecretKeySpec readKey()
        throws java.rmi.RemoteException;

    public SealedObject encrypt(AuctionItem cItem)
    throws java.rmi.RemoteException;

    public void updateNewBid(int listingID, int buyerID, double bid)
    throws java.rmi.RemoteException;

    public AuctionItem closeListing(int ID)
    throws java.rmi.RemoteException;

    public boolean reservePriceCheck(int ID)
    throws java.rmi.RemoteException;

    public int generateClientID()
    throws java.rmi.RemoteException;


    public boolean clientIDChecker(int ClientID, int ID)
    throws java.rmi.RemoteException;

    public boolean buyerIDCheck(int ID)
    throws java.rmi.RemoteException;
    
    public boolean auctionItemCheck(int ID)
    throws java.rmi.RemoteException;

   
}