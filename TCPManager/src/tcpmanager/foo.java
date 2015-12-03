/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * THIS IS A TEST FILE, NOT FINAL AND DON'T INCLUDE
 * @author mk
 */
public class foo {

    private static Scanner x;
    private static ArrayList<String> typeArray;
    private static ArrayList<Integer> portArray;
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
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
            //read and store the name of type into the array
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
             //   ProcessBuilder builder = new ProcessBuilder("java", "TCPServerManager", typeArray.get(i));
              //  Process process_to_use = builder.start();
              //  BufferedReader reader = new BufferedReader(new InputStreamReader(process_to_use.getInputStream()));
            //    String portString = reader.readLine();
               // portArray.add(Integer.parseInt(portString));
            }
            catch(Exception f)
            {
                System.out.println("Could not get the ports.");
            }
            //print out all the type there are and its port
            System.out.println("Type " + typeArray.get(i) + "Port " + portArray.get(i));
        
        }
    }
}
