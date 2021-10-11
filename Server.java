import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * @auther Yongchang He
 * @profile StuID:11279620 yoh534@usask.ca
 * @date 2021-10-09 12:01 p.m.
 * @project Assignment1_Chatroom
 * @discription
 * Server is defined to:
 *      1. Start Server and accept multiple clients' connections;
 *      2. Create multi-thread object named clientHandler to receive and forward messages;
 *      3. Close Server if necessary;
 **/

public class Server {
    private final ServerSocket serverSocket;
    //private final BufferedReader bufferedReader;
    //private BufferedWriter bufferedWriter;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        //bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                System.out.println("Client '" + clientHandler.getClientUsername() + "' has entered chat!~");
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8818);
        System.out.println("Server run successfully...\nWaiting for client..");
        Server server = new Server(serverSocket);
        server.startServer();
    }
}





/*  if ("quit".equalsIgnoreCase(bufferedReader.readLine())){
                        for (int i = 0; i < 10; i++) {
        System.out.println("Server is closing...\\\r");
        Thread.sleep(100);
        System.out.println("Server is closing.../\r");
        }
        System.out.println("Server says Goodbye!");
        closeServerSocket();
        }*/
