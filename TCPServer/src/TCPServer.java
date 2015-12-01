//import static TCPServer.tokenizeCommand;
import java.io.*; // Provides for system input and output through data
                  // streams, serialization and the file system
import java.net.*; // Provides the classes for implementing networking
import java.util.ArrayList;
import java.util.Arrays;
                   // applications



// TCP Server Class
class TCPServer {
    
    static ArrayList <Record> recordsArray = new ArrayList<Record>();
    
        public static void main(String argv[]) throws Exception 
        { 
                String clientSentence = ""; 
                String capitalizedSentence = "";

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
                        
                        TCPThread newThread = new TCPThread(connectionSocket, clientSentence, recordsArray, capitalizedSentence);
                        newThread.start();
                } 
        } 
        



        
        
} 


class TCPThread extends Thread{
    
    Socket connectionSocket;
    String clientSentence;
    ArrayList <Record> recordsArray;
    String capitalizedSentence;
    
    public TCPThread(Socket conSocket, String clientSen, ArrayList <Record> records, String capitalizedSen) {
        connectionSocket = conSocket;
        clientSentence = clientSen;
        recordsArray = records;
        capitalizedSentence = capitalizedSen;
    }
    
    public void run()
    {
        try
        {
                                    
                        
                        //create an input stream from the socket input stream
                        BufferedReader inFromClient = new BufferedReader(
                           new InputStreamReader(connectionSocket.getInputStream())); 
                        
//thread
                        
                        // create an output stream from the socket output stream
                        DataOutputStream  outToClient = 
                        new DataOutputStream(connectionSocket.getOutputStream()); 

                        // read a line form the input stream
                        clientSentence = inFromClient.readLine(); 
                        
                        //this is the thing you parse
                        ArrayList<String> arrayOfCommandTokens = tokenizeCommand(clientSentence);
                        if(arrayOfCommandTokens.get(0).equalsIgnoreCase("put")){
                            Record newRecord = new Record(arrayOfCommandTokens.get(1),arrayOfCommandTokens.get(2),arrayOfCommandTokens.get(3));
                            
                            int foundRecordFlag = 0;
                            
                            //check to see if it exist, if does, then replace that one
                            for(int arrayLoop = 0; arrayLoop < recordsArray.size(); arrayLoop++)
                            {
                                if(recordsArray.get(arrayLoop).getName().equalsIgnoreCase(arrayOfCommandTokens.get(1)) && recordsArray.get(arrayLoop).getType().equalsIgnoreCase(arrayOfCommandTokens.get(2)))
                                {
                                    recordsArray.remove(arrayLoop);
                                    recordsArray.add(arrayLoop, newRecord);
                                    String ackGoodPutMessage = "200 PutOk ";
                                    System.out.println(ackGoodPutMessage);
                                    outToClient.writeBytes(ackGoodPutMessage);
                                    foundRecordFlag = 1;
                                }
                            }
                            
                            if(foundRecordFlag == 0){
                                recordsArray.add(newRecord);  
                                String ackGoodPutMessage = "200 PutOk ";
                                System.out.println(ackGoodPutMessage);
                                outToClient.writeBytes(ackGoodPutMessage);
                            }
                            else
                            {
                                //do nothing
                                String ackBadPutMessage = "202 PutNotOk ";
                                System.out.println(ackBadPutMessage);
                                outToClient.writeBytes(ackBadPutMessage);
                            }
                            
                            
                            
                        }
                        else if(arrayOfCommandTokens.get(0).equalsIgnoreCase("get")){
                            
                            //name and type
                            
                            //look through the arraylist for the name and the type, if found return to client
                            //else return a error message
                            int foundRecordFlag = 0;
                            for(int arrayLoop = 0; arrayLoop < recordsArray.size(); arrayLoop++)
                            {
                                if(recordsArray.get(arrayLoop).getName().equalsIgnoreCase(arrayOfCommandTokens.get(1)) && recordsArray.get(arrayLoop).getType().equalsIgnoreCase(arrayOfCommandTokens.get(2)))
                                {
                                    System.out.println("Sending back: "+ recordsArray.get(arrayLoop).getValue());
                                    outToClient.writeBytes(recordsArray.get(arrayLoop).getValue());
                                    foundRecordFlag = 1;
                                }
                            }
                            
                            if(foundRecordFlag == 0)
                            {
                                //send error message
                                System.out.println("404 NotFound");
                                String errorMessage = "404 NotFound";
                                outToClient.writeBytes(errorMessage);
                            }
                            else
                            {
                                //do nothing
                            }
                            
                        }
                        else if(arrayOfCommandTokens.get(0).equalsIgnoreCase("del"))
                        {
                            //name and type
                            int foundRecordFlag = 0;
                            for(int arrayLoop = 0; arrayLoop < recordsArray.size(); arrayLoop++)
                            {
                                if(recordsArray.get(arrayLoop).getName().equalsIgnoreCase(arrayOfCommandTokens.get(1)) && recordsArray.get(arrayLoop).getType().equalsIgnoreCase(arrayOfCommandTokens.get(2)))
                                {
                                    recordsArray.remove(arrayLoop);
                                    String deleteGoodMessage = "300 DeleteOk";
                                    outToClient.writeBytes(deleteGoodMessage);
                                    foundRecordFlag = 1;
                                }
                            }
                            
                            if(foundRecordFlag == 0)
                            {
                                //send error message
                                String errorMessage = "404 NotFound";
                                outToClient.writeBytes(errorMessage);
                            }
                            else
                            {
                                //do nothing
                            }
                            
                        }
                        else if(arrayOfCommandTokens.get(0).equalsIgnoreCase("browse"))
                        {
                            //return the name and type fields of all records
                            //delimiter is &
                            String s = "";
                            
                            //check if empty
                            if(recordsArray.isEmpty()){
                                String errorEmptyMessage = "100 DatabaseIsEmpty";
                                outToClient.writeBytes(errorEmptyMessage);
                            }
                            else
                            {
                                //otherwise, loop through the string and then send it over to the client
                                for(int arrayLoop = 0; arrayLoop < recordsArray.size(); arrayLoop++)
                                {
                                    s = s + recordsArray.get(arrayLoop).getName() + " " + recordsArray.get(arrayLoop).getType() + "&";
                                }
                                //once you have the entire string
                                outToClient.writeBytes(s);
                            }
                            
                            
                        }
                        else{
                            //the command was bad
                            
                        }
                        

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
        catch(Exception e)
        {
            
        }
    }
    
     static ArrayList<String> tokenizeCommand(String str)
{

    //tokenize
    String[] parts = str.split(" ");
    
    // turn list into an arraylist
     ArrayList<String> arrayOfParsedInputs = new ArrayList<String>(Arrays.asList(parts));
     
     return arrayOfParsedInputs;
    
}
}