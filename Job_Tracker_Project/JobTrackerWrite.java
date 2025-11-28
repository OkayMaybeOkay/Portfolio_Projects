import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.InputStreamReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JobTrackerWrite {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        String filePath = "JobTracker/JobTracker.csv";

        menuJT(filePath, reader);

        reader.close();

        /*
        List<String[]> readContents = readJT(filePath);
        for (String[] contents: readContents) {
            System.out.println(Arrays.toString(contents));
        }

        writeJT(readContents, filePath);

        List<String[]> newContent = editJobJT(readContents, scanning);

        writeJT(newContent, filePath);

        Scanner scanning = new Scanner(System.in);
        String filePath = "JobTracker/JobTracker.csv";

        List<String[]> contents = readJT(filePath);

        contents = editJobJT(contents, scanning);
        
        */

    }


    public static List<String[]> readJT(String csvPath) {
        List<String[]> fileContents = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] content = line.trim().split(",");
                fileContents.add(content);
            }

            return fileContents;

        } catch (IOException e) {
            System.err.println("Error (readJT): " + e.getMessage());
            e.printStackTrace();

            return new ArrayList<String[]>();

        }
    }


    public static void readFormatedJT(List<String[]> fileContents) {
        int jobNumber = 1;

        for (String[] content: fileContents) {

            if (content[0].equals("Who")) {
                continue;
            }

            System.out.println("Job #" + jobNumber);
            System.out.println();
            System.out.println(" Organization who Gave the Job: " + content[0]);
            System.out.println("                  Job Position: " + content[1]);
            System.out.println("           Location of the Job: " + content[2]);
            System.out.println("Tailored Resume / Cover Letter: " + content[3]);
            System.out.println("     Completed Job Application: " + content[4]);
            System.out.println("       Sent a Thank You Letter: " + content[5]);
            System.out.println();
            jobNumber++;
        }
    }


    public static void identifyJT(List<String[]> fileContents) {
        int currentNumber = 1;

        for (String[] content: fileContents) {
            System.out.println("(" + currentNumber + ") " + Arrays.toString(content));
            currentNumber++;
        }
    }


    public static List<String[]> editJobJT(List<String[]> contentsToEdit, BufferedReader reader) throws IOException {
        System.out.println("Here is the contents of the file: ");
        System.out.println();
        identifyJT(contentsToEdit);

        if (contentsToEdit.isEmpty()) {
            System.err.println("Error (getContents): Contents empty");

            return new ArrayList<String[]>();

        }

        System.out.println("\nWhich line do you want to edit? ");
        System.out.print("Note, you can't edit the header: ");
        int responseInt = Integer.parseInt(reader.readLine());
        System.out.println();

        while (responseInt <= 1 || responseInt > contentsToEdit.size()) {
            System.out.print("Error, enter a valid number: ");
            responseInt = Integer.parseInt(reader.readLine());
            System.out.println();
        }

        String[] lineToEdit = contentsToEdit.get(responseInt - 1);
        System.out.print("Do you want to re-do (1) or delete line (2): ");
        int responseInt_2 = Integer.parseInt(reader.readLine());
        System.out.println();

        while (responseInt_2 != 1 && responseInt_2 != 2) {
            System.out.print("Error, enter a valid number: ");
            responseInt = Integer.parseInt(reader.readLine());
            System.out.println();
        }

        String[] editedLine = editLineJT(lineToEdit, responseInt_2, reader);
        if (editedLine.length == 0) {
            contentsToEdit.remove(responseInt - 1);
        } else {
            contentsToEdit.set(responseInt - 1, editedLine);
        }

        System.out.println("Current Contents");
        identifyJT(contentsToEdit);
        System.out.println();

        return contentsToEdit;

    }


    public static String[] editLineJT(String[] lineToEdit, int responseInt, BufferedReader reader) throws IOException {
        
        if (responseInt == 1) {
            String response;

            System.out.print("Who is giving this job: ");
            response = reader.readLine();
            response = reader.readLine();
            System.out.println();
            lineToEdit[0] = response;

            System.out.print("What is the position: ");
            response = reader.readLine();
            System.out.println();
            lineToEdit[1] = response;

            System.out.print("Where is the job: ");
            response = reader.readLine();
            System.out.println();
            lineToEdit[2] = response;

            System.out.print("Have you tailored a resume / cover letter: ");
            response = reader.readLine();
            System.out.println();
            lineToEdit[3] = response;

            System.out.print("Have you completed the application process: ");
            response = reader.readLine();
            System.out.println();
            lineToEdit[4] = response;

            System.out.print("Have you sent a thank you email / letter: ");
            response = reader.readLine();
            System.out.println();
            lineToEdit[5] = response;

            return lineToEdit;

        } else {
            System.out.println("Deleting Line");
            System.out.println();

            return new String[0];

        }
    }


    public static String[] writeJobJT(BufferedReader reader) throws IOException {
        String response;
        String[] newJob = new String[6];

        System.out.println("Starting to create a new job: ");
        System.out.println();

        System.out.print("Who is the job from: ");
        response = reader.readLine();
        System.out.println();
        newJob[0] = response;

        System.out.print("What is the position: ");
        response = reader.readLine();
        System.out.println();
        newJob[1] = response;

        System.out.print("Where is the job: ");
        response = reader.readLine();
        System.out.println();
        newJob[2] = response;

        System.out.print("Have you tailored a resume / cover letter: ");
        response = reader.readLine();
        System.out.println();
        newJob[3] = response;

        System.out.print("Have you completed the application process: ");
        response = reader.readLine();
        System.out.println();
        newJob[4] = response;

        System.out.print("Have you sent a thank you email / letter: ");
        response = reader.readLine();
        System.out.println();
        newJob[5] = response;

        return newJob;
            
    }

    public static void writeJT(List<String[]> fileContent, String csvPath) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath))) {

            for (String[] writeContents: fileContent) {
                String writeLine = String.join(",", writeContents) + "\n";
                writer.write(writeLine);
            }

        } catch (IOException e) {
            System.err.println("Error (writeJobTracker): " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void menuJT(String filePath, BufferedReader reader) throws IOException {
        
        String response;

        do {

            System.out.println("Welcome to the Job Tracker Program!");
            System.out.println("Here is our menu options:");
            System.out.println();

            System.out.println("(1) View File Contents");
            System.out.println("(2) Edit/Delete Previous Jobs");
            System.out.println("(3) Create a New Job");
            System.out.println();

            System.out.print("What is your choice: ");
            int responseInt = Integer.parseInt(reader.readLine());
            System.out.println();

            while (responseInt != 1 && responseInt != 2 && responseInt != 3) {
                System.out.print("Error, enter a valid number: ");
                responseInt = Integer.parseInt(reader.readLine());
                System.out.println();
            }

            if (responseInt == 1) {
                List<String[]> readContents = readJT(filePath);
                readFormatedJT(readContents);
            } else if (responseInt == 2) {
                List<String[]> readContents = readJT(filePath);
                List<String[]> newContents = editJobJT(readContents, reader);
                writeJT(newContents, filePath);
            } else {
                List<String[]> readContents = readJT(filePath);
                String[] newJob = writeJobJT(reader);
                readContents.add(newJob);
                writeJT(readContents, filePath);
            }

            System.out.print("Would you like to continue the program? (y/n): ");
            response = reader.readLine();
            System.out.println();

            while (response.equals("Y") && response.equals("y") &&
                   response.equals("N") && response.equals("n")) {
            System.out.print("Error, use y,Y,n,N: ");
            response = reader.readLine();
            System.out.println();
            }
        } while ((response.equals("y") || response.equals("Y")));
    }
}
