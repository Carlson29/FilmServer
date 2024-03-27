package Server;

import business.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class FilmServer {
    private static UserManager userManager;
    private static FilmManager filmManager;
    private static User user;

    public static void main(String[] args) {

        try (ServerSocket listeningSocket = new ServerSocket(FilmService.PORT)) {
            userManager = new UserManager();
            filmManager = new FilmManager();
            while (true) {

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
                            String action = components[0].toUpperCase();
                            switch (action) {
                                //pattern "REGISTER%%userName%%password%%adminStatus"
                                case (FilmService.REGISTER_REQUEST):
                                    if (components.length == 4) {
                                        if (components[1].length() == 3) {
                                            if (CredentialVerification.checkPassword(components[2])) {
                                                try {
                                                    int adminStatus = Integer.parseInt(components[3]);
                                                    if (userManager.addUser(components[1], components[2])) {
                                                        user = new User(components[1], components[1], adminStatus);
                                                        response = FilmService.SUCCESSFUL_REGISTRATION;
                                                    } else {
                                                        response = FilmService.USERNAME_TAKEN_REGISTRATION;
                                                    }
                                                } catch (NumberFormatException ex) {
                                                    System.out.println("Invalid entry for admin status");
                                                    response = FilmService.INVALID_DETAILS_RESPONSE;
                                                }
                                            } else {
                                                response = FilmService.INVALID_PASSWORD_REGISTRATION;
                                            }
                                        } else {
                                            response = FilmService.INVALID_USERNAME_RESPONSE;
                                        }
                                    } else {
                                        response = FilmService.INVALID_DETAILS_RESPONSE;
                                    }
                                    break;
                                //pattern "LOGIN%%userName%%password"
                                case (FilmService.LOGIN_REQUEST):
                                    if (components.length == 3) {
                                        response = FilmService.FAILED_LOGIN;
                                        User u = userManager.searchByUsername(components[1]);
                                        if (u != null) {
                                            if (u.getPassword().equals(components[2])) {
                                                user = u;
                                                response = FilmService.SUCCESSFUL_LOGIN;
                                            }
                                        }
                                    } else {
                                        response = FilmService.INVALID_DETAILS_RESPONSE;
                                    }
                                    break;
                                case (FilmService.EXIT_REQUEST):
                                    response = FilmService.EXIT_RESPONSE;
                                    user = null;
                                    state = false;
                                    break;

                            }
                        } else {
                            response = FilmService.INSUFFICIENT_DETAILS_RESPONSE;
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
