//import static TCPServer.tokenizeCommand;

import java.io.*; // Provides for system input and output through data
// streams, serialization and the file system
import java.net.*; // Provides the classes for implementing networking
import java.util.ArrayList;
import java.util.Arrays;
                   // applications

// TCP Server Class
class TCPServer {

    static ArrayList<Record> recordsArray = new ArrayList<Record>();

    public static void main(String argv[]) throws Exception {
        String clientSentence = "";
        String capitalizedSentence = "";

        // get the port number that is free to use
        int lisPort = 0;
        // create a server socket (TCP)
        ServerSocket welcomeSocket = new ServerSocket(lisPort);
        System.out.println("Server runs on port: " + welcomeSocket.getLocalPort()+"\n");
        // loop infinitely (process clients sequentially)
        while (true) {
            // Wait and accept client connection
            Socket connectionSocket = welcomeSocket.accept();

            //create thread that will do the work
            TCPThread newThread = new TCPThread(connectionSocket, clientSentence, recordsArray, capitalizedSentence);
            newThread.start();
        }
    }

}

class TCPThread extends Thread {

    Socket connectionSocket;
    String clientSentence;
    ArrayList<Record> recordsArray;
    String capitalizedSentence;

    public TCPThread(Socket conSocket, String clientSen, ArrayList<Record> records, String capitalizedSen) {
        connectionSocket = conSocket;
        clientSentence = clientSen;
        recordsArray = records;
        capitalizedSentence = capitalizedSen;
    }

    public void run() {
        try {

            //create an input stream from the socket input stream
            BufferedReader inFromClient = new BufferedReader(
                    new InputStreamReader(connectionSocket.getInputStream()));

            // create an output stream from the socket output stream
            DataOutputStream outToClient
                    = new DataOutputStream(connectionSocket.getOutputStream());

            // read a line form the input stream
            clientSentence = inFromClient.readLine();

            //this is the thing you parse
            ArrayList<String> arrayOfCommandTokens = tokenizeCommand(clientSentence);
            if (arrayOfCommandTokens.get(0).equalsIgnoreCase("put")) {
                //make sure there is something for value, type and name
                if (arrayOfCommandTokens.size() > 3) {

                    //the following if check is the type is one of the four: NS, A, CNAME, MX
                    if ((arrayOfCommandTokens.get(3).equalsIgnoreCase("NS"))
                            || (arrayOfCommandTokens.get(3).equalsIgnoreCase("A"))
                            || (arrayOfCommandTokens.get(3).equalsIgnoreCase("CNAME"))
                            || (arrayOfCommandTokens.get(3).equalsIgnoreCase("MX"))) {

                        Record newRecord;
                        
                        newRecord = new Record(arrayOfCommandTokens.get(1), arrayOfCommandTokens.get(2), arrayOfCommandTokens.get(3));

                        int foundRecordFlag = 0;

                        //check to see if it exist, if does, then replace that one
                        for (int arrayLoop = 0; arrayLoop < recordsArray.size(); arrayLoop++) {
                            if (recordsArray.get(arrayLoop).getName().equalsIgnoreCase(arrayOfCommandTokens.get(1)) && recordsArray.get(arrayLoop).getType().equalsIgnoreCase(arrayOfCommandTokens.get(3))) {
                                recordsArray.remove(arrayLoop);
                                recordsArray.add(arrayLoop, newRecord);
                                String ackGoodPutMessage = "200 PutOk ";
                                System.out.println("Sending back: "+ackGoodPutMessage+"\n");
                                outToClient.writeBytes(ackGoodPutMessage + "\n");
                                foundRecordFlag = 1;
                            }
                        }

                        //if there is not existing record create new ones
                        if (foundRecordFlag == 0) {
                            recordsArray.add(newRecord);
                            String ackGoodPutMessage = "200 PutOk ";
                            System.out.println("Sending back: "+ackGoodPutMessage+"\n");
                            outToClient.writeBytes(ackGoodPutMessage + "\n");
                        }
                    }
                    else{//send back an error message that the input type is invalid
                        outToClient.writeBytes("204 Not a Valid Type\n");
                        System.out.println("Sending back: 204 Not a Valid Type\n");

                    }
                } else {//missing arg
                    String ackBadPutMessage = "202 PutNotOk, missing name and/or value of the record. ";
                    System.out.println("Sending back: "+ackBadPutMessage+"\n");
                    outToClient.writeBytes(ackBadPutMessage + "\n");

                }

            } else if (arrayOfCommandTokens.get(0).equalsIgnoreCase("get")) {
                //make sure there is enough arg get, name and type
                if (arrayOfCommandTokens.size() == 3) {

                    //look through the arraylist for the name and the type, if found return to client
                    //else return a error message
                    int foundRecordFlag = 0;
                    for (int arrayLoop = 0; arrayLoop < recordsArray.size(); arrayLoop++) {
                        if (recordsArray.get(arrayLoop).getName().equalsIgnoreCase(arrayOfCommandTokens.get(1)) && recordsArray.get(arrayLoop).getType().equalsIgnoreCase(arrayOfCommandTokens.get(3))) {
                            System.out.println("Sending back: " + recordsArray.get(arrayLoop).getValue()+"\n");
                            outToClient.writeBytes("400 GetOK " + recordsArray.get(arrayLoop).getValue() + "\n");
                            foundRecordFlag = 1;
                        }
                    }
                    
                    //didn't find the record send back an error
                    if (foundRecordFlag == 0) {
                        //send error message
                        System.out.println("404 Not Found");
                        String errorMessage = "404 Not Found";
                        outToClient.writeBytes("Sending back: "+errorMessage + "\n");
                    }
                } else {
                    outToClient.writeBytes("101 Too much/little argument" + "\n");
                    System.out.println("Sending back: "+"101 Too much/little argument\n");
                }
            } else if (arrayOfCommandTokens.get(0).equalsIgnoreCase("del")) {
                //make sure there is enough arg (del, name and type)
                if (arrayOfCommandTokens.size() == 3) {

                    //name and type
                    int foundRecordFlag = 0;
                    for (int arrayLoop = 0; arrayLoop < recordsArray.size(); arrayLoop++) {
                        if (recordsArray.get(arrayLoop).getName().equalsIgnoreCase(arrayOfCommandTokens.get(1)) && recordsArray.get(arrayLoop).getType().equalsIgnoreCase(arrayOfCommandTokens.get(3))) {
                            recordsArray.remove(arrayLoop);
                            String deleteGoodMessage = "300 DeleteOk";
                            outToClient.writeBytes(deleteGoodMessage + "\n");
                            System.out.println("Sending back "+deleteGoodMessage+"\n");
                            foundRecordFlag = 1;
                        }
                    }

                    if (foundRecordFlag == 0) {
                        //send error message
                        String errorMessage = "404 Not Found";
                        System.out.println("Sending back: 404 Not Found\n");
                        outToClient.writeBytes(errorMessage + "\n");
                    }
                } else {//not enough input arg
                    outToClient.writeBytes("101 Too much/little argument" + "\n");
                    System.out.println("Sending back: 101 Too much/little argument\n");
                }

            } else if (arrayOfCommandTokens.get(0).equalsIgnoreCase("browse")) {
                //make sure there isn't too much input arg
                if (arrayOfCommandTokens.size() == 1) {

                    //return the name and type fields of all records
                    //delimiter is &
                    String s = "";

                    //check if empty
                    if (recordsArray.isEmpty()) {
                        String errorEmptyMessage = "100 Database Is Empty";
                        outToClient.writeBytes(errorEmptyMessage + "\n");
                        System.out.println("Sending back: "+errorEmptyMessage+"\n");
                    } else {
                        //otherwise, loop through the string and then send it over to the client
                        for (int arrayLoop = 0; arrayLoop < recordsArray.size(); arrayLoop++) {
                            s = s + recordsArray.get(arrayLoop).getName() + " " + recordsArray.get(arrayLoop).getType() + "&";
                        }
                        //once you have the entire string
                        outToClient.writeBytes(s + "\n");
                        System.out.println("Sending back: "+s+"\n");
                    }

                } else {
                    outToClient.writeBytes("101 Too much/little argument" + "\n");
                    System.out.println("Sending back: 101 Too much/little argument\n");
                    
                }
            } else {

            }

            // capitalize the sentence
            capitalizedSentence = clientSentence.toUpperCase() + '\n';

            System.out.println("Hello world!");
            System.out.println("input is: " + clientSentence);

            // send the capitalized sentence back to the  client
            //outToClient.writeBytes(capitalizedSentence);
            // close the connection socket
            connectionSocket.close();
        } catch (Exception e) {

        }
    }

    static ArrayList<String> tokenizeCommand(String str) {

        //tokenize
        String[] parts = str.split(" ");

        // turn list into an arraylist
        ArrayList<String> arrayOfParsedInputs = new ArrayList<String>(Arrays.asList(parts));

        return arrayOfParsedInputs;

    }
}
