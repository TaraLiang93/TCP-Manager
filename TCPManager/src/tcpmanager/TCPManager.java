/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package tcpmanager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author mk
 */
public class TCPManager {

    private static Scanner x;
    private static ArrayList<String> typeArray = new ArrayList<>();
    private static ArrayList<Integer> portArray = new ArrayList<>();
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        //open the file
        try
        {
            x = new Scanner(new File("manager.in"));
        }
        catch(Exception e)
        {
            System.out.println("The file was not found and could not be opened.");
        }
        
        //read the file
        while(x.hasNextLine())
        {
            //read and store into the array
            String type_in_file = x.nextLine();
            typeArray.add(type_in_file);
        }
        
        //close the file
        x.close();
        
        //loop through the array and use it to build processes
        for(int i = 0; i<typeArray.size(); i++)
        {
            try
            {
               ProcessBuilder builder = new ProcessBuilder("java", "TCPServerManager", typeArray.get(i));
               Process process_to_use = builder.start();
               BufferedReader reader = new BufferedReader(new InputStreamReader(process_to_use.getInputStream()));
               String portString = reader.readLine();
               portArray.add(Integer.parseInt(portString));
            }
            catch(Exception f)
            {
                System.out.println("Could not get the ports.");
            }
        
            System.out.println("Type " + typeArray.get(i) + "Port " + portArray.get(i));
        
        }
    
        
            String clientSentence = ""; 
                String capitalizedSentence = "";
                
            // get the port number assigned from the command line
                //int lisPort = Integer.parseInt(argv[0]);
                int lisPort = 6319;
                // create a server socket (TCP)
                ServerSocket welcomeSocket = new ServerSocket(lisPort); 
                System.out.println(welcomeSocket.getLocalPort()); 
                // loop infinitely (process clients sequentially)
                while(true) {
                        // Wait and accept client connection
                        Socket connectionSocket = welcomeSocket.accept(); 
                        
                        TCPThreadManager newThread = new TCPThreadManager(connectionSocket, clientSentence, capitalizedSentence, typeArray, portArray);
                        newThread.start();
                } 
    }
    
}

class TCPThreadManager extends Thread{
    
    Socket connectionSocket;
    String clientSentence;
    String capitalizedSentence;
    ArrayList<String> typeArray;
    ArrayList<Integer> portArray;
    
    public TCPThreadManager(Socket conSocket, String clientSen, String capitalizedSen, ArrayList<String> type, ArrayList<Integer> port) {
        connectionSocket = conSocket;
        clientSentence = clientSen;
        capitalizedSentence = capitalizedSen;
        typeArray = type;
        portArray = port;
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
                        
                       //parse the statement "Type ..."
                        String[] parts = clientSentence.split(" ");
    
                        // turn list into an arraylist - second element contains the type to look for
                        ArrayList<String> arrayOfSentenceInputs = new ArrayList<String>(Arrays.asList(parts));
                        
                        int typeFoundIndex = -1;  //some number other than -1 if the type was found
                        
                        for(int j = 0; j < typeArray.size(); j++)
                        {
                            if(arrayOfSentenceInputs.get(1).equalsIgnoreCase(typeArray.get(j)))
                            {
                                //you found the type, send back the port that has to do with it
                                typeFoundIndex = j;
                            }
                            else
                            {
                                //keep looping
                            }
                        }
                        
                        //if it was found, send. otherwise send error
                        if(typeFoundIndex != -1)
                        {
                            // capitalize the sentence
                            capitalizedSentence = "Port "+portArray.get(typeFoundIndex); 
                        
                            // send the capitalized sentence back to the  client
                            outToClient.writeBytes(capitalizedSentence);

                            // close the connection socket
                            connectionSocket.close();
                        }
                        else
                        {
                            // capitalize the sentence
                            capitalizedSentence = "500 PortNotFound"; 
                        
                            // send the capitalized sentence back to the  client
                            outToClient.writeBytes(capitalizedSentence);

                            // close the connection socket
                            connectionSocket.close();
                        }
        }
        catch(Exception e)
        {
            System.out.println("error" + e.toString());
        }
    }
}
