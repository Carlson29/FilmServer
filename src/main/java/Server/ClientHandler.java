package Server;

import business.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    private final Socket dataSocket;
    private  FilmManager filmManager;
    private static  User user;
    private  UserManager userManager;
    private static boolean serverState=true;
    private  boolean state = true;
    public ClientHandler(Socket dataSocket, FilmManager filmManager, UserManager userManager) {
        this.dataSocket = dataSocket;
        this.filmManager = filmManager;
        this.userManager = userManager;
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        try (dataSocket){
            try (Scanner input = new Scanner(dataSocket.getInputStream());
                 PrintWriter output = new PrintWriter(dataSocket.getOutputStream())) {
                while (state) {
                    String message = input.nextLine();
                    //System.out.println("server received: " + message);
                    String[] components = message.split(FilmService.DELIMITER);
                    String response = FilmService.DEFAULT_RESPONSE;
                    if (components.length > 0) {
                        String action = components[0];
                        switch (action) {
                            case (FilmService.REGISTER_REQUEST):
                                response = register(components);
                                break;
                            case (FilmService.LOGIN_REQUEST):
                                response = login(components);
                                break;
                            case (FilmService.LOGOUT_REQUEST):
                                response = logout();
                                break;
                            case (FilmService.RATE_FILM_REQUEST):
                                response = rateFilm(components);
                                break;
                            case (FilmService.SEARCH_FILM_REQUEST):
                                response = searchFilm(components);
                                break;
                            case (FilmService.SEARCH_FILM_BY_GENRE_REQUEST):
                                response = searchByGenre(components);
                                break;
                            case (FilmService.ADD_FILM_REQUEST):
                                response = addFilm(components);
                                break;
                            case (FilmService.REMOVE_FILM_REQUEST):
                                response = removeFilm(components);
                                break;
                            case (FilmService.EXIT_REQUEST):
                                response = exit();
                                break;
                            case (FilmService.SHUTDOWN_REQUEST):
                                response = shutdownServer();
                                break;
                            case (FilmService.SEARCH_FILM_BY_RATING):
                                response = searchByRating(components);
                                break;
                        }
                    } else {
                        System.out.println("Invalid details");
                    }
                    output.println(response);
                    output.flush();
                }
            } catch (IOException e) {
                System.out.println("IOException occurred on server socket");
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public  String register(String[] components) {
        String response = FilmService.FAILED_REGISTRATION;
        if (components.length == 3) {
            if (components[1].length() >= 3) {
                if (CredentialVerification.checkPassword(components[2])) {
                    if (userManager.addUser(components[1], components[2])) {
                        user = new User(components[1], components[1], 1);
                        response = FilmService.SUCCESSFUL_REGISTRATION;
                    } else {
                        System.out.println("user already exist");
                    }

                } else {
                    System.out.println("Invalid password entered");
                }

        }else {
                System.out.println("User name too short");
            }
        } else {
            System.out.println("Invalid details provided");
        }
        return response;
    }

    public  String login(String[] components) {
        String response = FilmService.FAILED_LOGIN;
        if (components.length == 3) {
            response = FilmService.FAILED_LOGIN;
            User u = userManager.searchByUsername(components[1]);
            if (u != null) {
                if (u.getPassword().equals(components[2])) {
                    user = u;
                    if (u.getAdminStatus() == 1) {
                        response = FilmService.SUCCESSFUL_USER_LOGIN;
                    } else if (u.getAdminStatus() == 2) {
                        response = FilmService.SUCCESSFUL_ADMIN_LOGIN;
                    }
                } else {
                    System.out.println("Invalid password");
                }
            } else {
                System.out.println("no user found");
            }
        } else {
            System.out.println("Invalid details provided");
        }
        return response;
    }

    public  String logout() {
        String response = FilmService.FAILED_LOGOUT_RESPONSE;
        if (user != null) {
            user = null;
            state = false;
            response = FilmService.SUCCESSFUL_LOGOUT_RESPONSE;
        } else {
            System.out.println("Not logged in, so can't log out ");
        }
        return response;
    }

    public  String rateFilm(String[] components) {
        String response = FilmService.DEFAULT_RESPONSE;
        if (components.length == 3) {
            if (user != null) {
                try {
                    int rating = Integer.parseInt(components[2]);
                    if (rating >= 0 && rating <= 10) {
                        boolean rate = filmManager.rateFilm(components[1], rating);
                        if (rate) {
                            response = FilmService.SUCCESSFUL_RATE_FILM_RESPONSE;
                        } else {
                            response = FilmService.NO_MATCH_FOUND_RATE_FILM_RESPONSE;
                        }

                    } else {
                        response = FilmService.INVALID_RATING_SUPPLIED_RATE_FILM_RESPONSE;
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Number not entered for rating");
                    response = FilmService.INVALID_RATING_SUPPLIED_RATE_FILM_RESPONSE;
                }

            } else {
                response = FilmService.NOT_LOGGED_IN_RATE_FILM_RESPONSE;
            }
        } else {
            System.out.println("Invalid details provided");
        }
        return response;
    }

    public  String searchFilm(String[] components) {
        String response = FilmService.DEFAULT_RESPONSE;
        if (components.length == 2) {
            Film filmsByTitle = filmManager.searchByTitle(components[1]);
            if (filmsByTitle != null) {
                response = filmsByTitle.getTitle() + FilmService.DELIMITER + filmsByTitle.getGenre() + FilmService.DELIMITER + filmsByTitle.getTotalRatings() + FilmService.DELIMITER + filmsByTitle.getNumberOfRaters();
                System.out.println("film found");
            } else {
                response = FilmService.NO_MATCH_FOUND;
                System.out.println("no match found");
            }

        } else {
            System.out.println("Invalid details provided");
        }
        return response;
    }

    public  String searchByGenre(String[] components) {
        String response = FilmService.DEFAULT_RESPONSE;
        if (components.length == 2) {
                ArrayList<Film> filmsByGenre = filmManager.searchByGenre(components[1]);
                if (!filmsByGenre.isEmpty()) {
                    response = filmManager.encode(FilmService.filmDELIMITER, FilmService.DELIMITER, filmsByGenre);

                } else {
                    response = FilmService.NO_MATCH_FOUND;
                }

        } else {
            System.out.println("Invalid details provided");
        }

        return response;
    }

    public  String searchByRating(String[] components) {
        String response = FilmService.DEFAULT_RESPONSE;
        try{
        if (components.length == 2) {
            ArrayList<Film> filmsByGenre = filmManager.searchByRating(Integer.parseInt(components[1]));
            if (!filmsByGenre.isEmpty()) {
                response = filmManager.encode(FilmService.filmDELIMITER, FilmService.DELIMITER, filmsByGenre);

            } else {
                response = FilmService.NO_MATCH_FOUND;
            }

        } else {
            System.out.println("Invalid details provided");
        }}
        catch(NumberFormatException ex){
            System.out.println("Invalid rating inputed");
        }

        return response;
    }

    public  String addFilm(String[] components) {
        String response = FilmService.DEFAULT_RESPONSE;
        if (components.length == 3) {
            if (user != null) {
                if (user.getAdminStatus() == 2) {
                    Film newFilm = new Film(components[1], components[2]);
                    boolean added = filmManager.add(newFilm);
                    if (added) {
                        response = FilmService.SUCCESSFUL_ADD_FILM_RESPONSE;
                    } else {
                        response = FilmService.FAILED_ADD_FILM_RESPONSE;

                    }

                } else {
                    response = FilmService.INSUFFICIENT_PERMISSIONS_ADD_FILM_RESPONSE;
                    System.out.println("You aren't an admin ");
                }

            } else {
                response = FilmService.INSUFFICIENT_PERMISSIONS_ADD_FILM_RESPONSE;
                System.out.println("Not logged In");
            }
        } else {
            System.out.println("Invalid details provided");
        }
        return response;
    }

    public  String removeFilm(String[] components) {
        String response = FilmService.DEFAULT_RESPONSE;
        if (components.length == 2) {
            if (user != null) {
                if (user.getAdminStatus() == 2) {
                    boolean remove = filmManager.remove(components[1]);
                    if (remove) {
                        response = FilmService.SUCCESSFUL_REMOVE_FILM_RESPONSE;
                    } else {
                        response = FilmService.FAILED_REMOVE_FILM_RESPONSE;
                    }
                } else {
                    response = FilmService.INSUFFICIENT_PERMISSIONS_REMOVE_FILM_RESPONSE;
                    System.out.println("You aren't an admin ");
                }
            } else {
                response = FilmService.INSUFFICIENT_PERMISSIONS_REMOVE_FILM_RESPONSE;
                System.out.println("Not logged In");
            }
        } else {
            System.out.println("Invalid details provided");
        }
        return response;
    }

    public  String exit() {
        user = null;
        state = false;
        String response = FilmService.EXIT_RESPONSE;
        return response;
    }

    public  String shutdownServer() {
        String response = FilmService.DEFAULT_RESPONSE;
        if (user != null) {
            if (user.getAdminStatus() == 2) {
                user = null;
                state = false;
                serverState=false;
                FilmServer.setServerState(false);
                response = FilmService.SUCCESSFUL_SHUTDOWN_RESPONSE;
                System.out.println("server shutting down...");
            } else {
                response = FilmService.INSUFFICIENT_PERMISSIONS_SHUTDOWN_RESPONSE;
                System.out.println("Insufficient permission ");
            }
        } else {
            System.out.println("Not logged in");
        }
        return response;
    }

    public static boolean isServerState() {
        return serverState;
    }
}
