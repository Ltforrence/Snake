public class UserSettings {

    private String name;
    private int bSize;
    private int speed;
    private int dAdded;
    private boolean border;
    private int mainColorInt;


    //most of the time you will establish a new user with standard user settings!
    public UserSettings(String n)
    {
        name = n;
        bSize = 4;
        speed = 2;
        dAdded = 2;
        border = false;
        mainColorInt = 0;
    }


    //in some circumstances it will take into account all current settings!
    public UserSettings(String n, int bs, int s, int da, boolean b, int mci) //I think these will likely be the only choices though...
    {
        name = n;
        bSize = bs;
        speed = s;
        dAdded = da;
        border = b;
        mainColorInt = mci;
    }

    public int getbSize()
    {
        return bSize;
    }

    public void setbSize(int bs)
    {
        bSize = bs;
    }
    
    public int getspeed()
    {
        return speed;
    }

    public void setspeed(int s)
    {
        speed = s;
    }
    
    public int getdAdded()
    {
        return dAdded;
    }

    public void setdAdded(int da)
    {
        dAdded = da;
    }

    public int getmainColorInt()
    {
        return mainColorInt;
    }

    public void setmainColorInt(int mci)
    {
        mainColorInt = mci;
    }
    
    public boolean getborder()
    {
        return border;
    }

    public void setborder(boolean b)
    {
        border = b;
    }
}
