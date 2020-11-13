import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;
import java.util.Random;
import java.util.Set;

public class auctionImpl extends java.rmi.server.UnicastRemoteObject implements auction  {
    // Create a Hashmap to store the auction items
    HashMap<Integer, AuctionItem> AuctionItemMap = new HashMap<Integer, AuctionItem>();
    HashMap<Integer, auctionBuyer> AuctionBuyerMap = new HashMap<Integer, auctionBuyer>();
    HashMap<Integer, Integer> clientID = new HashMap<Integer, Integer>();
    //HashMap<Integer> clientID = new HashMap<Integer>();

    public auctionImpl()

            throws java.rmi.RemoteException {
        super();
        //Populate the auction list
        //Populate new Buyers

         createNewListing(384,"Book", "Some text on paper", 2.00);
         createNewListing(3492,"Car", "Metal box with wheels", 1000.00);
         createNewListing(23,"Chair", "Made of wood and has four legs", 40.00);

         createNewBuyer("John Smith", "john.smith@gmail.com");
         createNewBuyer("Brad Walsh", "brad.walsh@gmail.com");


         

    }
    //gets all the keys from the listings
    public Set<Integer> getAllKeys()
    {
        return AuctionItemMap.keySet();
    }

    //creates new Listing
    public synchronized AuctionItem createNewListing(int clientID, String item, String desc, double reservePrice) {
        int ID = generateID(AuctionItemMap);
        AuctionItem newAuctionItem = new AuctionItem(clientID, ID, item, desc, reservePrice);
        AuctionItemMap.put(ID, newAuctionItem);
        System.out.println(AuctionItemMap);
        return newAuctionItem;
    }

    //creates new buyer
    public synchronized auctionBuyer createNewBuyer(String Name, String Email) {
        int ID = generateID(AuctionItemMap);
        auctionBuyer newBuyer = new auctionBuyer(ID, Name, Email);
        AuctionBuyerMap.put(ID, newBuyer);
        System.out.println(AuctionBuyerMap);
        return newBuyer;
    }

    //closes listing
    public synchronized AuctionItem closeListing(int ID)
    {
        AuctionItem curClosedListing = AuctionItemMap.get(ID);
        AuctionItemMap.remove(ID);
        return curClosedListing;
    }

    //checks to see if the listing you want to close has the same client ID and returns a boolean
    public boolean clientIDChecker(int ClientID, int ID)
    {
        AuctionItem curClosedListing = AuctionItemMap.get(ID);
        
        if (curClosedListing.getClientID() == ClientID)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //checks to see if buyer ID exists
    public boolean buyerIDCheck(int ID)
    {
        if (AuctionBuyerMap.get(ID) != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //checks to see if auction item exists
    public boolean auctionItemCheck(int ID)
    {
        if (AuctionItemMap.get(ID) != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    //returns a unique random int
    public int generateClientID()
    {
        return generateID(clientID);
    }

    //checks that the price is more then the reserve price
    public boolean reservePriceCheck(int ID)
    {
        if(AuctionItemMap.get(ID).getItemReservePrice()<AuctionItemMap.get(ID).getItemCurrentPrice())
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    //generates a unique int 
    public int generateID(HashMap map)
    {
        boolean Found = true;
        Random ran = new Random();
        int newID = 0;
        int mapSize = 10000;
        while (Found) 
        {
            
            int randomInt = ran.nextInt(mapSize);
            //if map is full close program
            if (map.size() == mapSize)
            {
                System.exit(0); 
            } 
            else if (map.get(randomInt) == null)
            {
                Found = false;
                newID = randomInt;
            }
        }
        System.out.println("ID: " + newID);
        return newID;
    }

    public HashMap<Integer, AuctionItem> getAuctionListingMap()
    {
        return AuctionItemMap;
    }

    public HashMap<Integer, auctionBuyer> getBuyerMap()
    {
        return AuctionBuyerMap;
    }

    //updates a listing with the new bid with the buyers ID
    public synchronized void updateNewBid(int listingID, int buyerID, double bid)
    {
        AuctionItemMap.get(listingID).setItemBuyerID(buyerID);
        AuctionItemMap.get(listingID).setItemCurrentPrice(bid);
        System.out.println(AuctionItemMap.get(listingID).getItemTitle() + "\n" + AuctionItemMap.get(listingID).getItemCurrentPrice()+ "\n" + AuctionItemMap.get(listingID).getItemBuyerID());
    }

    //Level 2
    public SealedObject getEnSpec(int itemId, SealedObject clientRequest) throws java.rmi.RemoteException {
        //Gets the current auction item and encrypts it
        AuctionItem currentItem = AuctionItemMap.get(itemId);
        SealedObject encryptedCurItem = encrypt(currentItem);
        System.out.println("[SERVER] Decrypted Client Request: " + decrypt(clientRequest).getID());
        System.out.println("[SERVER] Encrypted Current Item: " + encryptedCurItem);
        return encryptedCurItem;
    }

    public AuctionItem getSpec(int itemId, int clientID) throws java.rmi.RemoteException {
        //Gets the current auction item and encrypts it
        AuctionItem currentItem = AuctionItemMap.get(itemId);
        return currentItem;
    }

    //get buyer details
    public auctionBuyer getBuyerSpec(int buyerID)
    {
        auctionBuyer currentBuyer = AuctionBuyerMap.get(buyerID);
        return currentBuyer;
    }


    //Level 2
    public clientRequest decrypt(SealedObject obj) {
        //Takes a SealedObject and decrypts using a cipher which we get by reading in the shared key to return the client request
        Cipher ci;
        try {
            ci = Cipher.getInstance("AES");
            ci.init(Cipher.DECRYPT_MODE, readKey());
            clientRequest cr = (clientRequest) obj.getObject(ci);
            return cr;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException | ClassNotFoundException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        } 
    }

    //Level 2
    public SecretKeySpec readKey()
    {
        //Read in shared key from file and return the key
        File file = new File("Key.txt");
        SecretKeySpec sK;
        try {
            sK = new SecretKeySpec(Files.readAllBytes(file.toPath()), "AES");
            return sK;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }     
    }

    //Level 2
    public SealedObject encrypt(AuctionItem cItem)
    {
        //Take the current auction item and encrypt it using the cipher which you get from the shared key
        Cipher ci;
        try {
            ci = Cipher.getInstance("AES");
            ci.init(Cipher.ENCRYPT_MODE, readKey());
            SealedObject cEncrypt = new SealedObject(cItem,ci);
            return cEncrypt;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }
    }   
}