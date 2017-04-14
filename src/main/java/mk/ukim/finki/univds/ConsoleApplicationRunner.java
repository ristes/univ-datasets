package mk.ukim.finki.univds;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Runs spring console application.
 */
@Component
public class ConsoleApplicationRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("application is now started!");
    }
}
