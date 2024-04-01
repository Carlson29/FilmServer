package Server;

import business.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class FilmServer {
    private static UserManager userManager;
    private static FilmManager filmManager;
    private static User user;

    public static void main(String[] args) {

        try (ServerSocket listeningSocket = new ServerSocket(FilmService.PORT)) {
            userManager = new UserManager();
            filmManager = new FilmManager();
            boolean serverState = true;
            while (serverState) {

                Socket dataSocket = listeningSocket.accept();
                try (Scanner input = new Scanner(dataSocket.getInputStream());
                     PrintWriter output = new PrintWriter(dataSocket.getOutputStream())) {
                    user = null;
                    boolean state = true;
                    while (state) {
                        String message = input.nextLine();
                        System.out.println("server received: " + message);
                        String[] components = message.split(FilmService.DELIMITER);
                        String response = FilmService.DEFAULT_RESPONSE;
                        if (components.length > 0) {
                            String action = components[0];
                            switch (action) {
                                case (FilmService.REGISTER_REQUEST):
                                    response = FilmService.FAILED_REGISTRATION;
                                    if (components.length == 4) {
                                        if (components[1].length() == 3) {
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
                                        } else {
                                            System.out.println("User name too short");
                                        }
                                    } else {
                                        System.out.println("Invalid details provided");
                                    }
                                    break;
                                case (FilmService.LOGIN_REQUEST):
                                    response = FilmService.FAILED_LOGIN;
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
                                    break;
                                case (FilmService.LOGOUT_REQUEST):
                                    if (components.length == 1) {
                                        response = FilmService.FAILED_LOGOUT_RESPONSE;
                                        if (user != null) {
                                            user = null;
                                            state = false;
                                            response = FilmService.SUCCESSFUL_LOGOUT_RESPONSE;
                                        } else {
                                            System.out.println("Not logged in, so can't log out ");
                                        }
                                    } else {
                                        System.out.println("Invalid details provided");
                                    }
                                    break;
                                case (FilmService.RATE_FILM_REQUEST):
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

                                    }
                                    break;
                                case (FilmService.SEARCH_FILM_BY_GENRE_REQUEST):
                                    if (components.length == 2) {
                                        ArrayList<Film> filmsByGenre = filmManager.searchByGenre(components[1]);
                                        if (!filmsByGenre.isEmpty()) {
                                            response = "";
                                            for (Film film : filmsByGenre) {
                                                response = response + "*" + film.getTitle() + FilmService.DELIMITER + film.getGenre() + FilmService.DELIMITER + film.getTotalRatings() + FilmService.DELIMITER + film.getNumberOfRatings();
                                            }

                                        } else {
                                            response = FilmService.NO_MATCH_FOUND;
                                        }
                                    }
                                    break;
                                case (FilmService.ADD_FILM_REQUEST):
                                    response = FilmService.INSUFFICIENT_PERMISSIONS_ADD_FILM_RESPONSE;
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
                                                System.out.println("You aren't an admin ");
                                            }
                                        } else {
                                            System.out.println("Not logged In");
                                        }
                                    }
                                    break;
                                case (FilmService.REMOVE_FILM_REQUEST):
                                    response = FilmService.INSUFFICIENT_PERMISSIONS_REMOVE_FILM_RESPONSE;
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
                                                System.out.println("You aren't an admin ");
                                            }
                                        } else {
                                            System.out.println("Not logged In");
                                        }
                                    }
                                    break;
                                case (FilmService.EXIT_REQUEST):
                                    response = FilmService.EXIT_RESPONSE;
                                    if (user != null) {
                                        user = null;
                                        state = false;
                                    } else {
                                        System.out.println("Not logged in");
                                    }
                                    break;
                                case (FilmService.SHUTDOWN_REQUEST):
                                    response = FilmService.INSUFFICIENT_PERMISSIONS_SHUTDOWN_RESPONSE;
                                    if (user != null) {
                                        if (user.getAdminStatus() == 2) {
                                            user = null;
                                            state = false;
                                            serverState = false;
                                            response = FilmService.SUCCESSFUL_SHUTDOWN_RESPONSE;
                                        }
                                    } else {
                                        System.out.println("Not logged in");
                                    }
                                    break;

                            }
                        } else {
                            //response = FilmService.INSUFFICIENT_DETAILS_RESPONSE;
                        }
                        output.println(response);
                        output.flush();

                    }

                }
            }
        } catch (BindException e) {
            System.out.println("BindException occurred when attempting to bind to port " + FilmService.PORT);
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException occurred on server socket");
            System.out.println(e.getMessage());
        }

    }
}
