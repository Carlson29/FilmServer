package Client;

import business.FilmService;
import business.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLOutput;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {
    private static boolean loggedIn;

    private static User user = new User();
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        boolean validClient = true;
        loggedIn = false;
        while(validClient){
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
                    if (message!=null){
                        // Send message to server
                        output.println(message);
                    // Flush message through to server
                    output.flush();

                    // Receive message from server
                    String response = input.nextLine();
                    // Display result to user
                    System.out.println("Received from server: " + response);
                    if (response.equals(FilmService.SUCCESSFUL_LOGOUT_RESPONSE)) {
                        validSession = false;
                        loggedIn = false;
                    }
                    if (response.equals(FilmService.SUCCESSFUL_SHUTDOWN_RESPONSE)) {
                        loggedIn = false;
                        validSession = false;
                        validClient = false;
                    }
                    if (response.equals(FilmService.EXIT_RESPONSE)) {
                        loggedIn = false;
                        validSession = false;
                    }
                    if ( response.equals(FilmService.SUCCESSFUL_USER_LOGIN)) {
                        loggedIn = true;
                        user.setAdminStatus(1);
                    }
                        if (response.equals(FilmService.SUCCESSFUL_ADMIN_LOGIN)) {
                            loggedIn = true;
                            user.setAdminStatus(2);
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

    public static void displayMenu(){
        System.out.println("0) Exit");
        if(loggedIn==false) {
            System.out.println("1) Register");
            System.out.println("2) Login");
        }
        if(loggedIn==true) {
            System.out.println("3) Logout");
            System.out.println("4) Rate a film");
        }

        System.out.println("5) Search film by name");
        System.out.println("6) Search all film by genre");
        if (loggedIn==true && user.getAdminStatus()==2) {
            System.out.println("7) Add a film");
            System.out.println("8) Remove a film");
            System.out.println("9) Shut down server");
        }
    }

    public static String generateRequest(Scanner userInput){
        boolean valid = false;
        String request = null;

        while(!valid) {
            displayMenu();
            String choice = userInput.nextLine();
            int rating;
            String username;
            String password ;
            String title ;
            String genre ;

            switch (choice) {
                case "0":
                    System.out.println("Exit?");
                    request = FilmService.EXIT_REQUEST;
                    break;
                case "1":
                    if(loggedIn==false) {
                        System.out.println("Register: ");
                        System.out.println("Enter username: ");
                        username = userInput.nextLine();
                        System.out.println("Enter password: ");
                        password = userInput.nextLine();
                        request = FilmService.REGISTER_REQUEST + FilmService.DELIMITER + username + FilmService.DELIMITER + password;
                    }
                    break;
                case "2":
                    if(loggedIn==false) {
                        System.out.println("Login: ");
                        System.out.println("Enter username: ");
                        username = userInput.nextLine();
                        System.out.println("Enter password: ");
                        password = userInput.nextLine();
                        request = FilmService.LOGIN_REQUEST + FilmService.DELIMITER + username + FilmService.DELIMITER + password;
                    }
                    break;
                case "3":
                    if(loggedIn==true) {
                        request = FilmService.LOGOUT_REQUEST;
                    }
                    break;
                case "4":
                    if(loggedIn==true) {
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
                    if(loggedIn==true  && user.getAdminStatus()==2) {
                        System.out.println("Add a film: ");
                        System.out.println("Enter title: ");
                        title = userInput.nextLine();
                        System.out.println("Enter genre: ");
                        genre = userInput.nextLine();
                        request = FilmService.ADD_FILM_REQUEST + FilmService.DELIMITER + title + FilmService.DELIMITER + genre;
                    }
                    break;
                case "8":
                    if(loggedIn==true  && user.getAdminStatus()==2) {
                        System.out.println("Remove a film: ");
                        System.out.println("Enter title: ");
                        title = userInput.nextLine();
                        request = FilmService.REMOVE_FILM_REQUEST + FilmService.DELIMITER + title;
                    }
                    break;
                case "9":
                    if(loggedIn==true  && user.getAdminStatus()==2) {
                        System.out.println("Shut down server?");
                        request = FilmService.SHUTDOWN_REQUEST;
                    }
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
        while(!valid) {
            System.out.println(prompt);
            try {
                value = userInput.nextInt();
                if (value >= 1 && value <= 10){
                    valid = true;
                }
            }catch(InputMismatchException e){
                System.out.println("Please enter valid number: 1 to 10. ");
                userInput.nextLine();
            }
        }
        userInput.nextLine();
        return value;
    }

}
