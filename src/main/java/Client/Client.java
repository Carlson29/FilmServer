package Client;

import business.Film;
import business.FilmManager;
import business.FilmService;
import business.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {
    private static boolean loggedIn;

    private static User user = new User();

    private static String choice = "-1";
    private static boolean validClient = true;

    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        loggedIn = false;
        while (validClient) {
            // Requests a connection
            try (Socket dataSocket = new Socket(FilmService.HOST, FilmService.PORT)) {

                // Sets up communication lines
                // Create a Scanner to receive messages
                // Create a Printwriter to send messages
                try (Scanner input = new Scanner(dataSocket.getInputStream());
                     PrintWriter output = new PrintWriter(dataSocket.getOutputStream())) {
                    boolean validSession = true;
                    // Repeated:
                    while (validSession) {
                        // Ask user for information to be sent
                        System.out.println("Please enter a message to be sent (Send EXIT to end):");
                        String message = generateRequest(userInput);
                        if (message != null) {
                            // Send message to server
                            output.println(message);
                            // Flush message through to server
                            output.flush();

                            // Receive message from server
                            String response = input.nextLine();
                            // Display result to user
                            //System.out.println("Received from server: " + response);

                            //Register response
                            if (choice.equalsIgnoreCase("1") && response.equals(FilmService.SUCCESSFUL_REGISTRATION)) {
                                System.out.println("You're are successful register.");
                            }
                            if (choice.equalsIgnoreCase("1") && response.equals(FilmService.FAILED_REGISTRATION)) {
                                System.out.println("Register failed, please try again.");
                            }

                            //Login response
                            if (choice.equalsIgnoreCase("2") && response.equals(FilmService.SUCCESSFUL_USER_LOGIN)) {
                                System.out.println("Welcome back, login success as user.");
                                loggedIn = true;
                                user.setAdminStatus(1);
                            }
                            if (choice.equalsIgnoreCase("2") && response.equals(FilmService.SUCCESSFUL_ADMIN_LOGIN)) {
                                System.out.println("Welcome back, login success as admin.");
                                loggedIn = true;
                                user.setAdminStatus(2);
                            }
                            if (choice.equalsIgnoreCase("2") && response.equals(FilmService.FAILED_LOGIN)) {
                                System.out.println("Login failed, please try again.");
                            }

                            //Logout response
                            if (choice.equalsIgnoreCase("3") && response.equals(FilmService.SUCCESSFUL_LOGOUT_RESPONSE)) {
                                System.out.println("Logout success.");
                                validSession = false;
                                loggedIn = false;
                                choice = "-1";
                            }
                            if (choice.equalsIgnoreCase("3") && response.equals(FilmService.FAILED_LOGOUT_RESPONSE)) {
                                System.out.println("Logout failed, please try again.");
                            }

                            //Rate film response
                            if (choice.equalsIgnoreCase("4") && response.equals(FilmService.SUCCESSFUL_RATE_FILM_RESPONSE)) {
                                System.out.println("Thank you for rating this film.");
                            }
                            if (choice.equalsIgnoreCase("4") && response.equals(FilmService.INVALID_RATING_SUPPLIED_RATE_FILM_RESPONSE)) {
                                System.out.println("Please try again, rating is invalid.");
                            }
                            if (choice.equalsIgnoreCase("4") && response.equals(FilmService.NOT_LOGGED_IN_RATE_FILM_RESPONSE)) {
                                System.out.println("Please login to rate this film.");
                            }

                            //Search film by title response
                            if (choice.equalsIgnoreCase("5") && response.equals(FilmService.NO_MATCH_FOUND_RATE_FILM_RESPONSE) == false) {
                                Film decoded = Film.decode(response, FilmService.DELIMITER);
                                System.out.println(decoded);
                            }

                            //Search film by  rating
                            if (choice.equalsIgnoreCase("10") && response.equals(FilmService.NO_MATCH_FOUND_RATE_FILM_RESPONSE) == false) {
                                ArrayList<Film> f = FilmManager.decode(FilmService.filmDELIMITER, FilmService.DELIMITER, response);
                                System.out.println(f);
                            }

                            //Search film by genre response
                            if (choice.equalsIgnoreCase("6") && response.equals(FilmService.NO_MATCH_FOUND_RATE_FILM_RESPONSE) == false) {
                                ArrayList<Film> f = FilmManager.decode(FilmService.filmDELIMITER, FilmService.DELIMITER, response);
                                System.out.println(f);
                            }

                            //Add film response
                            if (choice.equalsIgnoreCase("7") && response.equals(FilmService.SUCCESSFUL_ADD_FILM_RESPONSE)) {
                                System.out.println("Film added success.");
                            }
                            if (choice.equalsIgnoreCase("7") && response.equals(FilmService.FAILED_ADD_FILM_RESPONSE)) {
                                System.out.println("Film already exist.");
                            }

                            //Remove film response
                            if (choice.equalsIgnoreCase("8") && response.equals(FilmService.SUCCESSFUL_REMOVE_FILM_RESPONSE)) {
                                System.out.println("Remove film success.");
                            }
                            if (choice.equalsIgnoreCase("8") && response.equals(FilmService.FAILED_REMOVE_FILM_RESPONSE)) {
                                System.out.println("Remove film failed because film not founded.");
                            }

                            //Insufficient permission response for add, remove film and shut down server
                            if (response.equals(FilmService.INSUFFICIENT_PERMISSIONS_REMOVE_FILM_RESPONSE)) {
                                System.out.println("Sorry, you're insufficient permission.");
                            }

                            //No film match found response for rate a film and search film by title and genre
                            if (response.equals(FilmService.NO_MATCH_FOUND)) {
                                System.out.println("Please try again, no film match found.");
                            }


                            //Exit response
                            if (response.equals(FilmService.EXIT_RESPONSE)) {
                                System.out.println("Goodbye, you're exit.");
                                loggedIn = false;
                                validSession = false;
                            }

                            //Shut down response
                            if (response.equals(FilmService.SUCCESSFUL_SHUTDOWN_RESPONSE)) {
                                System.out.println("You're successful shutdown server.");
                                loggedIn = false;
                                validSession = false;
                                validClient = false;
                            }

                            //Invalid request response
                            if (response.equals(FilmService.DEFAULT_RESPONSE)) {
                                System.out.println("Please try again, this is invalid choice.");
                            }
                        }
                    }
                }
            } catch (UnknownHostException e) {
                System.out.println("Host cannot be found at this moment. Try again later");
            } catch (IOException e) {
                System.out.println("An IO Exception occurred: " + e.getMessage());
            }
        }
        // Close connection to server
    }

    public static void displayMenu() {
        System.out.println("0) Exit");
        if (loggedIn == false) {
            System.out.println("1) Register");
            System.out.println("2) Login");
        }
        if (loggedIn == true) {
            System.out.println("3) Logout");
            System.out.println("4) Rate a film");
        }

        System.out.println("5) Search film by name");
        System.out.println("6) Search all film by genre");
        if (loggedIn == true && user.getAdminStatus()==2) {
            System.out.println("7) Add a film");
            System.out.println("8) Remove a film");
            System.out.println("9) Shut down server");
        }
        System.out.println("10) Search all film by rating");
    }

    public static String generateRequest(Scanner userInput) {
        boolean valid = false;
        String request = null;

        while (!valid) {
            displayMenu();
            choice = userInput.nextLine();
            int rating;
            String username;
            String password;
            String title;
            String genre;

            switch (choice) {
                case "0":
                    System.out.println("Exit?");
                    request = FilmService.EXIT_REQUEST;
                    break;
                case "1":
                    if (loggedIn == false) {
                        System.out.println("Register: ");
                        System.out.println("Enter username: ");
                        username = userInput.nextLine();
                        System.out.println("Enter password: ");
                        password = userInput.nextLine();
                        request = FilmService.REGISTER_REQUEST + FilmService.DELIMITER + username + FilmService.DELIMITER + password;
                    }
                    break;
                case "2":
                    if (loggedIn == false) {
                        System.out.println("Login: ");
                        System.out.println("Enter username: ");
                        username = userInput.nextLine();
                        System.out.println("Enter password: ");
                        password = userInput.nextLine();
                        request = FilmService.LOGIN_REQUEST + FilmService.DELIMITER + username + FilmService.DELIMITER + password;
                    }
                    break;
                case "3":
                    if (loggedIn == true) {
                        request = FilmService.LOGOUT_REQUEST;
                    }
                    break;
                case "4":
                    if (loggedIn == true) {
                        System.out.println("Enter film title: ");
                        title = userInput.nextLine();
                        rating = getValidRating(userInput, "Rating film from 1 to 10");
                        request = FilmService.RATE_FILM_REQUEST + FilmService.DELIMITER + title + FilmService.DELIMITER + rating;
                    }
                    break;
                case "5":
                    System.out.println("Search film by title: ");
                    System.out.println("Enter title: ");
                    title = userInput.nextLine();
                    request = FilmService.SEARCH_FILM_REQUEST + FilmService.DELIMITER + title;
                    break;
                case "6":
                    System.out.println("Search film by genre: ");
                    System.out.println("Enter genre: ");
                    genre = userInput.nextLine();
                    request = FilmService.SEARCH_FILM_BY_GENRE_REQUEST + FilmService.DELIMITER + genre;
                    break;
                case "7":
                    if (loggedIn == true && user.getAdminStatus()==2) {
                        System.out.println("Add a film: ");
                        System.out.println("Enter title: ");
                        title = userInput.nextLine();
                        System.out.println("Enter genre: ");
                        genre = userInput.nextLine();
                        request = FilmService.ADD_FILM_REQUEST + FilmService.DELIMITER + title + FilmService.DELIMITER + genre;
                    }
                    break;
                case "8":
                    if (loggedIn == true && user.getAdminStatus()==2) {
                        System.out.println("Remove a film: ");
                        System.out.println("Enter title: ");
                        title = userInput.nextLine();
                        request = FilmService.REMOVE_FILM_REQUEST + FilmService.DELIMITER + title;
                    }
                    break;
                case "9":
                    if (loggedIn == true && user.getAdminStatus()==2) {
                        System.out.println("Shut down server?");
                        request = FilmService.SHUTDOWN_REQUEST;
                    }
                    break;

                case "10":
                    System.out.println("Search film by rating: ");
                    System.out.println("Enter rating : ");
                    rating = getValidRating(userInput, "Rating film from 1 to 10");;
                    request = FilmService.SEARCH_FILM_BY_RATING + FilmService.DELIMITER + rating;
                    break;
                default:
                    System.out.println("Please select one of the stated options!");
                    System.out.println("------------------------------------");
                    continue;
            }
            valid = true;
        }
        return request;
    }

    public static int getValidRating(Scanner userInput, String prompt) {
        boolean valid = false;
        int value = 0;
        while (!valid) {
            System.out.println(prompt);
            try {
                value = userInput.nextInt();
                if (value >= 1 && value <= 10) {
                    valid = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter valid number: 1 to 10. ");
                userInput.nextLine();
            }
        }
        userInput.nextLine();
        return value;
    }

}
