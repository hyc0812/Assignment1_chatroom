<img src="/home/yoh534/Screenshot from 2021-10-17 18-11-45.png" style="zoom: 30%;" />

## Assignment 1   

### Design chat rooms using Tcp socket and multi-thread in java

*name: 	Yongchang He* 

*StuID: 	11279620*



#### *Manual and test report*

**Manual**

This project is designed in java. It has three components: Server, Client, and ClientHandler. 

**Server** is defined to start Server and accept multiple clients' connections from local or remote; create multi-thread client-handler to hold user names, and receive and forward message; close server if necessary. It allows clients to create chat, list all users in the terminal, join the multi-user chatroom to send message, and leave a chatroom.

The class **ClientHandler** is to hold clients' information(name), receive clients' message  and forward clients' message to aimed users. Message can be forwarded publicly and privately.

The class **Client** is much similar as ClientHandler. Except for handling message, the Client provide users with terminal to register their names, and send message to one or more existing client/clients, and leave their chat any time they want.

***How to use the code?*** 

1. The computer should have java [jdk version 8](https://www.oracle.com/java/technologies/downloads/) or higher;

2. Pay attention to the current directory where you put your java code. Use the command " pwd" (Linux) to show your current directory, and paste your own directory to parameter "dir" in the class CurrentDir(you can find this class in the file "ClientHandler.java" line 21).

   ```java
   class CurrentDir {
       // REMINDER!!
       // need first to substitute dir with your own directory for the code
       private static final String dir = "/home/yoh534/IdeaProjects/Assignment1_Chatroom/src/";
     ......
   }
   ```

   ![](/home/yoh534/Screenshot from 2021-10-17 22-38-16.png)

3. Use IDE tools or Terminal to run the code. Copy  the code to the targeted directory and run . If using the terminal(s), first run the Server.java, and then the Client.java. Client.java can be launched up to ten times parallel .



**Test report**

1. Server should run first by execute commands below.

```java
Javac Server.java
java Server
```

![](/home/yoh534/Screenshot from 2021-10-17 21-07-15.png)

Server is running successfully, and start listening to clients' connection.

2. Run Client  to create users, we can create up to ten users by running the Client.java up to ten times. When the client number is about to exceed the Maximum number, the Server will give out the alarm, and new client cannot proceed successful registration.

```java
Javac Server.java
java Server
```

![](/home/yoh534/Screenshot from 2021-10-17 21-38-45.png)

- After clients have registered their name to the chatroom successfully, they will get greetings from the Server. And they can also get announcement of other clients' arrival .At the same time, the server can get the clients' name after clients' registration.

![](/home/yoh534/Screenshot from 2021-10-17 21-30-47.png)

- Clients in their chatroom can send message to other clients publicly. In such situation, every one in the chatroom can receive the message. 

- > E.g. the user 'yong' sent a message to the other two client, and the other two client can receive the very message at the same time.

![](/home/yoh534/Screenshot from 2021-10-17 21-56-32.png)

- If client 'Jamie' want to talk to 'Cozy67' secretly, just talk to Cozy67 by adding the '@' in front of the name(and a blank after the name) . By doing this, only 'Cozy67' can see the message, and 'yong' will not receive their private talk. 

![](/home/yoh534/Screenshot from 2021-10-17 22-00-46.png)



- A new comer can get all public history messages ('yong' and 'Cozy67' 's message) before his/her arrival. All the historical message is stored in the file called 'history.txt' by IO stream in the same directory.

![](/home/yoh534/Screenshot from 2021-10-17 22-07-34.png)

- A client can join and leave the chat room whenever he/ she wants to. Other client currently in the chat room will get a message from the server when someone comes or leaves.

![](/home/yoh534/Screenshot from 2021-10-17 22-10-57.png)

- If the number of clients exceed 10, the server will ignore new client's connections, and the new client cannot log in. 

![](/home/yoh534/Screenshot from 2021-10-17 22-24-58.png)

