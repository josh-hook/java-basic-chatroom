package Communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class ClientHandler extends Thread {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Server server;
    private boolean running = true;

    public ClientHandler(Socket socket, DataOutputStream out, DataInputStream in, Server server) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.server = server;
    }

    public void run() {

//        Assign the client its random text colour.
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

        while (running) {

            try {

                String data = in.readUTF();

                if (data.startsWith("UPDATE")) { //UPDATE format: 'UPDATE:name:text:r:g:b]'

                    server.updateClients(data + ":"+r+":"+g+":"+b);

                } else if (data.equals("DISCONNECT")) { //DISCONNECT format: 'DISCONNECT'

                    out.writeUTF("DISCONNECT");

                    running = false;
                    server.removeHandler(this);

                    out.flush();
                    out.close();
                    in.close();
                    socket.close();

                } else if (data.startsWith("SYSTEM")) { //SYSTEM format: 'SYSTEM:text'

                    server.updateClients(data);

                } else if (data.startsWith("LOGIN")) { // 'LOGIN:username:password'

                    String[] message = data.split(":");
                    login(message[1], message[2].toCharArray());

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void updateClient(String text) {

        try {

            out.writeUTF(text);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(String username, char[] password) {

        String userExists = server.getSQL().getUser(username, password);

        if (userExists.equals("DOESNT EXIST")) {

            server.getSQL().storeUser(username, password);

            userExists = "EXISTS";

        }

        try {

            out.writeUTF("LOGIN:"+userExists);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
