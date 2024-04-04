package Client;

import business.FilmService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
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
                    String message= userInput.nextLine() ;
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
        System.out.println("9) Rate a film");
        System.out.println("10) Shut down server");
    }
}
