
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

        //prints the host name of the machine
        System.out.println("Host name of the Machine: "+argv[0]);

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
                
                // create an output stream from the socket output stream, that will send to server
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                // create an input stream from the socket input stream, recv from server
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                
                //read a line of what the user inputed, splite the sentence into parts and store in an array
                sentence = inFromUser.readLine();
                String[] sentence_Part = sentence.trim().split(" ");
                
                //trim is use to get rid of the black spaces before the command
                if(sentence.trim().equalsIgnoreCase("help"))
                {
                    //print help menu
                    System.out.println("_______________________________________________________________________________\n");
                    System.out.println("Help Menu: \n");
                    System.out.println("Put <name> <value> <type>: add name "
                            + "record of the specific type to the name server "
                            + "database. If there an existing record with the specific"
                            + "name update it.\n");
                    System.out.println("Get <name> <type>: get the vale of the "
                            + "requested name record. If not such record was "
                            + "found return \"Not Found\".\n");
                    System.out.println("Del <name> <type>: remove the specific "
                            + "name record from the service database. If remove "
                            + "was successful return positive feedback, else "
                            + "reutn \"Not Found\".\n");
                    System.out.println("Browse: get all the name record with its"
                            + " name and type from the database. If the database"
                            + " is empty return \"Database is empty\".\n");
                    System.out.println("Exit: terminate the program and connection");
                    System.out.println("_______________________________________________________________________________\n");
                }
                else if(sentence.trim().equalsIgnoreCase("exit"))
                {
                    //exit the program
                    System.out.println("Program is exiting ...");
                    System.exit(0);
                }
                else if(sentence_Part[0].equalsIgnoreCase("put"))
                {   
                    outToServer.writeBytes(sentence + '\n');
                    System.out.println("TO SERVER: "+sentence);
                    //get the reply from the server
                    modifiedSentence = inFromServer.readLine();
                    //print the returned sentence
                    System.out.println("FROM SERVER: " + modifiedSentence);
                    //if any error is return print to terminal
                     if (modifiedSentence.startsWith("202")
                            ||modifiedSentence.startsWith("204")
                             ||modifiedSentence.startsWith("505")) {
                        System.out.println("Error: " + modifiedSentence);
                    } 
                    System.out.println("");
                    //close the socket
                    clientSocket.close();
                }
                else if(sentence_Part[0].equalsIgnoreCase("get"))
                {
                    outToServer.writeBytes(sentence + '\n'); 
                    System.out.println("TO SERVER: "+sentence);
                    //get the reply from the server
                    modifiedSentence = inFromServer.readLine();
                    //print the returned sentence
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
                        System.out.print("Value is: ");
                        for (int temp = 2; temp < parts.length; temp++) {
                            System.out.print(parts[temp] + " ");
                        }
                    }
                    System.out.println("\n");
                    //close the socket
                    clientSocket.close();
                }
                else if(sentence_Part[0].equalsIgnoreCase("del"))
                {
                    //delete was good, it was acked
                    //else it was errored
                    outToServer.writeBytes(sentence + '\n');
                    System.out.println("TO SERVER: "+sentence);
                    //get the reply from the server
                    modifiedSentence = inFromServer.readLine();
                    //print the returned sentence
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
                }
                else if(sentence_Part[0].equalsIgnoreCase("browse"))
                {
                    //for browse, you returned a message, if the message was a database empty, print, otehrwise parse
                    outToServer.writeBytes(sentence + '\n');
                    System.out.println("TO SERVER: "+sentence);
                    //get the reply from the server
                    modifiedSentence = inFromServer.readLine();
                    //print the returned sentence
                    
                   //check for error(100 = DB is empty, 505 = before declearing a type, 101 = too much or little arg )
                    if (modifiedSentence.startsWith("100") || modifiedSentence.startsWith("505")
                            || modifiedSentence.startsWith("101")) {
                        //database was empty
                        System.out.println("Error: " + modifiedSentence);
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
                    System.out.println("\n");

                    //close the socket
                    clientSocket.close();
                }
                else
                {
                    System.out.println("Invalid Command.\n");
                }
                
            }
        } catch (ConnectException e){//close the program if the server shut it self for some reason
            System.out.println("Server close unexpectedly, Program closing.");
            System.exit(0);
        }catch (IOException e) {
            System.out.println("error is " + e);
        } 
      

    }
    
    
    
    
    
}
