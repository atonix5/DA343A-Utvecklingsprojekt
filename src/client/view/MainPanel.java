package client.view;

import client.controller.ClientController;
import client.controller.ServerConnection;
import model.Message;
import model.ServerUpdate;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainPanel extends JPanel {
    private ClientController controller;
    private Message msg;
    private int width;
    private int hight;

    private LPanel leftPanel; //Kontakter, användare online etc.
    private RPanel rightPanel; //Chatt och möjlighet att skriva.

    public MainPanel(ClientController controller, int width, int hight) {
        this.width = width;
        this.hight = hight;
        this.controller = controller;

        this.setSize(width, hight);
        this.setLayout(null);
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        setUp();
    }

    public void setUp(){
        leftPanel = new LPanel(controller, width / 3, hight);
        leftPanel.setLocation(0,0);
        add(leftPanel);

        rightPanel = new RPanel(controller, leftPanel,  700, hight);
        rightPanel.setLocation(width/3, 0);
        add(rightPanel);


    }

    public void serverUpdate(ServerUpdate update) {
        leftPanel.serverUpdate(update);
    }

    public void displayMessage(Message msg) {
        rightPanel.newMessage(msg);
    }

    public void setUser(User login) {
        rightPanel.setUser(login);
    }

    public void setContacts(ArrayList<String> contacts) {
        leftPanel.setContacts(contacts);
    }
}
