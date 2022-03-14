package client.controller;

import model.Buffer;
import model.Message;
import model.ServerUpdate;
import model.User;

import java.io.*;
import java.net.Socket;

public class ServerConnection{
    private ClientController controller;
    private Socket socket;
    private Buffer<Message> messagesToServer;
    private User user;

    //Klassen ServerConnection är klientens anslutning till servern. Den använder
    //två separata trådar, en för input och en för output. Detta för att hela tiden kunna
    //skicka meddelanden och samtidigt ta emot server uppdateringar samt meddelanden.
    public ServerConnection(String ip, int port, ClientController controller) throws IOException {
        this.controller = controller;
        messagesToServer = new Buffer<Message>();
        socket = new Socket(ip, port);
        sendMessage();
        user = new User("Philip", null);
        new ClientOutput().start();
        new ClientInput().start();
    }



    public void sendMessage(){
        Message msg = new Message("Hejsan svejsan detta är ett msg", null);
        messagesToServer.put(msg);
    }



    //Skickar meddelande till servern
    private class ClientOutput extends Thread{
        private ObjectOutputStream oos;

        public void run(){
            try {
                oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                oos.writeObject(user);
                oos.flush();
                System.out.println("Klient skickade sin användare till server");

                while(true){
                    //Väntar tills dess att tråden blir notifierad av bufferten.
                    //Den blir notifierad då det finns ett message i bufferten att hämta.
                    Message msg = messagesToServer.get();
                    oos.writeObject(msg);
                    oos.flush();

                }


            }catch (Exception e){
                System.err.println(e);
            }

        }

    }

    private class ClientInput extends Thread{
        private ObjectInputStream ois;

        public void run(){
            try{
                ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                while(true){
                    Object obj = ois.readObject();

                    if(obj instanceof ServerUpdate){
                        //Ny serverupdate. Packa upp den och visa i view.
                        System.out.println("Clienten fick en serveruppdatering!");
                    }
                    if(obj instanceof Message){
                        //Nytt meddelande. Displaya det i view.
                        System.out.println("Clienten fick ett nytt Message!");
                    }
                }

            }catch (Exception e){
                System.err.println(e);
            }



        }
    }
}