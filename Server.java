import java.io.IOException;
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

    // client number (Maximum number) final
    public final int maximumClientNumber = 3;

    private int currentClientNumber = 0;

    public void addCurrentClientNumber(){
        currentClientNumber++;
    }
    public void decCurrentClientNumber(){
        currentClientNumber--;
    }

    public int getCurrentClientNumber() {
        return currentClientNumber;
    }

    // member variable
    private final ServerSocket serverSocket;
    // constructor to initialize object serverSocket
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     *member method: startServer()
     * description:  create Thread object clientHandler to interact with multiple clients
     */
    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                if (currentClientNumber < maximumClientNumber) {
                    Socket socket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(socket);
                    System.out.println("Connected Client:  " + clientHandler.getClientUsername());
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                    addCurrentClientNumber();
                } else {
                    System.out.println("SERVER REACH MAXIMUM LOAD ");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * member method: closeServerSocket
     * description: to close serverSocket
     */
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param args default
     * @throws IOException
     * description: start the server, create port for client to connect, create clientHandlers
     * to handle clients' affairs.Close server if necessary.
     */
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8818);
        System.out.println("Server run successfully...\nWaiting for client..");
        Server server = new Server(serverSocket);
        server.startServer();
        server.closeServerSocket();
    }
}





