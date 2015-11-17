import java.io.*; // Provides for system input and output through data 
                  // streams, serialization and the file system
import java.net.*; // Provides the classes for implementing networking 
                   // applications

// TCP Client class
class TCPClient {
        public static void main(String argv[]) throws Exception 
        { 
                String sentence; 
                String modifiedSentence; 

                System.out.println(argv[0]);
                
                // get the server port form command line
                int lisPort = Integer.parseInt(argv[1]);

                // create an input stream from the System.in
                BufferedReader inFromUser = 
                new BufferedReader(new InputStreamReader(System.in));
                
                // create a client socket (TCP) and connect to server
                Socket clientSocket = new Socket(InetAddress.getByName(argv[0]), lisPort);

                // create an output stream from the socket output stream
                DataOutputStream outToServer = 
                new DataOutputStream(clientSocket.getOutputStream()); 

                // create an input stream from the socket input stream
                BufferedReader inFromServer = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

                // read a line form the standard input
                sentence = inFromUser.readLine();
                
                // check if the sentence is a specific phrase, if it is, then you want to do things
                if(sentence.equalsIgnoreCase("help")){
                    //do this
                }
                else{
                    if(sentence.equalsIgnoreCase("put")){
                        
                    }
                    else{
                        if(sentence.equalsIgnoreCase("get")){
                            
                        }
                        else{
                            if(sentence.equalsIgnoreCase("del")){
                                
                            }
                            else{
                                if(sentence.equalsIgnoreCase("browse")){
                                    
                                }
                                else{
                                    if(sentence.equalsIgnoreCase("exit")){
                                        clientSocket.close();
                                    }
                                    else{
                                        //input is not a valid input command
                                    }
                                }
                            }
                        }
                    }
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

