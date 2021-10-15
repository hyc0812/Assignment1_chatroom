import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * @auther Yongchang He
 * @profile StuID:11279620 yoh534@usask.ca
 * @date 2021-10-09 12:01 p.m.
 * @project Assignment1_Chatroom
 * @discription
 * Client is similar to ClientHandler. The main purpose is to register a client to the server via
 * ClientHandler, and send and receive message via clientHandler.
 **/
public class Client {

    // member variables
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String userName;

    // constructor initialize socket and userName and other variables
    public Client(Socket socket, String userName) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * member method
     * send userName to clientHandler
     * while the socket is connected, send message to clientHandler
     */
    public void sendMessage() {
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println("Hi! "+ userName + ", welcome~");

            printHistoryMsg();


            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(userName + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * Print history message to a new client's terminal when the new client
     * first sign in the chatroom
     */
    private void printHistoryMsg() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("history.txt");
            byte[] bytes = new byte[1024];
            int readCount = 0;
            while ((readCount = fis.read(bytes)) != -1) {
                System.out.println(new String(bytes, 0, readCount));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * member method
     * receive and read message sent from clientHandler, and print the message to terminal
     */
    public void listeningForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupchat;

                while (socket.isConnected()) {
                    try {
                        msgFromGroupchat = bufferedReader.readLine();
                        System.out.println(msgFromGroupchat);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    /**
     * member method
     * close
     */
    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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
     * main method
     */
    public static void main(String[] args)  {

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter userName:");
            String userName = scanner.nextLine();
            Socket socket = null;
            socket = new Socket("localhost", 8818);
            Client client = new Client(socket, userName);
            client.listeningForMessage();
            client.sendMessage();
        } catch (IOException e) {
            System.out.println("SERVER EXCEED MAXIMUM LOAD!...\n\n You cannot log in...");
        }

    }
}
