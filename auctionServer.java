import java.rmi.Naming;	//Import naming classes to bind to rmiregistry
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