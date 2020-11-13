import java.io.Serializable;

public class auctionChallenge implements Serializable {
    private int Name;
    private int Challenge;
    public auctionChallenge(int aName, int aChallenge)
    {
        Name = aName;
        Challenge = aChallenge;
    }

    public int getName()
    {
        return Name;
    }

    public int getChallenge()
    {
        return Challenge;
    }
}
