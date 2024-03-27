package business;

import java.util.ArrayList;
import java.util.HashMap;

public class UserManager {

    private final ArrayList<User> usersList = new ArrayList<User>();

    public boolean addUser(String username, String password)
    {
        User user = new User(username,password);
        if(usersList.contains(user)){
            return false;
        }
        else{
            usersList.add(user);
            return true;
        }
    }

    public ArrayList<User> searchByUsername(String username)
    {
        ArrayList<User> matches = new ArrayList<User>();
        for(User u : usersList)
        {
            if(u.getUsername().equalsIgnoreCase(username))
            {
                matches.add(u);
            }
        }
        return matches;
    }
}
