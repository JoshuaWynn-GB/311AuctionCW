import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;
public interface auction 


          extends java.rmi.Remote {
              	
    public SealedObject getSpec(int itemId, SealedObject clientRequest) 
        throws java.rmi.RemoteException;
    
    public clientRequest decrypt(SealedObject obj)
        throws java.rmi.RemoteException;
    
    public SecretKeySpec readKey()
        throws java.rmi.RemoteException;

    public SealedObject encrypt(AuctionItem cItem)
    throws java.rmi.RemoteException;
    

   
}
