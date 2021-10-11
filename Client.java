import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * @auther Yongchang He
 * @profile StuID:11279620 yoh534@usask.ca
 * @date 2021-10-09 12:01 p.m.
 * @project Assignment1_Chatroom
 * @discription
 **/
public class Client {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String userName;

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

    public void sendMessage() {
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println("Hi! "+ userName + ", welcome to chatroom!~");

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

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter userName:");
        String userName = scanner.nextLine();
        Socket socket = new Socket("localhost", 8818);
        Client client = new Client(socket, userName);
        client.listeningForMessage();
        client.sendMessage();
    }
}
