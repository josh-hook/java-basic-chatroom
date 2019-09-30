package Communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private List<ClientHandler> handlers = new ArrayList<>();
    private static final SQLHandler sql = new SQLHandler();

    public Server() {

        try {

            ServerSocket ss = new ServerSocket(8080);

            while (true) {

                Socket socket = null;
                socket = ss.accept();

                System.out.println("New Client connected");

//                Assign client handler
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());

                ClientHandler handler = new ClientHandler(socket, out, in, this);
                handlers.add(handler);
                handler.start();


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void updateClients(String text) {

        for (ClientHandler handler : handlers)
            handler.updateClient(text);

    }

    public void removeHandler(ClientHandler handler) {
        handlers.remove(handler);
    }

    public SQLHandler getSQL() {
        return sql;
    }
}
