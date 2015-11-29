
import java.io.*; // Provides for system input and output through data 
// streams, serialization and the file system
import java.net.*; // Provides the classes for implementing networking 
// applications

// TCP Client class
class TCPClient {

    public static void main(String argv[]) throws Exception {
        String sentence;
        String modifiedSentence;

        String serverMessage;

        System.out.println(argv[0]);

        // get the server port form command line
        int lisPort = Integer.parseInt(argv[1]);

        try {
            // create an input stream from the System.in
            BufferedReader inFromUser
                    = new BufferedReader(new InputStreamReader(System.in));

            // create a client socket (TCP) and connect to server
            Socket clientSocket = new Socket(InetAddress.getByName(argv[0]), lisPort);

            // create an output stream from the socket output stream
            DataOutputStream outToServer
                    = new DataOutputStream(clientSocket.getOutputStream());

            // create an input stream from the socket input stream
            BufferedReader inFromServer = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

                    // read a line form the standard input
            //sentence = inFromUser.readLine();
            while (true) {
                //a user puts in a string
                if ((sentence = inFromUser.readLine()) != null) {
                    // check if the sentence is a specific phrase, if it is, then you want to do things
                    if (sentence.equalsIgnoreCase("help")) {
                        System.out.println("Help Menu:");
                        System.out.println("-----------------------------------------------------------------------------");
                        System.out.println("help: print help menu.\n");
                        System.out.println("put <name> <value> <type>: Add the name record to the service database or update an existing record.\n");
                        System.out.println("get <name> <type>: Get the value of the requested record, if not found return \"not found\".\n");
                        System.out.println("del <name> <type>: Remove a name record from service database, if name record is not found return \"not found\".\n");
                        System.out.println("browse: get all the name records in service database. Returns the name and type of the record, if the database is empty return \"Database is empty\".\n");
                        System.out.println("exit: exit the program and close the connection.\n");
                    } else if (sentence.equalsIgnoreCase("put")) {
                        // send the sentence read to the server
                        modifiedSentence = sentence.concat(" \r\n");
                        outToServer.writeBytes(sentence + '\n');

                    } else if (sentence.equalsIgnoreCase("get")) {

                    } else if (sentence.equalsIgnoreCase("del")) {

                    } else if (sentence.equalsIgnoreCase("browse")) {

                    } else if (sentence.equalsIgnoreCase("quit")) {
                        clientSocket.close();

                    } else {
                        //input is not a valid input command
                    }
                }
                else if ((serverMessage = inFromServer.readLine()) != null) {//server have a message
                    System.out.println(serverMessage);
                }
            }
        } catch (IOException e) {
            System.out.println("error is " + e);
        }

                // send the sentence read to the server
        //outToServer.writeBytes(sentence + '\n');
                // get the reply from the server
        //modifiedSentence = inFromServer.readLine();
                // print the returned sentence
        //System.out.println("FROM SERVER: " + modifiedSentence);
                // close the socket
        //clientSocket.close();
    }
}
