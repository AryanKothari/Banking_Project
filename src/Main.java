//Importing Libraries
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Main {
    private static Scanner scan = new Scanner (System.in);
    private static ArrayList <User> users = new ArrayList<>();
    private static Feedback feedback = new Feedback();
    private static Scanner readFile;
    private static PrintWriter toFile;

    public static void main(String[] args) {
        boolean isInteracting = true;

        System.out.println("\nWelcome to AAD Bank's computer banking application!\nTo select a menu item" +
                " enter the number stated to the left of the item.");

           /*
           All the scan.nextLines and scan.nexts are to move the scanner to the next line and to 'reset' the
           scanner respectively, in order to minimize the times that the program will give the user the same
           message if the input contains spaces. Nothing crashes without them it just makes the ux more
           pleasant.
           */

        while (isInteracting)
        {
            try
            {
                System.out.println("\n1. Login\n2. Sign up\n3. Help\n4. Exit");
                int input = scan.nextInt();

                switch (input)
                {
                    case 1: // login
                        System.out.println("\nEnter your username:");
                        String username = scan.next(); scan.nextLine();
                        System.out.println("\nEnter your password:");
                        String password = scan.next(); scan.nextLine();
                        users.add(new User(username, password));
                        if (users.get(users.size()-1).login())
                        {
                            userMenu(users.get(users.size()-1).getUserFile());
                        }
                        break;

                    case 2: // This is the sign up function. Over here, a new user can sign
                        System.out.println("\n** Your username or password may not contain spaces. **");
                        System.out.println("Enter the username you would like to use:");
                        username = scan.next(); scan.nextLine();
                        System.out.println("\nConfirm your username:");

                        if ((scan.next()).equals(username))
                        {
                            scan.nextLine();
                            System.out.println("\nEnter the password you would like to use:");
                            password = scan.next(); scan.nextLine();
                            System.out.println("\nConfirm your password:");
                            if (scan.next().equals(password))
                            {
                                users.add(new User(username, password));
                                users.get(users.size()-1).signUp();
                                scan.nextLine();
                            } else {
                                System.out.println("\nYour password does not match what you have entered " +
                                        "or contains a space.");
                            }
                        } else {
                            System.out.println("\nYour username does not match what you have entered " +
                                    "or contains a space.");
                        }
                        break;

                    case 3: // How to get extra help (check the manual)
                        System.out.println("\nFor helpful information about this program's functionally, please refer to the user manual located in the program's folder.");
                        break;

                    case 4: // exit the code
                        isInteracting = false;
                        break;

                    default:
                        System.out.println("\nThat option does not exist! Please try again.");
                        break;
                }
            }
            catch (java.util.InputMismatchException e) {
                System.out.println("\nPlease enter whole numbers only.");
                scan.next();
            }
        }
    }

    /* This is a function that deals with the functionality after the user logs in. */
    private static void userMenu (File _user)
    {
        boolean isLoggedIn = true;
        while (isLoggedIn)
        {
            try {
                readFile = new Scanner(new File(_user.getPath()));
                ArrayList <String> fileContents = new ArrayList<>();
                while (readFile.hasNext()) {
                    fileContents.add(readFile.nextLine());
                }

                System.out.println("\n1. Deposit\n2. Withdraw\n3. Transfer\n4. Register for Premium\n5. Check Account History\n6. Submit Feedback\n7. Logout");
                int input = scan.nextInt(); scan.nextLine();
                float amount;

                switch (input) {
                    // deposit and withdraw + time and date done by Dev Khanna
                    case 1:
                    case 2:
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss | dd/MM/yyyy");
                        LocalDateTime now = LocalDateTime.now();

                        boolean transaction = false;
                        String sTotal = fileContents.get(fileContents.size()-1);
                        char [] caTotal = new char[sTotal.length()-1];
                        for (int i = 0; i < caTotal.length; i++) {
                            caTotal[i] = sTotal.charAt(i+1);
                        }
                        float newTotal = Float.parseFloat(new String(caTotal));

                        if (input == 1) {
                            System.out.println("\nEnter the amount you would like to deposit:");
                            amount = scan.nextFloat(); scan.nextLine();
                            if (amount >= 0)
                            {
                                newTotal += amount;
                                fileContents.add(dtf.format(now)+"  +"+amount+" has been deposited.");
                                System.out.println("Success! $"+amount+" has been deposited.");
                                transaction = true;
                            }
                            else
                            {
                                System.out.println("\nYou can not deposit a negative amount.");
                            }
                        } else {
                            System.out.println("\nEnter the amount you would like to withdraw:");
                            amount = scan.nextFloat(); scan.nextLine();
                            if (amount >= 0)
                            {
                                newTotal -= amount;
                                fileContents.add(dtf.format(now)+"  -"+amount+" has been withdrawn.");
                                System.out.println("Success! $"+amount+" has been withdrawn.");
                                transaction = true;
                            }
                            else
                            {
                                System.out.println("\nYou can not withdraw a negative amount");
                            }
                        }
                        if (transaction)
                        {
                            fileContents.remove(fileContents.size()-2);
                            fileContents.add("="+newTotal);

                            toFile = new PrintWriter(new File(_user.getPath()));
                            for (String line: fileContents) {
                                toFile.println(line);
                            }
                            toFile.close();
                        }
                        break;
                    case 3: // User to user transfer by Dev Khanna
                        System.out.println("Enter the username of the account you would like to transfer funds to:");
                        String recipient = scan.next(); scan.nextLine();
                        System.out.println("Enter the ID of the user:");
                        int userID = scan.nextInt(); scan.nextLine();
                        System.out.println("Enter the amount you would like to transfer to the account:");
                        amount = scan.nextFloat(); scan.nextLine();
                        users.get(users.size()-1).transfer(recipient,userID,amount);
                        break;
                    case 4: /* premium account function created by Aryan Kothari */
                        System.out.println("\nWould you like to register for a premium account?\n 1. Yes \n If not, press any other number to go back.");
                        int val = scan.nextInt();
                        if (val == 1) {
                            users.get(users.size()-1).registerForPremium();
                        }
                        break;
                    case 5: /* review account history function created by Aaryan S */
                        System.out.println("\nYour account history is as follows:\n");
                        users.get(users.size()-1).accHistory();

                        break;
                    case 6: /* feedback function created by Aryan Kothari */
                        System.out.println("We value your feedback. Please type it in below and click ENTER");
                        feedback.reportFeedback(users.get(users.size()-1).getUsername());
                        System.out.println("Your feedback has been submitted! Thank you.");
                        break;
                    case 7:
                        isLoggedIn = false;
                        break;
                    default:
                        System.out.println("\nThat option does not exist! Please try again.");
                        break;
                }
            } catch (java.io.FileNotFoundException e) {
                System.out.println("\nCould not find your file.");
                scan.next();
            } catch (java.util.InputMismatchException e) {
                System.out.println("\nWhen selecting a menu option please enter whole number only. When entering in an amount please use ints or floats only.");
                scan.next();
            }
        }
    }
}