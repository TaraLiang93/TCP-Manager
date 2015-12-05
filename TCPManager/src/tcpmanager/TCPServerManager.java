import java.io.*; // Provides for system input and output through data
// streams, serialization and the file system
import java.net.*; // Provides the classes for implementing networking
import java.util.ArrayList;
import java.util.Arrays;
                   // applications

// TCP Server Class
class TCPServerManager {

    static ArrayList<Record> recordsArray = new ArrayList<Record>();
    private static String type;

    public static void main(String argv[]) throws Exception {
        String clientSentence = "";
        String capitalizedSentence = "";

        // get the port number depends on what is free
        int lisPort = 0;
        // create a server socket (TCP)
        ServerSocket welcomeSocket = new ServerSocket(lisPort);
        System.out.println(welcomeSocket.getLocalPort());
        type = argv[0];
        // loop infinitely (process clients sequentially)
        while (true) {
            // Wait and accept client connection
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("I am connected.");

            //create and start new thread
            TCPThread newThread = new TCPThread(connectionSocket, clientSentence, recordsArray, capitalizedSentence, type);
            newThread.start();
        }
    }

}

class TCPThread extends Thread {

    Socket connectionSocket;
    String clientSentence;
    ArrayList<Record> recordsArray;
    String capitalizedSentence;
    String type;

    public TCPThread(Socket conSocket, String clientSen, ArrayList<Record> records, String capitalizedSen, String type) {
        connectionSocket = conSocket;
        clientSentence = clientSen;
        recordsArray = records;
        capitalizedSentence = capitalizedSen;
        this.type = type;
    }

    public void run() {
        try {

            System.out.println("The TCPThread is started.");
            //create an input stream from the socket input stream
            BufferedReader inFromClient = new BufferedReader(
                    new InputStreamReader(connectionSocket.getInputStream()));

            // create an output stream from the socket output stream
            DataOutputStream outToClient
                    = new DataOutputStream(connectionSocket.getOutputStream());

            // read a line form the input stream
            System.out.println("Waiting for client input...");
            clientSentence = inFromClient.readLine();
            System.out.println(clientSentence); // print the sentence i just got

            //this is the thing you parse
            ArrayList<String> arrayOfCommandTokens = tokenizeCommand(clientSentence);
            System.out.println(arrayOfCommandTokens.toString());

            if (arrayOfCommandTokens.get(0).equalsIgnoreCase("put")) {

                //make sure there is something for value and name
                if (arrayOfCommandTokens.size() > 2) {
                    Record newRecord;
                    //there is multiple word for value need to concate it and create the newRecord.
                    int temp = 3;
                    String value = arrayOfCommandTokens.get(2);
                    while (temp != arrayOfCommandTokens.size()) {
                        value = value.concat(" ").concat(arrayOfCommandTokens.get(temp));
                        temp++;
                    }
                    newRecord = new Record(arrayOfCommandTokens.get(1), value, type);

                    int foundRecordFlag = 0;

                    //check to see if it exist, if does, then replace that one
                    for (int arrayLoop = 0; arrayLoop < recordsArray.size(); arrayLoop++) {
                        if (recordsArray.get(arrayLoop).getName().equalsIgnoreCase(arrayOfCommandTokens.get(1))) {
                            recordsArray.remove(arrayLoop);
                            recordsArray.add(arrayLoop, newRecord);
                            String ackGoodPutMessage = "200 PutOk ";
                            System.out.println(ackGoodPutMessage);
                            outToClient.writeBytes(ackGoodPutMessage + "\n");
                            foundRecordFlag = 1;
                        }
                    }

                    //no existing record, add new ones
                    if (foundRecordFlag == 0) {
                        recordsArray.add(newRecord);
                        String ackGoodPutMessage = "200 PutOk ";
                        System.out.println(ackGoodPutMessage);
                        outToClient.writeBytes(ackGoodPutMessage + "\n");
                    }
                } else {//missing arg
                    String ackBadPutMessage = "202 PutNotOk, missing name and/or value of the record. ";
                    System.out.println(ackBadPutMessage);
                    outToClient.writeBytes(ackBadPutMessage + "\n");

                }

            } else if (arrayOfCommandTokens.get(0).equalsIgnoreCase("get")) {

                if (arrayOfCommandTokens.size() == 2) {
                //name and type
                    //look through the arraylist for the name and the type, if found return to client
                    //else return a error message
                    int foundRecordFlag = 0;
                    for (int arrayLoop = 0; arrayLoop < recordsArray.size(); arrayLoop++) {
                        if (recordsArray.get(arrayLoop).getName().equalsIgnoreCase(arrayOfCommandTokens.get(1))) {
                            System.out.println("Sending back: " + recordsArray.get(arrayLoop).getValue());
                            outToClient.writeBytes("400 GetOK " + recordsArray.get(arrayLoop).getValue() + "\n");
                            foundRecordFlag = 1;
                        }
                    }

                    if (foundRecordFlag == 0) {
                        //send error message
                        System.out.println("404 Not Found");
                        String errorMessage = "404 Not Found";
                        outToClient.writeBytes(errorMessage + "\n");
                    }
                } else {
                    outToClient.writeBytes("101 Too much/little argument" + "\n");
                }
            } else if (arrayOfCommandTokens.get(0).equalsIgnoreCase("del")) {
                if (arrayOfCommandTokens.size() == 2) {
                    //name and type
                    int foundRecordFlag = 0;
                    for (int arrayLoop = 0; arrayLoop < recordsArray.size(); arrayLoop++) {
                        if (recordsArray.get(arrayLoop).getName().equalsIgnoreCase(arrayOfCommandTokens.get(1))) {
                            recordsArray.remove(arrayLoop);
                            String deleteGoodMessage = "300 DeleteOk";
                            outToClient.writeBytes(deleteGoodMessage + "\n");
                            foundRecordFlag = 1;
                        }
                    }

                    if (foundRecordFlag == 0) {
                        //send error message
                        String errorMessage = "404 Not Found";
                        outToClient.writeBytes(errorMessage + "\n");
                    } else {
                        //do nothing
                    }
                } else {
                    outToClient.writeBytes("101 Too much/little argument" + "\n");
                }
            } else if (arrayOfCommandTokens.get(0).equalsIgnoreCase("browse")) {
                //return the name and type fields of all records
                //delimiter is &
                if (arrayOfCommandTokens.size() == 1) {
                    String s = "";

                    //check if empty
                    if (recordsArray.isEmpty()) {
                        String errorEmptyMessage = "100 Database Is Empty";
                        outToClient.writeBytes(errorEmptyMessage + "\n");
                    } else {
                        //otherwise, loop through the string and then send it over to the client
                        for (int arrayLoop = 0; arrayLoop < recordsArray.size(); arrayLoop++) {
                            s = s + recordsArray.get(arrayLoop).getName() + " " + recordsArray.get(arrayLoop).getType() + "&";
                        }
                        //once you have the entire string
                        outToClient.writeBytes(s + "\n");
                    }
                } else {
                    outToClient.writeBytes("101 Too much/little argument" + "\n");
                }
            } else {

            }


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
