import java.io.*; // Provides for system input and output through data
                  // streams, serialization and the file system
import java.net.*; // Provides the classes for implementing networking
                   // applications

// TCP Server Class
class TCPServer {
        public static void main(String argv[]) throws Exception 
        { 
                String clientSentence; 
                String capitalizedSentence;

                //System.out.println(argv[0]);

                // get the port number assigned from the command line
                //int lisPort = Integer.parseInt(argv[0]);
                int lisPort = 0;
                // create a server socket (TCP)
                ServerSocket welcomeSocket = new ServerSocket(lisPort); 
                System.out.println(welcomeSocket.getLocalPort());
                // loop infinitely (process clients sequentially)
                while(true) {
                        // Wait and accept client connection
                        Socket connectionSocket = welcomeSocket.accept(); 

                        //create an input stream from the socket input stream
                        BufferedReader inFromClient = new BufferedReader(
                           new InputStreamReader(connectionSocket.getInputStream())); 

                        // create an output stream from the socket output stream
                        DataOutputStream  outToClient = 
                        new DataOutputStream(connectionSocket.getOutputStream()); 

                        // read a line form the input stream
                        clientSentence = inFromClient.readLine(); 

                        // capitalize the sentence
                        capitalizedSentence = clientSentence.toUpperCase() + '\n'; 

                        System.out.println("Hello world!");
                        System.out.println("input is: " + clientSentence);
                        System.out.println("output is: " + capitalizedSentence);
                        
                        // send the capitalized sentence back to the  client
                        outToClient.writeBytes(capitalizedSentence);

                        // close the connection socket
                        connectionSocket.close();
                } 
        } 
} 
