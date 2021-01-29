import java.util.*; 
public class SettingsFile {
    //This file will be used to return a dictionary of UserSettings to the main Board class. 
    private String path;

    public SettingsFile(String p)
    {
        //Here we will either open or just create a file at the path that was entered
        path = p;
        boolean fresh = openCSV();
        if(!fresh)
            readCSV();
        
        

    }

    private boolean openCSV()
    {
        //here we will open the csv or not!
        return true; //returns true if the CSV did not exist before
    }

    private void readCSV()
    {
        //here we will read the CSV
    }

    public void writeCSV(Dictionary d)
    {
        //here we will write the whole dictionary to the CSV
    }
    public void writeCSV(UserSettings us)
    {
        //here we will write a single new us to the dictionary or if it is in the Dictionary in this then it will just edit the one
    }

    
}
