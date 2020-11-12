import java.rmi.Naming; //Import the rmi naming - so you can lookup remote object
import java.rmi.RemoteException; //Import the RemoteException class so you can catch it
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException; //Import the MalformedURLException class so you can catch it
import java.nio.file.Files;
import java.rmi.NotBoundException; //Import the NotBoundException class so you can catch it
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException; // Import the IOException class to handle errors
import java.io.OutputStream;
import java.io.File; // Import the File class
import java.io.FileOutputStream;
public class auctionClient {

    public static void main(String[] args) {

        try {

            // Create the reference to the remote object through the remiregistry
            auction c = (auction) Naming.lookup("rmi://localhost:1099/AuctionService");
            System.out.println(c.getSpec(1, 0));
            //create an instance of a client request (just contains the client id)
            clientRequest cOne = new clientRequest(0);

            //Create the key and write it to a file
            writeFile(createKey());
            //using the key created, you create a cipher to encrypt the client request
            Cipher ci = Cipher.getInstance("AES");
            ci.init(Cipher.ENCRYPT_MODE, readKey());
            System.out.println("[CLIENT] Shared Key: " + readKey().toString());
            SealedObject cEncryptOne = new SealedObject(cOne, ci);
            System.out.println("[CLIENT] Encrypted Client Request: " + cEncryptOne + "\n");

            //Get the current auction item and decrypt it
            SealedObject currentEncryptedItem = c.getEnSpec(0, cEncryptOne);
            AuctionItem currentDecryptedItem = decrypt(currentEncryptedItem);
            System.out.println("[CLIENT] Encrypted Current Item: " + currentEncryptedItem);
            System.out.println("[CLIENT] Decrypted Item Title: " + currentDecryptedItem.getItemTitle() + "\n[CLIENT] Decrypted Item Description: " + currentDecryptedItem.getItemDescription());

        }
        catch (MalformedURLException murle) {
            System.out.println();
            System.out.println("MalformedURLException");
            System.out.println(murle);
        } catch (RemoteException re) {
            System.out.println();
            System.out.println("RemoteException");
            System.out.println(re);
        } catch (NotBoundException nbe) {
            System.out.println();
            System.out.println("NotBoundException");
            System.out.println(nbe);
        } catch (java.lang.ArithmeticException ae) {
            System.out.println();
            System.out.println("java.lang.ArithmeticException");
            System.out.println(ae);
        } catch (InvalidKeyException | IOException | NoSuchAlgorithmException | NoSuchPaddingException
                | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    public static Key createKey() {
        //Create key
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRan = new SecureRandom();
            keyGenerator.init(secureRan);
            Key k = keyGenerator.generateKey();
            System.out.print("[CLIENT] Key Creation: " + k);
            return k;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void writeFile(Key k) {
        try {
            //Takes the shared key and writes it to a file
            OutputStream os = new FileOutputStream("Key.txt");
            os.write(k.getEncoded());
            System.out.println("Successfully" + " byte inserted");
            os.close();
        }
        catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    public static SecretKeySpec readKey() throws IOException {
        //Read in shared key from file and return the key
        File file = new File("Key.txt");
        SecretKeySpec sK = new SecretKeySpec(Files.readAllBytes(file.toPath()), "AES");
        return sK;
    }

    public static AuctionItem decrypt(SealedObject obj) {
        //Takes a SealedObject and decrypts using a cipher which we get by reading in the shared key to return the AuctionItem
        Cipher ci;
        try {
            ci = Cipher.getInstance("AES");
            ci.init(Cipher.DECRYPT_MODE, readKey());
            AuctionItem aitem = (AuctionItem) obj.getObject(ci);
            return aitem;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException | ClassNotFoundException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        } 
    }
}
