package giganticintegers;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;

public class PrintUtils {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";


    static void println(String s) {
        System.out.printf("[" + ANSI_CYAN + "%s" + ANSI_RESET + "] " + ANSI_PURPLE + "%s" + ANSI_RESET + " %s\n",
                Thread.currentThread().getName(),
                Instant.now().toEpochMilli(),
                s);
    }

    public static void printlnStatus(GiganticIntegersProcessor gip) {
        printStatus("", gip, "\n");
    }

    public static void writeStatus(GiganticIntegersProcessor gip) {
        printStatus("\r", gip, "");
    }

    public static void printStatus(String prefix, GiganticIntegersProcessor gip, String suffix) {
        System.out.printf(prefix + "[" + ANSI_CYAN + "%s" + ANSI_RESET + "] "
                        + ANSI_PURPLE + "%s" + ANSI_RESET
                        + " count="
                        + ANSI_GREEN + "%s" + ANSI_RESET
                        + " min="
                        + ANSI_GREEN + "%s" + ANSI_RESET
                        + " max="
                        + ANSI_GREEN + "%s" + ANSI_RESET
                        + suffix,
                Thread.currentThread().getName(),
                Instant.now().toEpochMilli(),
                gip.getCount(),
                StringUtils.abbreviate(gip.getMin().toString(), 30),
                StringUtils.abbreviate(gip.getMax().toString(), 30));
    }

}
