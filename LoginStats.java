package project3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

/**
 * The LoginStats class is responsible for reading the data file,
 * obtaining user input, performing some data validation and handling all errors.
 *
 * @author Leyan Yu
 */
public class LoginStats {
    /**
     * The main method of the LoginStats class.
     * It reads the data file, creates Record objects and adds them to a RecordList.
     * It handles IOExceptions that may occur during file reading.
     *
     * @param args the command line arguments. args[0] is expected to be the name of the data file.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage Error: the program expects a file name as an argument.");
            System.exit(1); // Terminate with an error code
        }

        String fileName = args[0];
        BufferedReader reader;
        RecordList recordList = new RecordList();

        // Handle file open and read errors
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            // Read and process the file data
            while (line != null) {
                String[] words = line.split(" ");
                int terminal = Integer.parseInt(words[0]);
                long time = Long.parseLong(words[1]);
                String username = words[2];
                boolean isLogin = terminal > 0;
                Date date = new Date(time);
                Record record = new Record(Math.abs(terminal), isLogin, username, date);
                recordList.add(record);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error: the file " + fileName + " cannot be opened.");
            System.exit(1); // Terminate with an error code
        }

        System.out.println("Welcome to Login Stats!\n");
        System.out.println("Available commands:");
        System.out.println("  first USERNAME   - retrieves first login session for the USER");
        System.out.println("  last USERNAME    - retrieves last login session for the USER");
        System.out.println("  all USERNAME       - retrieves all login sessions for the USER");
        System.out.println("  total USERNAME - retrieves total login time for the USER");
        System.out.println("  quit             - terminates this program");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            String[] inputs = line.split(" ");
            String command = inputs[0];
            if("quit".equals(command)) {
                break;
            }
            String username = inputs[1];
            if ("first".equals(command)) {
                Session session = recordList.getFirstSession(username);
                checkSession(session,username);
                System.out.println(session);
            }
            else if ("last".equals(command)) {
                Session session = recordList.getLastSession(username);
                checkSession(session,username);
                System.out.println(session);
            } else if ("all".equals(command)) {
                SortedLinkedList<Session> sessions = recordList.getAllSessions(username);
                for (Session session : sessions) {
                    System.out.println(session);
                }
            } else if ("total".equals(command)) {
                long totalTime = recordList.getTotalTime(username);
                long days = totalTime / (24 * 60 * 60 * 1000);
                long duration = totalTime % (24 * 60 * 60 * 1000);
                long hours = duration / (60 * 60 * 1000);
                duration = duration % (60 * 60 * 1000);
                long minutes = duration / (60 * 1000);
                duration = duration % (60 * 1000);
                long seconds = duration / 1000;
                System.out.printf("%s, total duration %dd %dh %dm %ds\n", username, days, hours, minutes, seconds);
            } else {
                System.out.println("This is not a valid command. Try again.");
            }
        }
    }

    /**
     * Checks if the session is null and prints a message if it is.
     *
     * @param session the session to check
     * @param username the username for the session
     */
    private static void checkSession(Session session,String username) {

        if (session == null) {
            System.out.println("No user matching "+username+" found.");
        }
    }

}
