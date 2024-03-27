package business;

public class FilmService {

    public static final String HOST = "localhost";
    public static final int PORT = 41900;
    public static final String REGISTER_REQUEST = "REGISTER";
    public static final String SUCCESSFUL_REGISTRATION = "REGISTERED";
    public static final String USERNAME_TAKEN_REGISTRATION = "USERNAME_TAKEN";
    public static final String INVALID_USERNAME_RESPONSE = "USERNAME TOO SHORT";
    public static final String INVALID_PASSWORD_REGISTRATION = "INVALID_PASSWORD";
    public static final String LOGIN_REQUEST = "LOGIN";
    public static final String SUCCESSFUL_LOGIN = "LOGIN SUCCESSFUL";
    public static final String FAILED_LOGIN = "USERNAME OR PASSWORD DON'T MATCH";
    public static final String SEARCH_FILM_REQUEST = "SEARCH_FILM";
    public static final String SEARCH_FILM_BY_GENRE_REQUEST = "SEARCH_BY_GENRE";
    public static final String RATE_FILM_REQUEST = "RATE_FILM";
    public static final String ADD_FILM_REQUEST = "ADD_FILM";
    public static final String SUCCESSFUL_ADD_FILM_RESPONSE = "FILM ADDED";
    public static final String INSUFFICIENT_DETAILS_RESPONSE = "INSUFFICIENT DETAILS";
    public static final String FAILED_ADD_FILM_RESPONSE = "FILM ALREADY EXIST";
    public static final String REMOVE_FILM_REQUEST = "REMOVE_FILM";
    public static final String SUCCESSFUL_REMOVE_FILM_RESPONSE = "FILM REMOVED";
    public static final String FAILED_REMOVE_FILM_RESPONSE = "FILM DOESN'T EXIST";
    public static final String EXIT_REQUEST = "EXIT";
    public static final String EXIT_RESPONSE = "GOODBYE";
    public static final String DEFAULT_RESPONSE = "UNKNOWN REQUEST";
    public static final String DELIMITER = "%%";
    public static final String INVALID_DETAILS_RESPONSE = "INVALID DETAILS";

}
