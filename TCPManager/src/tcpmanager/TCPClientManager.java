
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

        System.out.println(argv[0]);

        // get the server port form command line
        int lisPort = Integer.parseInt(argv[1]);

        try {
            // create an input stream from the System.in
            BufferedReader inFromUser
                    = new BufferedReader(new InputStreamReader(System.in));

            

                    // read a line form the standard input
            //sentence = inFromUser.readLine();
            while (true) {
                
                if(hasType == true)
                {
                    lisPort =  portForTypeUserWants;
                }
                else
                {
                    lisPort = Integer.parseInt(argv[1]);
                    //lisport is just the argv from the user
                }
                Socket clientSocket = null;
                DataOutputStream outToServer = null;
                
                // create a client socket (TCP) and connect to server
                // create an output stream from the socket output stream
                

                // create an input stream from the socket input stream
                BufferedReader inFromServer = null;
                
                sentence = inFromUser.readLine();
                //parse
                //modifiedSentence = sentence.concat(" \r\n");
                //send the sentence read to the server
                //outToServer.writeBytes(sentence + '\n');
                
                if(sentence.trim().equalsIgnoreCase("help"))
                {
                    //print help menu
                    System.out.println("<Print help menu>");
                }
                else if(sentence.trim().equalsIgnoreCase("exit"))
                {
                    System.out.println("<Exiting>");
                    System.exit(0);
                }
                else if(sentence.trim().startsWith("put"))
                {   
                    //get the reply from the server
                    System.out.println("put case");
                    System.out.println("waka waka tryna connect" + lisPort);
                    clientSocket = new Socket(InetAddress.getByName(argv[0]), lisPort);
                    System.out.println("i've connected successfully");
                    outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    //modifiedSentence = inFromServer.readLine();
                    // actually send the message to the server, assiming its correct
                    outToServer.writeBytes(sentence + "\n");
                    System.out.println("I have sent things to the server");
                    //print the returned sentence
                    System.out.println("wating for server input");
                    modifiedSentence = inFromServer.readLine();
                    System.out.println("ive got server input");
                    System.out.println("FROM SERVER: " + modifiedSentence);
                    //close the socket
                    clientSocket.close();
                }
                else if(sentence.trim().startsWith("get"))
                {
                    //get the reply from the server
                    System.out.println("waka waka tryna connect" + lisPort);
                    clientSocket = new Socket(InetAddress.getByName(argv[0]), lisPort);
                    System.out.println("i've connected successfully");
                    outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    outToServer.writeBytes(sentence + "\n");
                    //print the returned sentence
                    modifiedSentence = inFromServer.readLine();
                    System.out.println("FROM SERVER: " + modifiedSentence);
                    //close the socket
                    clientSocket.close();
                }
                else if(sentence.trim().startsWith("del"))
                {
                    //delete was good, it was acked
                    //else it was errored
                    //get the reply from the server
                    //modifiedSentence = inFromServer.readLine();
                    System.out.println("waka waka tryna connect" + lisPort);
                    clientSocket = new Socket(InetAddress.getByName(argv[0]), lisPort);
                    System.out.println("i've connected successfully");
                    outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    outToServer.writeBytes(sentence + "\n");
                    //print the returned sentence
                    modifiedSentence = inFromServer.readLine();
                    System.out.println("FROM SERVER: " + modifiedSentence);
                    //close the socket
                    clientSocket.close();
                }
                else if(sentence.trim().startsWith("browse"))
                {
                    //for browse, you returned a message, if the message was a database empty, print, otehrwise parse
                    //send things to the server
                    System.out.println("waka waka tryna connect" + lisPort);
                    clientSocket = new Socket(InetAddress.getByName(argv[0]), lisPort);
                    System.out.println("i've connected successfully");
                    outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    outToServer.writeBytes(sentence + "\n");
                    // read bytes
                    modifiedSentence = inFromServer.readLine();
                    //print the returned sentence
                    
                    //check first number
                    if(modifiedSentence.startsWith("100"))
                    {
                        //database was empty
                        System.out.println("FROM SERVER: " + modifiedSentence);
                    }
                    else
                    {
                        //parse the command that came back and then print out all the stuff onto the screen
                        //tokenize
                        String[] parts = modifiedSentence.split("&");
    
                        // turn list into an arraylist
                        ArrayList<String> arrayOfSentenceInputs = new ArrayList<String>(Arrays.asList(parts));
                        
                        for(int print = 0; print < arrayOfSentenceInputs.size(); print++)
                        {
                            System.out.println(arrayOfSentenceInputs.get(print));
                        }
                    }
                    
                    
                    //close the socket
                    clientSocket.close();
                }
                else if(sentence.trim().startsWith("type") && hasType == false)
                {
                    //modifiedSentence = inFromServer.readLine();
                    //split on the space and then store the type the person wants
                    System.out.println("waka waka tryna connect" + lisPort);
                    clientSocket = new Socket(InetAddress.getByName(argv[0]), lisPort);
                    System.out.println("i've connected successfully");
                    outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    System.out.println("ayy must be the money");
                    String[] typeParts = sentence.split(" ");
                    
                    //1st token has the type that the user is looking for
                    typeUserWants = typeParts[1];
                    
                    //once the type is recieved, you want to send to the manager to get the port back
                    String sendSentence = "Type " + typeUserWants;
                    outToServer.writeBytes(sendSentence + '\n');
                    System.out.println("i've sent bytes");
                    //check what comes back from the server manager
                    modifiedSentence = inFromServer.readLine();
                    if(modifiedSentence.startsWith("500"))
                    {
                        //port was not found
                        System.out.println("Port was not found for the type that was specifed by the user.");
                    }
                    else
                    {
                        //Port somenumber
                        String[] portParts = modifiedSentence.split(" ");
                        portForTypeUserWants = Integer.parseInt(portParts[1]);
                        hasType = true;
                        System.out.println("we got a response! let's connect to the server");
                    }
                    //close the socket
                    clientSocket.close();
                    System.out.println("im closing the connection now");
                }
                else if(sentence.trim().startsWith("done"))
                {
                    System.out.println("<done is not supported yet>");
                    hasType = false;
                }
                else
                {
                    System.out.println("invlaid arguments");
                }
                
                //clientSocket.close();
                
            }
        } catch (IOException e) {
            System.out.println("error is " + e);
        }

    }
    
    
    
    
    
}
