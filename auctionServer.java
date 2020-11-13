import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.rmi.Naming; //Import naming classes to bind to rmiregistry
import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.spec.SecretKeySpec;
public class auctionServer {
   public auctionServer() {
     try {
        auction c = new auctionImpl();
       	Naming.rebind("rmi://localhost:1099/AuctionService", c);
     } 
     catch (Exception e) {
       System.out.println("Server Error: " + e);
     }
   }

   public static void main(String args[]) {
     	//Create the new Auction server
	new auctionServer();
   }


}