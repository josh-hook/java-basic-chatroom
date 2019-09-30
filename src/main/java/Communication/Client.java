package Communication;

import GUI.ClientWindow;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean running = true;
    private ClientWindow GUI;
    private String name;

    public Client(ClientWindow GUI, String name) {
        this.GUI = GUI;
        this.name = name;

        try {

            socket = new Socket("192.168.1.176", 8080);

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        notifySystem(name, true);

        while (running) {

            try {

                String data = in.readUTF();

                if (data.startsWith("UPDATE")) { //UPDATE format: 'UPDATE:name:text:r:g:b'

                    String[] array = data.split(":");

                    String name = array[1];
                    String text = array[2];
                    Color colour = new Color(Float.parseFloat(array[3]), Float.parseFloat(array[4]), Float.parseFloat(array[5]));

                    GUI.updateChat(name, text, colour);

                } else if (data.equals("DISCONNECT")) {

                    running = false;
                    out.flush();
                    out.close();
                    in.close();
                    socket.close();

                } else if (data.startsWith("SYSTEM")) { //SYSTEM format: 'SYSTEM:text'

                    String[] array = data.split(":");
                    String text = array[1];

                    GUI.updateChat("System", text, Color.RED);

                } else if (data.startsWith("LOGIN")) { // 'LOGIN:message'

                    GUI.setLoginMessage(data.split(":")[1]);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void sendUpdate(String text) {

        try {

            out.writeUTF("UPDATE:"+name+":"+text);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void notifySystem(String name, boolean status) {

        try {

            if (status) //Connected
                out.writeUTF("SYSTEM:" + name + " has arrived in the Chatroom!");
            else
                out.writeUTF("SYSTEM:" + name + " has left the Chatroom!");

            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void disconnect() {
        try {

            notifySystem(name, false);
            out.writeUTF("DISCONNECT");
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(String username, char[] password) {

        try {
            out.writeUTF("LOGIN:"+username+":"+password.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void updateName(String name) {
        this.name = name;
    }

}
