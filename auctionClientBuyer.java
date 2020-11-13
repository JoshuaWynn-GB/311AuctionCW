import java.rmi.Naming; //Import the rmi naming - so you can lookup remote object
import java.rmi.RemoteException; //Import the RemoteException class so you can catch it
import java.util.Set;
import java.io.Console;
import java.net.MalformedURLException;	//Import the MalformedURLException class so you can catch it
import java.rmi.NotBoundException;	//Import the NotBoundException class so you can catch it

public class auctionClientBuyer {
    public static void main(String[] args) {
        Console console = System.console();
        boolean exit = false;
        int currentLoggedInBuyer = -1;
        try {
    
                // Create the reference to the remote object through the remiregistry			
                auction c = (auction) Naming.lookup("rmi://localhost:1099/AuctionService");

                //User enters name and email to create a new buyer and log in
                System.out.println("Enter your name: ");
                String Name = console.readLine();
                System.out.println("Enter your email: ");
                String Email = console.readLine();
                currentLoggedInBuyer = c.createNewBuyer(Name, Email).getBuyerID();

                //Program runs in a loop until exit program is called
                while (exit == false)
                {
                    System.out.println("Welcome to the Client Buyer\n==================");
                    //Checks to see if a User is logged in
                    if (currentLoggedInBuyer==-1)
                    {
                        System.out.println("You are currently NOT logged in.");
                    }
                    else
                    {
                        System.out.println("You are currently logged in as: " + c.getBuyerSpec(currentLoggedInBuyer).getBuyerName());
                    }

                    System.out.println("Type '1' to Log In/Log Out\nType '2' to see all accounts\nType '3' to make a bid\nType '4' to exit the program");
                    switch (console.readLine()) {
                        //This is for demonstration purposes and allows you to switch accounts
                        case "1":
                            System.out.println("Enter your buyer's ID: ");
                            String strID = console.readLine();
                            int ID = Integer.valueOf(strID).intValue();
                            
                            //checks the buyer exists and logs in
                            if (c.buyerIDCheck(ID))
                            {
                                currentLoggedInBuyer = ID;
                                System.out.println("You are now logged into as: "+c.getBuyerSpec(currentLoggedInBuyer).getBuyerName() + "\n");
                            }
                            else 
                            {
                                System.out.println("Account with that ID does not exist.");
                            }
                            break;
                        case "2":
                            //gets them all the keys from the buyers map and uses that to print all the buyers
                            Set<Integer> allKeys = c.getBuyerMap().keySet();
                            for(Integer i : allKeys) 
                            {
                                System.out.println("ID: " + c.getBuyerSpec(i).getBuyerID() + "\nName: " + c.getBuyerSpec(i).getBuyerName() + "\nEmail: " + c.getBuyerSpec(i).getBuyerEmail() + "\n");           
                            }
                            break;
                        case "3":
                            //gets all the auctions
                            Set<Integer> allListingKeys = c.getAuctionListingMap().keySet();
                            for(Integer i : allListingKeys) 
                            {
                                if (c.getSpec(i, 0).getItemBuyerID() == -1)
                                {
                                    System.out.println("ID: " + c.getSpec(i, 0).getItemID() + "\nTitle: " + c.getSpec(i, 0).getItemTitle() + "\nDescription: " + c.getSpec(i, 0).getItemDescription() + "\nCurrent Bid: £" + c.getSpec(i, 0).getItemCurrentPrice() + "\nBidder Name: " + "No Bids Yet" + "\n");
                                }
                                else
                                {
                                    System.out.println("ID: " + c.getSpec(i, 0).getItemID() + "\nTitle: " + c.getSpec(i, 0).getItemTitle() + "\nDescription: " + c.getSpec(i, 0).getItemDescription() + "\nCurrent Bid: £" + c.getSpec(i, 0).getItemCurrentPrice() + "\nBidder Name: " + c.getBuyerSpec(c.getSpec(i, 0).getItemBuyerID()).getBuyerName() + "\n");
                                }

                            }


                            //gets id of auction item
                            System.out.println("Enter the ID of the listing you want to bid on: ");
                            String strListingID = console.readLine();
                            int ListingID = Integer.valueOf(strListingID).intValue();

                            //checks the item exist
                            if (c.auctionItemCheck(ListingID))
                            {
                                double curPrice = c.getSpec(ListingID, 0).getItemCurrentPrice();
                                System.out.println("Current Bid: " + curPrice);
                                System.out.println("Enter new bid: ");
                                String strNewBid= console.readLine();
                                double newBid = Double.valueOf(strNewBid).doubleValue();
                                //checks the the new bid is more then the current price
                                if (newBid>curPrice)
                                {
                                    c.updateNewBid(ListingID, currentLoggedInBuyer, newBid);
                                }
                                else
                                {
                                    System.out.println("Your bid was either the same or lower then the current sale price, try a higher bid.");
                                }
                            }
                            else 
                            {
                                System.out.println("Listing doesn't exist.");
                            }
                          break;
                        case "4":
                        //exits program
                            exit = true;
                            break;
                        default:
                      }
                }

            }

           

            
            // Catch the exceptions that may occur - rubbish URL, Remote exception
        // Not bound exception or the arithmetic exception that may occur in 
        // one of the methods creates an arithmetic error (e.g. divide by zero)
        catch (MalformedURLException murle) {
                System.out.println();
                System.out.println("MalformedURLException");
                System.out.println(murle);
            }
            catch (RemoteException re) {
                System.out.println();
                System.out.println("RemoteException");
                System.out.println(re);
            }
            catch (NotBoundException nbe) {
                System.out.println();
                System.out.println("NotBoundException");
                System.out.println(nbe);
            }
            catch (java.lang.ArithmeticException ae) {
                System.out.println();
                System.out.println("java.lang.ArithmeticException");
                System.out.println(ae);
            }
        }
}