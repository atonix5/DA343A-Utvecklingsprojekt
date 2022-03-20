package client.controller;

import client.view.LoginWindow;
import client.view.MainFrame;
import model.Buffer;
import model.Message;
import model.User;

import javax.sound.sampled.Port;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ClientController {
    private MainFrame view;
    private LoginWindow loginWindow;
    private User login;
    private ServerConnection serverConnection;

    public ClientController() throws IOException {
        LoginWindow loginWindow = new LoginWindow(new Frame("Logga in"));
        loginWindow.setVisible(true);

        if (loginWindow.authenticate()) {
            String username = loginWindow.getUsername();
            String imagePath = loginWindow.getImagePath();
            Icon icon = new ImageIcon(loginWindow.getImagePath());
            login = new User(username, new ImageIcon(imagePath));

            view = new MainFrame(this, serverConnection);


            String server = loginWindow.getServerName();
            int port = loginWindow.getPort();
            serverConnection = new ServerConnection("127.0.0.1", 721, this, login);

            login.setUsername(loginWindow.getUsername());
            login.setIcon(icon);
            view.setUser(login);
            //vet ej om detta funkar ^^^^?
        } else {
            System.exit(0);
        }


    }

    public static void main(String[] args) throws IOException {
        new ClientController();
        new ClientController();
    }

    public void clientDisconnecting() {
        serverConnection.clientDisconnecting();
    }
}
