
import java.io.*; // Provides for system input and output through data 
// streams, serialization and the file system
import java.net.*; // Provides the classes for implementing networking 
import java.util.ArrayList;
import java.util.Arrays;
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

            

                    // read a line form the standard input
            //sentence = inFromUser.readLine();
            while (true) {
                
                // create a client socket (TCP) and connect to server
                Socket clientSocket = new Socket(InetAddress.getByName(argv[0]), lisPort);
                
                // create an output stream from the socket output stream
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                // create an input stream from the socket input stream
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                
                sentence = inFromUser.readLine();
                        //parse
                //modifiedSentence = sentence.concat(" \r\n");
                //send the sentence read to the server
                outToServer.writeBytes(sentence + '\n');
                
                         
                
                
                if(sentence.trim().equalsIgnoreCase("help"))
                {
                    //print help menu
                }
                else if(sentence.trim().equalsIgnoreCase("exit"))
                {
                    System.exit(0);
                }
                else if(sentence.trim().startsWith("put"))
                {   
                    //get the reply from the server
                    modifiedSentence = inFromServer.readLine();
                    //print the returned sentence
                    System.out.println("FROM SERVER: " + modifiedSentence);
                    //close the socket
                    clientSocket.close();
                }
                else if(sentence.trim().startsWith("get"))
                {
                    //get the reply from the server
                    modifiedSentence = inFromServer.readLine();
                    //print the returned sentence
                    System.out.println("FROM SERVER: " + modifiedSentence);
                    //close the socket
                    clientSocket.close();
                }
                else if(sentence.trim().startsWith("del"))
                {
                    //delete was good, it was acked
                    //else it was errored
                    //get the reply from the server
                    modifiedSentence = inFromServer.readLine();
                    //print the returned sentence
                    System.out.println("FROM SERVER: " + modifiedSentence);
                    //close the socket
                    clientSocket.close();
                }
                else if(sentence.trim().startsWith("browse"))
                {
                    //for browse, you returned a message, if the message was a database empty, print, otehrwise parse
                    //get the reply from the server
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
                else
                {
                    outToServer.writeBytes(sentence + '\n');
                }
                
            }
        } catch (IOException e) {
            System.out.println("error is " + e);
        }

    }
    
    
    
    
    
}
