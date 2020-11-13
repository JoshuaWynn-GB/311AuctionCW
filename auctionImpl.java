import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;
import java.util.Random;
import java.util.Set;
import java.util.jar.Attributes.Name;

public class auctionImpl extends java.rmi.server.UnicastRemoteObject implements auction  {
    // Create a Hashmap to store the auction items
    HashMap<Integer, AuctionItem> AuctionItemMap = new HashMap<Integer, AuctionItem>();
    HashMap<Integer, auctionBuyer> AuctionBuyerMap = new HashMap<Integer, auctionBuyer>();
    HashMap<Integer, Integer> clientID = new HashMap<Integer, Integer>();
    //HashMap<Integer> clientID = new HashMap<Integer>();

    public auctionImpl()

            throws java.rmi.RemoteException {
        super();
        // Populate the auction list
        
         /*AuctionItem itemOne = new AuctionItem(0, "Book", "Some text on paper", 2.00);
         AuctionItem itemTwo = new AuctionItem(1, "Car", "Metal box with wheels", 1000.00);
         AuctionItem itemThree = new AuctionItem(2, "Chair", "Made of wood and has four legs", 40.00);
         
         AuctionItemMap.put(itemOne.getItemID(), itemOne);
         AuctionItemMap.put(itemTwo.getItemID(), itemTwo);
         AuctionItemMap.put(itemThree.getItemID(), itemThree);*/

         createNewListing(384,"Book", "Some text on paper", 2.00);
         createNewListing(3492,"Car", "Metal box with wheels", 1000.00);
         createNewListing(23,"Chair", "Made of wood and has four legs", 40.00);

         createNewBuyer("John Smith", "john.smith@gmail.com");
         createNewBuyer("Brad Walsh", "brad.walsh@gmail.com");


         

    }

    public Set<Integer> getAllKeys()
    {
        return AuctionItemMap.keySet();
    }

    public AuctionItem createNewListing(int clientID, String item, String desc, double reservePrice) {
        int ID = generateID(AuctionItemMap);
        AuctionItem newAuctionItem = new AuctionItem(clientID, ID, item, desc, reservePrice);
        AuctionItemMap.put(ID, newAuctionItem);
        System.out.println(AuctionItemMap);
        return newAuctionItem;
    }

    public auctionBuyer createNewBuyer(String Name, String Email) {
        int ID = generateID(AuctionItemMap);
        auctionBuyer newBuyer = new auctionBuyer(ID, Name, Email);
        AuctionBuyerMap.put(ID, newBuyer);
        System.out.println(AuctionBuyerMap);
        return newBuyer;
    }

    public AuctionItem closeListing(int ID)
    {
        AuctionItem curClosedListing = AuctionItemMap.get(ID);
        AuctionItemMap.remove(ID);
        return curClosedListing;
    
        
    }

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

    public int generateClientID()
    {
        return generateID(clientID);
    }

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


    public int generateID(HashMap map)
    {
        boolean Found = true;
        Random ran = new Random();
        int newID = 0;
        int mapSize = 10000;
        while (Found) 
        {
            int randomInt = ran.nextInt(mapSize);
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


    public void updateNewBid(int listingID, int buyerID, double bid)
    {
        AuctionItemMap.get(listingID).setItemBuyerID(buyerID);
        AuctionItemMap.get(listingID).setItemCurrentPrice(bid);
        System.out.println(AuctionItemMap.get(listingID).getItemTitle() + "\n" + AuctionItemMap.get(listingID).getItemCurrentPrice()+ "\n" + AuctionItemMap.get(listingID).getItemBuyerID());
    }


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

    public auctionBuyer getBuyerSpec(int buyerID)
    {
        auctionBuyer currentBuyer = AuctionBuyerMap.get(buyerID);
        return currentBuyer;
    }



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