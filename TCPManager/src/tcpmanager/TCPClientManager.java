
import java.io.*; // Provides for system input and output through data 
// streams, serialization and the file system
import java.net.*; // Provides the classes for implementing networking 
import java.util.ArrayList;
import java.util.Arrays;
// applications

// TCP Client class
class TCPClientManager {

    public static void main(String argv[]) throws Exception {
        String sentence;
        String modifiedSentence;
        String serverMessage;
        String typeUserWants;
        int portForTypeUserWants = 0;
        Boolean hasType = false;

        //print host name of machine
        System.out.println("Host name of the machine: "+argv[0]+"\n");

        // get the server port form command line
        int lisPort = Integer.parseInt(argv[1]);

        try {
            // create an input stream from the System.in
            BufferedReader inFromUser
                    = new BufferedReader(new InputStreamReader(System.in));
            
            //The client will never stop unless exit is called
            while (true) {

                if (hasType == true) {
                    lisPort = portForTypeUserWants;
                } else {
                //lisport is just the argv from the user, before joining any types
                    lisPort = Integer.parseInt(argv[1]);
                }
                Socket clientSocket = null;
                DataOutputStream outToServer = null;

                // create a client socket (TCP) and connect to server
                // create an output stream from the socket output stream
                // create an input stream from the socket input stream
                BufferedReader inFromServer = null;

                //read the input from terminal and split the command in parts 
                sentence = inFromUser.readLine();
                String[] sentence_Part = sentence.trim().split(" ");

                //display the help menu is help is entered
                if (sentence.trim().equalsIgnoreCase("help")) {
                    System.out.println("______________________________________"
                            + "_________________________________________");
                    System.out.println("Help Menu: \n");
                    System.out.println("Put <name> <value>: add name "
                            + "record to the name server "
                            + "database. If there an existing record with the "
                            + "specific name update it.\n");
                    System.out.println("Get <name>: get the value of the "
                            + "requested name record. If not such record was "
                            + "found return \"Not Found\".\n");
                    System.out.println("Del <name>: remove the specific "
                            + "name record from the service database. If remove "
                            + "was successful return positive feedback, else "
                            + "reutn \"Not Found\".\n");
                    System.out.println("Browse: get all the name record with its"
                            + " name and type from the database. If the database"
                            + " is empty return \"Database is empty\".\n");
                    System.out.println("Type <record type>: get the specific record"
                            + " type's server address. If the record type was not"
                            + " found return \"Type not found\".\n");
                    System.out.println("Done: terminate the connection with current"
                            + " record type.\n");
                    System.out.println("Exit: terminate the program and connection.");
                    System.out.println("______________________________________"
                            + "_________________________________________\n");
                } 
                else if (sentence.trim().equalsIgnoreCase("exit")) {
                    //exit the program 
                    System.out.println("Program Exiting ...");
                    System.exit(0);
                } else if (sentence_Part[0].equalsIgnoreCase("put")) {
                    System.out.println("trying to connect to " + lisPort);
                    //try to connect to the correct server 
                    clientSocket = new Socket(
                            InetAddress.getByName(argv[0]), lisPort);
                    System.out.println("i've connected successfully to port: " 
                            + lisPort);
                    outToServer = new DataOutputStream(
                            clientSocket.getOutputStream());
                    inFromServer = new BufferedReader(new InputStreamReader(
                            clientSocket.getInputStream()));
                    // actually send the message to the server,
                    //assiming its correct
                    outToServer.writeBytes(sentence + "\n");
                    //print the message being sent
                    System.out.println("Sent: "+ sentence);
                    //print the returned sentence and read it
                    System.out.println("waiting for server");
                    modifiedSentence = inFromServer.readLine();
                    System.out.println("FROM SERVER: " + modifiedSentence);
                    //if a error is return then print it out to the terminal                    
                    //(202 = missing arg,505 = before declearing a type)
                    if (modifiedSentence.startsWith("202")
                            ||modifiedSentence.startsWith("505")) {
                        System.out.println("Error: " + modifiedSentence);
                    } 
                    System.out.println("");
                    //close the socket
                    clientSocket.close();
                } else if (sentence_Part[0].equalsIgnoreCase("get")) {
                    System.out.println("Connecting to " + lisPort);
                    //try to connect to the correct server 
                    clientSocket = new Socket(InetAddress.getByName(argv[0]), lisPort);
                    System.out.println("Connected Successfully");
                    outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    // actually send the message to the server, assiming its correct
                    outToServer.writeBytes(sentence + "\n");
                    //print the message being sent
                    System.out.println("Sent: "+ sentence);
                    //print the returned sentence
                    modifiedSentence = inFromServer.readLine();
                    System.out.println("FROM SERVER: " + modifiedSentence);
                    //if a error is return then print it out to the terminal
                    //(404 = Not found, 505 = before declearing a type, 101 = too much or little arg )
                    if (modifiedSentence.startsWith("404") 
                            || modifiedSentence.startsWith("505")
                            || modifiedSentence.startsWith("101")) {
                        System.out.println("Error: " + modifiedSentence);
                    } 
                    else {//print the value of the specific record out
                        String[] parts = modifiedSentence.split(" ");
                        System.out.print("Value of "+sentence_Part[1]+" is: ");
                        for (int temp = 2; temp < parts.length; temp++) {
                            System.out.print(parts[temp] + " ");
                        }
                    }
                    System.out.println("\n");
                    //close the socket
                    clientSocket.close();
                } else if (sentence_Part[0].equalsIgnoreCase("del")) {
                    System.out.println("Connecting to " + lisPort);
                    //try to connect to the correct server 
                    clientSocket = new Socket(InetAddress.getByName(argv[0]), lisPort);
                    System.out.println("Connected successfully");
                    outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    // actually send the message to the server, assiming its correct
                    outToServer.writeBytes(sentence + "\n");
                    //print the message being sent                   
                    System.out.println("Sent: "+ sentence);
                    //print the returned sentence
                    modifiedSentence = inFromServer.readLine();
                    //print the ack message
                    System.out.println("FROM SERVER: " + modifiedSentence);
                    //print to the terminal if a error message is returned
                    //(404 = Not found, 505 = before declearing a type, 101 = too much or little arg )
                    if (modifiedSentence.startsWith("404") 
                            || modifiedSentence.startsWith("505")
                            || modifiedSentence.startsWith("101")) {
                        System.out.println("Error: " + modifiedSentence);
                    }
                    System.out.println("");
                    //close the socket
                    clientSocket.close();
                } else if (sentence_Part[0].equalsIgnoreCase("browse")) {
                    System.out.println("Connecting to" + lisPort);
                    //try to connect to the correct server 
                    clientSocket = new Socket(InetAddress.getByName(argv[0]), lisPort);
                    System.out.println("Connected successfully");
                    outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    // actually send the message to the server, assiming its correct
                    outToServer.writeBytes(sentence + "\n");
                    //print the message being sent    
                    System.out.println("Sent: "+ sentence);
                    // read bytes
                    modifiedSentence = inFromServer.readLine();
                    //check for error(100 = DB is empty, 505 = before declearing a type, 101 = too much or little arg )
                    if (modifiedSentence.startsWith("100") || modifiedSentence.startsWith("505")
                            || modifiedSentence.startsWith("101")) {
                        //database was empty
                        System.out.println("Error: " + modifiedSentence);
                    } else {
                        //parse the command that came back and then print out all the stuff onto the screen
                        //tokenize
                        String[] parts = modifiedSentence.split("&");
                        // turn list into an arraylist
                        ArrayList<String> arrayOfSentenceInputs = new ArrayList<String>(Arrays.asList(parts));
                        //print all the records' name and type to the terminal
                        for (int print = 0; print < arrayOfSentenceInputs.size(); print++) {
                            System.out.println(arrayOfSentenceInputs.get(print));
                        }
                    }
                    System.out.println("");

                    //close the socket
                    clientSocket.close();
                } else if (sentence_Part[0].equalsIgnoreCase("type")) {
                    //only allow type's functionality to work if and only if the client is not in a type group
                    if (hasType == false) {
                        System.out.println("Trying to connect to port" + lisPort);
                        clientSocket = new Socket(InetAddress.getByName(argv[0]), lisPort);
                        System.out.println("connect success");
                        outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                        //split on the space and then store the type the person wants
                        String[] typeParts = sentence.split(" ");

                        //1st token has the type that the user is looking for
                        typeUserWants = typeParts[1];

                        //once the type is recieved, you want to send to the manager to get the port back
                        String sendSentence = "Type " + typeUserWants;
                        outToServer.writeBytes(sendSentence + '\n');
                        System.out.println("Sending to Server: " + sendSentence);
                        //check what comes back from the server manager
                        modifiedSentence = inFromServer.readLine();
                        System.out.println("Message from Server: " + modifiedSentence);
                        if (modifiedSentence.startsWith("500")) {
                            //port was not found
                            System.out.println("Error: " + modifiedSentence + "\n");
                        } else {
                            //Port somenumber
                            String[] portParts = modifiedSentence.split(" ");
                            portForTypeUserWants = Integer.parseInt(portParts[1]);
                            hasType = true;
                            System.out.println("we got a response! let's connect to the server");
                        }
                        System.out.println("");
                        //close the socket
                        clientSocket.close();
                    } else {//print out the error message that the client is in a type group
                        System.out.println("Already inside a specific type, cannot change type before calling done.\n");
                    }
                } else if (sentence.trim().startsWith("done")) {
                    //change hastype so type can be use again
                    hasType = false;
                    System.out.println("No longer in any type group.\n\n");
                } else {
                    System.out.println("invlaid arguments\n");
                }

            }
        } catch (ConnectException e){//close the program if the server shut it self for some reason
            System.out.println("Server close unexpectedly, Program closing.");
            System.exit(0);
        }catch (IOException e) {//catch all the input and output exceptions
            System.out.println("error is " + e);
        }

    }

}
