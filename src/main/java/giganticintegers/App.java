package giganticintegers;

import java.io.*;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class App {

    /**
     * Command line utility to process gigantic integers from given files
     * @param args The only argument is an integer to specify the threshold value
     */
    public static void main(String[] args) {

        PrintUtils.println("Hello!");

        /*
        Get the threshold value from args
         */
        BigInteger threshold = BigInteger.ZERO;
        if (args.length > 0 && !args[0].isEmpty()) {
            try {
                threshold = new BigInteger(args[0]);
            }
            catch (NumberFormatException ex) {
                PrintUtils.println(args[0] + " is not a valid integer. Threshold defaults to 0");
            }
        }
        else {
            PrintUtils.println("Threshold defaults to 0");
        }

        /*
        Create a persistent GiganticIntegersProcessor
         */
        GiganticIntegersProcessor gip = new GiganticIntegersProcessor(threshold);

        /*
        Just keep asking for files ad-nauseum to show the counts are persistent
         */
        for (; ; ) {
            PrintUtils.println("Please provide the path to a file full of tasty integers (defaults to ./input.txt)");
            Scanner scanner = new Scanner(System.in);
            String pathname = scanner.nextLine();

            if (pathname.isEmpty()) {
                pathname = "./input.txt";
            }
            File file = new File(pathname);
            String filePath = file.getPath();
            try {
                Reader reader = new BufferedReader(new FileReader(filePath));

                /*
                Create a timer here to show that the status can be accessed in a threadsafe fashion.
                Have it write the status every 100ms on the same line so the console doesn't splode
                 */
                Timer t = new Timer();
                t.scheduleAtFixedRate(
                        new TimerTask() {

                            public void run() {
                                PrintUtils.writeStatus(gip);
                            }
                        },
                        100, // run first occurrence after 0.1 seconds
                        100 // run every 0.1 seconds
                );
                /*
                Consume the file
                Nom nom nom
                 */
                Instant start = Instant.now();
                gip.process(reader);
                Instant finish = Instant.now();
                t.cancel();
                long timeElapsed = Duration.between(start, finish).toMillis();
                PrintUtils.println("Nom nom nom... Processed that file in " + timeElapsed + "ms");
            } catch (FileNotFoundException e) {
                PrintUtils.println("Oh noes! File " + filePath + " not found");
                continue;
            }
        }
    }
}




