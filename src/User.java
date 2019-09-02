/* User class that has functions that allow user to login,signup,transfer,etc */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class User
{
    private String username;
    private String password;
    private int userID;
    // private float balance; not used yet, transactions handled in main
    private Scanner readFile;
    private PrintWriter toFile;
    private File userFile;

    User (String _username, String _password)
    {
        username = _username;
        password = _password;
        userFile = new File ("users/" +username+ ".txt");
    }

    //coded by Dev Khanna
    public boolean login ()
    {
        boolean match = false;

        if (userFile.exists())
        {
            try
            {
                readFile = new Scanner(new File(userFile.getPath()));
                if (readFile.next().equals(password))
                {
                    System.out.println("\nWelcome back, " + username + ".");
                    match = true;
                }
                else {
                    System.out.println("The username or password is incorrect.");
                }
            }
            catch (FileNotFoundException e)
            {
                System.out.println("\nCould not find the user's file.");
            }
        }
        else
        {
            System.out.println("The username or password is incorrect.");
        }

        return match;
    }

    //Signup and unique id coded by Dev Khanna
    public void signUp ()
    {
        if (userFile.exists())
        {
            System.out.println("\nThis username is already in use. Please use a " +
                    "different one.");
        }
        else
        {
            try {
                readFile = new Scanner(new File("data/userCount.txt"));
                int id = Integer.parseInt(readFile.next())+1;
                toFile = new PrintWriter("data/userCount.txt");
                toFile.print(id);
                toFile.close();
                toFile = new PrintWriter(userFile.getPath());
                toFile.print(password+"\nID: "+id+"\n\n=0");
                toFile.close();
                System.out.println("\nAccount created.");
            } catch (FileNotFoundException e) {
                System.out.println("\nCould not find the file or IO exception.");
            }
        }
    }
    // transfer by Dev Khanna
    public void transfer (String _recipient , int recipientID, float _amount)
    {
        try
        {
            File recipient = new File ("users/"+_recipient+".txt");
            if (recipient.exists())
            {
                readFile = new Scanner (new File (recipient.getPath()));
                readFile.nextLine();

                if (readFile.nextLine().equals("ID: "+recipientID))
                {
                    if (_amount>=0)
                    {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss | dd/MM/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        // copying the contents of the file to an array list
                        ArrayList<String> fileContents = new ArrayList<>();
                        readFile = new Scanner (new File (recipient.getPath())); // resets the scanner's pointer to the beginning of the file.

                        while (readFile.hasNext()) {
                            fileContents.add(readFile.nextLine());
                        }
                        // getting the current total
                        String sTotal = fileContents.get(fileContents.size()-1);
                        char [] caTotal = new char[sTotal.length()-1];
                        for (int i = 0; i < caTotal.length; i++) {
                            caTotal[i] = sTotal.charAt(i+1);
                        }

                        // writing to the recipients file
                        float newTotal = Float.parseFloat(new String(caTotal))+_amount;
                        fileContents.add(dtf.format(now)+"  +"+_amount+" has been transferred from "+username+"'s account");
                        fileContents.remove(fileContents.size()-2);
                        fileContents.add("="+newTotal);

                        toFile = new PrintWriter(recipient.getPath());

                        for (String line: fileContents) {
                            toFile.println(line);
                        }
                        toFile.close();
                        fileContents.clear();

                        // reflecting changes in the user's file

                        readFile = new Scanner (new File (userFile.getPath()));

                        while (readFile.hasNext()) {
                            fileContents.add(readFile.nextLine());
                        }
                        sTotal = fileContents.get(fileContents.size()-1);
                        caTotal = new char[sTotal.length()-1];
                        for (int i = 0; i < caTotal.length; i++) {
                            caTotal[i] = sTotal.charAt(i+1);
                        }

                        newTotal = Float.parseFloat(new String(caTotal))-_amount;
                        fileContents.add(dtf.format(now)+"  -"+_amount+" has been transferred to "+_recipient+"'s account");
                        fileContents.remove(fileContents.size()-2);
                        fileContents.add("="+newTotal);

                        toFile = new PrintWriter(userFile.getPath());

                        for (String line: fileContents) {
                            toFile.println(line);
                        }
                        toFile.close();

                        System.out.println("\nSuccess! "+_amount+" has been transferred to "+_recipient+"'s account.");
                    }
                    else
                    {
                        System.out.println("Cannot transfer an amount less than zero.");
                    }
                }
                else
                {
                    System.out.println("The username and user ID do not match.");
                }
            }
            else
            {
                System.out.println("The username and user ID do not match.");
            }
        }
            catch (Exception e)
        {
            System.out.println("Things went wrong :(");
        }
    }

    /* section programmed by Aryan Kothari. Function that allows users to register for a premium account whenever they want */
    public void registerForPremium() {

        boolean loop = true;
        try {
            List<String> lines = Files.readAllLines(Paths.get("users" + File.separator + username + ".txt"));
            if(lines.get(2).equals("ACCOUNT: PREMIUM")) {
                System.out.println("YOU ALREADY HAVE A PREMIUM ACCOUNT");
                loop = false;
            }

                File tempFile = new File("users" + File.separator + "tempFile.txt");
                BufferedWriter writer = new BufferedWriter(new FileWriter("users" + File.separator + "tempFile.txt"));
                {
                    for (int i = 0; i < lines.size(); i++) {
                        if(i == 2) {
                            if(loop) {
                                writer.write("ACCOUNT: PREMIUM" + "\n"); // error here , can't have premium line as last line
                                System.out.println("You have signed up for a premium account!");
                            }
                        }
                        writer.write(lines.get(i) + "\n");
                    }
                    writer.flush();
                    writer.close();
                }
                new File("users" + File.separator + username + ".txt").delete();
                tempFile.renameTo(new File("users" + File.separator + username + ".txt"));
                tempFile.delete();
            } catch(Exception e){
            }
        }

    /* section programmed by Aaryan S. Function that allows users to check their account history */
        public void accHistory()
        {
            try {
                List<String> lines = Files.readAllLines(Paths.get("users" + File.separator + username + ".txt"));
                BufferedWriter writer = new BufferedWriter(new FileWriter("users" + File.separator + "tempFile.txt"));

                    for (int i = 1; i < lines.size(); i++) {
                        System.out.println(lines.get(i));
                    }

            } catch(Exception e){
            }
        }


        // Getters and Setters
    public File getUserFile() {
        return userFile;
    }

    public String getUsername() {
        return username;
    }
}