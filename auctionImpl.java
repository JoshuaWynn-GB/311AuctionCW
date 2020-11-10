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
public class auctionImpl extends java.rmi.server.UnicastRemoteObject implements auction {
    //Create a Hashmap to store the auction items
    HashMap<Integer, AuctionItem> AuctionItemMap = new HashMap<Integer, AuctionItem>();
    public auctionImpl()

        throws java.rmi.RemoteException {
        super();
        //Populate the auction list
        AuctionItem itemOne = new AuctionItem(0, "Book", "Some text on paper");
        AuctionItem itemTwo = new AuctionItem(1, "Car", "Metal box with wheels");
        AuctionItem itemThree = new AuctionItem(2, "Chair", "Made of wood and has four legs");

        AuctionItemMap.put(itemOne.getItemID(), itemOne);
        AuctionItemMap.put(itemTwo.getItemID(), itemTwo);
        AuctionItemMap.put(itemThree.getItemID(), itemThree);

    }

    public SealedObject getSpec(int itemId, SealedObject clientRequest) throws java.rmi.RemoteException {
        //Gets the current auction item and encrypts it
        AuctionItem currentItem = AuctionItemMap.get(itemId);
        SealedObject encryptedCurItem = encrypt(currentItem);
        System.out.println("[SERVER] Decrypted Client Request: " + decrypt(clientRequest).getID());
        System.out.println("[SERVER] Encrypted Current Item: " + encryptedCurItem);
        return encryptedCurItem;
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
