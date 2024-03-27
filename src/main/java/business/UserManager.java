package business;

import java.util.ArrayList;
import java.util.HashMap;

public class UserManager {

    private final ArrayList<User> usersList = new ArrayList<User>();

    /**
     * Add a new user to the list of usersList.
     * If a user already exists matching the username and password in usersList, new user won't be added.
     * @param username username of the user
     * @param password password of the user
     * @return true if the user was successfully added, else return false.
     */
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

    /**
     * Search user by username that store in usersList.
     * @param username username of the user we want to search.
     * @return a user by the specified username.
     */
    public User searchByUsername(String username)
    {
        User user = null;
        for(User u : usersList)
        {
            if(u.getUsername().equalsIgnoreCase(username))
            {
                user=u;
            }
        }
        return user;
    }
}
