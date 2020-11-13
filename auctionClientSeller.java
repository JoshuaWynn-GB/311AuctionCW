import java.rmi.Naming; //Import the rmi naming - so you can lookup remote object
import java.rmi.RemoteException; //Import the RemoteException class so you can catch it
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

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
import java.io.Console;
import java.io.File; // Import the File class
import java.io.FileOutputStream;
public class auctionClientSeller   {

    public static void main(String[] args) {
    Console console = System.console();
    boolean exit = false;
    int clientID;
        try {

            // Create the reference to the remote object through the remiregistry
            auction c = (auction) Naming.lookup("rmi://localhost:1099/AuctionService");
            clientID = c.generateClientID();
            /*System.out.println(c.getListingMap());
            System.out.println("hey");
            c.createNewListing("iPhone", "it's a phone");
            System.out.println(c.getListingMap());*/

            while (exit == false)
            {
                System.out.println("Welcome to the Client Seller\n==================\nType '1' to Create a new listing\nType '2' to see all current listings\nType '3' to close a listing\nType '4' to exit the program");
                
                switch (console.readLine()) {
                    case "1":
                        System.out.println("Enter your item's title: ");
                        String title = console.readLine();
                        System.out.println("Enter your item's description: ");
                        String description = console.readLine();
                        System.out.println("Enter your item's reserve price: ");
                        String strReservePrice= console.readLine();
                        double reservePrice = Double.valueOf(strReservePrice).doubleValue();
                        AuctionItem newItem = c.createNewListing(clientID, title, description, reservePrice);
                        System.out.println("Item has been added\nID: " + newItem.getItemID() + "\nTitle: " + newItem.getItemTitle() + "\nDescription: " + newItem.getItemDescription() + newItem.getItemReservePrice() + "\n");
                        break;
                    case "2":
                        
                        Set<Integer> allKeys = c.getAuctionListingMap().keySet();
                        for(Integer i : allKeys) {
                        System.out.println("ID: " + c.getSpec(i, 0).getItemID() + "\nTitle: " + c.getSpec(i, 0).getItemTitle() + "\nDescription: " + c.getSpec(i, 0).getItemDescription() + "\nReserve Price: " + c.getSpec(i, 0).getItemReservePrice() + "\n");
                        }
                      break;
                    case "3":
                        System.out.println("Enter your item ID of the auction you want to close: ");
                        String strCloseID = console.readLine();
                        int closeID = Integer.valueOf(strCloseID).intValue();

                        System.out.println("Listing CID: " + (c.getAuctionListingMap().get(closeID)).getClientID() + "\nSent CID: " + clientID);
                        if (c.clientIDChecker(clientID, closeID))
                        {
                            if (c.reservePriceCheck(closeID))
                            {
                                AuctionItem closedListing = c.closeListing(closeID);
                                System.out.println(c.getBuyerSpec(closedListing.getItemBuyerID()).getBuyerName() + " has won the listing (details below): "+ "\nTitle: " + closedListing.getItemTitle() + "\nSold Price: " + closedListing.getItemCurrentPrice() + "\nContact Name: " + c.getBuyerSpec(closedListing.getItemBuyerID()).getBuyerName() + "\nContact Email: "+ c.getBuyerSpec(closedListing.getItemBuyerID()).getBuyerEmail());
                            }
                            else
                            {
                                System.out.println("Reserve price has not been met so auction was not closed");
                            }
                        }
                        else
                        {
                            System.out.println("Client ID does not match so can't close the listing.");
                        }
                      break;
                    case "4":
                      exit = true;
                      break;
                    default:
                      // code block
                  }
            }


        } catch (MalformedURLException murle) {
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
        }
    }

   
}