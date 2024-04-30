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
    private static boolean serverState = true;


    public static void main(String[] args) {

        try (ServerSocket listeningSocket = new ServerSocket(FilmService.PORT)) {

            filmManager = new FilmManager();
            userManager = new UserManager();
            while (serverState) {
                Socket dataSocket = listeningSocket.accept();
                ClientHandler clientHandler = new ClientHandler(dataSocket, filmManager, userManager);
                Thread wrapper = new Thread(clientHandler);
                wrapper.start();
                serverState = ClientHandler.isServerState();

            }

        } catch (BindException e) {
            System.out.println("BindException occurred when attempting to bind to port " + FilmService.PORT);
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException occurred on server socket");
            System.out.println(e.getMessage());
        }

    }


    public static void setServerState(boolean serverState) {
        FilmServer.serverState = serverState;
    }


}
