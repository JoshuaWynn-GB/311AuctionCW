import java.io.Serializable;
public class clientRequest implements Serializable {
    int ID;
    //Creates a constructor to construct an client request object
    public clientRequest(int clientID)
    {
        ID = clientID;
    }

    public int getID()
    {
        return ID;
    }
}
