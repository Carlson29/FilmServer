package Client;

import business.FilmService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLOutput;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        // Requests a connection
        try (Socket dataSocket = new Socket(FilmService.HOST,FilmService.PORT)) {

            // Sets up communication lines
            // Create a Scanner to receive messages
            // Create a Printwriter to send messages
            try (Scanner input = new Scanner(dataSocket.getInputStream());
                 PrintWriter output = new PrintWriter(dataSocket.getOutputStream())) {
                boolean validSession = true;
                // Repeated:
                while(validSession) {
                    // Ask user for information to be sent
                    System.out.println("Please enter a message to be sent (Send EXIT to end):");
                    String message = generateRequest(userInput);
                    // Send message to server
                    output.println(message);
                    // Flush message through to server
                    output.flush();

                    // Receive message from server
                    String response = input.nextLine();
                    // Display result to user
                    System.out.println("Received from server: " + response);
                    /*if(response.equals(FilmService.EXIT_RESPONSE)){
                        validSession = false;
                    }*/
                }

            }

        } catch (UnknownHostException e) {
            System.out.println("Host cannot be found at this moment. Try again later");
        } catch (IOException e) {
            System.out.println("An IO Exception occurred: " + e.getMessage());
        }
        // Close connection to server
    }

    public static void displayMenu(){
        System.out.println("0) Exit");
        System.out.println("1) Register");
        System.out.println("2) Login");
        System.out.println("3) Logout");
        System.out.println("4) Rate a film");
        System.out.println("5) Search film by name");
        System.out.println("6) Search all film by genre");
        System.out.println("7) Add a film");
        System.out.println("8) Remove a film");
        System.out.println("9) Shut down server");
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
                    System.out.println("Please enter username and password to register: ");
                    username = userInput.nextLine();
                    password = userInput.nextLine();
                    request = FilmService.REGISTER_REQUEST + FilmService.DELIMITER + username + FilmService.DELIMITER + password;
                    break;
                case "2":
                    System.out.println("Please enter username and password to login: ");
                    username = userInput.nextLine();
                    password = userInput.nextLine();
                    request = FilmService.LOGIN_REQUEST + FilmService.DELIMITER + username + FilmService.DELIMITER + password;
                    break;
                case "3":
                    request = FilmService.LOGOUT_REQUEST;
                    break;
                case "4":
                    rating = getValidRating(userInput,"Rating a film from 1 to 10");
                    title = userInput.nextLine();
                    request = FilmService.RATE_FILM_REQUEST + FilmService.DELIMITER + title + FilmService.DELIMITER + rating;
                    break;
                case "5":
                    System.out.println("Search film by title: ");
                    title = userInput.nextLine();
                    request = FilmService.SEARCH_FILM_REQUEST + FilmService.DELIMITER + title;
                    break;
                case "6":
                    System.out.println("Search film by genre: ");
                    genre = userInput.nextLine();
                    request = FilmService.SEARCH_FILM_BY_GENRE_REQUEST + FilmService.DELIMITER + genre;
                    break;
                case "7":
                    System.out.println("Add a film: ");
                    title = userInput.nextLine();
                    genre = userInput.nextLine();
                    request = FilmService.ADD_FILM_REQUEST + FilmService.DELIMITER + title + FilmService.DELIMITER + genre;
                    break;
                case "8":
                    System.out.println("Remove a film: ");
                    title = userInput.nextLine();
                    request = FilmService.REMOVE_FILM_REQUEST + FilmService.DELIMITER + title;
                    break;
                case "9":
                    System.out.println("Shut down server?");
                    request = FilmService.SHUTDOWN_REQUEST;
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
