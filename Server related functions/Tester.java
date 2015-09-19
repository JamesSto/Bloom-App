import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.io.*;

import org.json.JSONException;
import org.json.JSONObject;

public class Tester {
    
    public static class BloomUserProfile {
        
        public String name;
        public UUID identifier; 
        public ArrayList<String> interests;
        public int politicalRating; 
        public double latitude; 
        public double longitude; 
        public ArrayList<String> activeMatches;
        
        
        public BloomUserProfile() {
            name = "Steven";
            identifier = UUID.randomUUID();
            interests = new ArrayList<String>();
            interests.add("Running");
            interests.add("Singing");
            politicalRating = 4;
            
            latitude = 42.45;
            longitude = -76.49;
            
            activeMatches = new ArrayList<String>();
            activeMatches.add("81d20a65-c8de-4665-9b2e-7d965bca2cd4");
            
        }
        
    }
    
    public static void main(String[] args) {
        
        
        BloomUserProfile myPreJSON = new BloomUserProfile();
        
        // List of your activities 
        String[] names = {"name", "identifier", "interests", "politicalRating", "longitude", "latitude", "activeMatches"};
        
        // makes json objects for the profile and coordinates 
        JSONObject myJSON = new JSONObject(myPreJSON, names);
        JSONObject coords = new JSONObject();
        
        // just follow this format for inputting stuff
        try {
            coords.put("longitude", myPreJSON.longitude);
            coords.put("latitude", myPreJSON.latitude);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        try {
            
            String location_update = "http://10.128.13.169/update_location.php";
            //String location_update = "http://10.128.13.169";
            
            
            URL myURL = new URL(location_update);
            URLConnection c = myURL.openConnection();
            c.setRequestProperty("User-Agent", "1a482047-ab66-41d1-9911-c9967632bf7a");
            //c.setRequestProperty("User-Agent", myPreJSON.toString());
            
            c.setRequestProperty("Coordinates", coords.toString());
            //c.setRequestProperty("json", myJSON.toString());
            BufferedReader b = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String s = b.readLine();
            while(s != null) {
                System.out.println(s);
                s = b.readLine();
            }
            b.close();
            
        } catch (IOException e){
            System.out.println(e);
        }
    }
    
    
}
