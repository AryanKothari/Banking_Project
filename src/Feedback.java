/* This class has been coded by Aryan Kothari. There is one instance of the feedback object that allows users to submit feedback */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Feedback {
    private Scanner readFile;
    private static Scanner scan = new Scanner (System.in);
    private PrintWriter toFile;
    private File feedbackFile;
    BufferedWriter writer;

    Feedback() {
        feedbackFile = new File ("data"+File.separator + "feedback.txt");
    }

    public void reportFeedback(String username) {
        try {
            readFile = new Scanner(new File("data"+File.separator + "feedback.txt"));
            ArrayList<String> fileContents = new ArrayList<>();
            while (readFile.hasNext()) {
                fileContents.add(readFile.nextLine());
            }
            String input = scan.nextLine();
            fileContents.add(username + " : " + input);
            toFile = new PrintWriter(new File("data"+File.separator + "feedback.txt"));
            for (String line: fileContents) {
                toFile.println(line);
            }
            toFile.close();

        } catch(Exception e){
            System.out.println("error on feedback");
        }
    }
}
