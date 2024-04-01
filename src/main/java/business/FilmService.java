package business;

public class FilmService {

    public static final String HOST = "localhost";
    public static final int PORT = 41235;
    public static final String REGISTER_REQUEST = "register";
    public static final String SUCCESSFUL_REGISTRATION = "ADDED";
    public static final String FAILED_REGISTRATION = "REJECTED";
    public static final String LOGIN_REQUEST = "login";
    public static final String SUCCESSFUL_ADMIN_LOGIN = "SUCCESS_ADMIN";
    public static final String SUCCESSFUL_USER_LOGIN = "SUCCESS_USER";
    public static final String FAILED_LOGIN = "FAILED";
    public static final String LOGOUT_REQUEST = "logout";
    public static final String SUCCESSFUL_LOGOUT_RESPONSE = "LOGGED_OUT";
    public static final String FAILED_LOGOUT_RESPONSE = "FAILED";
    public static final String SEARCH_FILM_REQUEST = "searchByName";
    public static final String NO_MATCH_FOUND = "NO_MATCH_FOUND";
    public static final String SEARCH_FILM_BY_GENRE_REQUEST = "searchByGenre";
    public static final String RATE_FILM_REQUEST = "rate";
    public static final String SUCCESSFUL_RATE_FILM_RESPONSE = "SUCCESS";
    public static final String INVALID_RATING_SUPPLIED_RATE_FILM_RESPONSE = "INVALID_RATING_SUPPLIED";
    public static final String NOT_LOGGED_IN_RATE_FILM_RESPONSE = "NOT_LOGGED_IN";
    public static final String NO_MATCH_FOUND_RATE_FILM_RESPONSE = "NO_MATCH_FOUND";
    public static final String ADD_FILM_REQUEST = "add";
    public static final String SUCCESSFUL_ADD_FILM_RESPONSE = "ADDED";
    public static final String FAILED_ADD_FILM_RESPONSE = "EXISTS";
    public static final String INSUFFICIENT_PERMISSIONS_ADD_FILM_RESPONSE = "INSUFFICIENT_PERMISSIONS";
    public static final String INSUFFICIENT_DETAILS_RESPONSE = "INSUFFICIENT DETAILS";
    public static final String REMOVE_FILM_REQUEST = "remove";
    public static final String SUCCESSFUL_REMOVE_FILM_RESPONSE = "REMOVED";
    public static final String FAILED_REMOVE_FILM_RESPONSE = "NOT_FOUND";
    public static final String INSUFFICIENT_PERMISSIONS_REMOVE_FILM_RESPONSE = "INSUFFICIENT_PERMISSIONS";
    public static final String EXIT_REQUEST = "exit";
    public static final String EXIT_RESPONSE = "GOODBYE";
    public static final String DEFAULT_RESPONSE = "INVALID_REQUEST";
    public static final String SHUTDOWN_REQUEST = "shutdown";
    public static final String SUCCESSFUL_SHUTDOWN_RESPONSE = "SHUTTING_DOWN";
    public static final String INSUFFICIENT_PERMISSIONS_SHUTDOWN_RESPONSE = "INSUFFICIENT_PERMISSIONS";
    public static final String DELIMITER = "%%";


}
