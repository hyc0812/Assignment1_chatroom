

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @auther Yongchang He
 * @profile StuID:11279620 yoh534@usask.ca
 * @date 2021-10-09 12:01 p.m.
 * @project Assignment1_Chatroom
 * @discription
 * When socket is connected, clientHandlers will receive messages from clients
 * , and forward messages to aimed clients.
 **/

public class ClientHandler implements Runnable {

    // member variables
    private Socket socket;
    // define ArrayList to hold clientHandlers
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    // member variable clientUsername hold client's name
    private String clientUsername;

    //convey currentClientNumber from class Server
    private  Server server;

    public Server getServer() {
        return server;
    }

    // getter get client's name;
    public String getClientUsername() {
        return clientUsername;
    }

    /**
     * Constructor: initialize socket, bufferedWriter, bufferedReader, use readLine()to acquire
     * client's name from terminal, and add these above to ArrayList ClientHandler
     * Server's terminal will announce a connection when a client input their name and 'enter'
     * @param socket Socket object
     */
    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);

            broadcastMessage("SERVER: '" + clientUsername + "' has entered the chat!~");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);

        }
    }

    /**
     * When socket is connected, receive message from current client and broadcast
     * message to aimed client/clients.
     */
    @Override
    public void run() {
        String messageFromClient;
       while (socket.isConnected()) {
           try {
               messageFromClient = bufferedReader.readLine();
               broadcastMessage(messageFromClient);
           } catch (IOException e) {
               closeEverything(socket, bufferedReader, bufferedWriter);
               broadcastMessage("SERVER: '" + clientUsername + "' has left");
               // decrease the number of current client
               // currently cannot decrease the number of client...
               //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
               server.decCurrentClientNumber();
               break;
           }
        }
    }

    /**
     * If the message received contains "@" in front of a username(with no blank),
     * send this message to the related user only; if the message received do not
     * contain "@", send message to all clients except the message sender.
     * @param messageToSend message from client
     */
    public void broadcastMessage(String messageToSend) {

        currentMsgToFile(messageToSend);
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (messageToSend != null) {
                    if (messageToSend.contains("@")) {
                        if (!clientHandler.clientUsername.equalsIgnoreCase(clientUsername)) {
                            if (messageToSend.contains(clientHandler.clientUsername)) {
                                String newMessageToSend = messageToSend.replaceAll("@" + clientHandler.clientUsername, "");
                                clientHandler.bufferedWriter.write(newMessageToSend);
                                clientHandler.bufferedWriter.newLine();
                                clientHandler.bufferedWriter.flush();
                            }
                        }
                    } else if (!clientHandler.clientUsername.equalsIgnoreCase(clientUsername)) {
                        clientHandler.bufferedWriter.write(messageToSend);
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();
                        // write current message to file
                        //currentMsgToFile(messageToSend);
                    }
                } else {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    /**
     * This method write current message sent by connected clients to a file named 'history.txt'.
     * and the content will be displayed will new client is connected.
     * @param messageToSend message that prepared to write into a file.
     */
    private void currentMsgToFile(String messageToSend) {
        messageToSend = messageToSend + "\n";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("history.txt", true);
            byte[] bs = (messageToSend).getBytes(StandardCharsets.UTF_8);
            if (!(messageToSend.contains("SERVER")|messageToSend.contains("@"))) {
                fos.write(bs);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Close socket and I/O stream method
     */
    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * remove clientHandler from ArrayList clientHandlers
     */
    public void removeClientHandler() {
        clientHandlers.remove(this);
    }
}