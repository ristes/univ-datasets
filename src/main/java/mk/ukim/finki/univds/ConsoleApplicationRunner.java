package mk.ukim.finki.univds;

import mk.ukim.finki.univds.generator.GeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Runs spring console application.
 */
@Component
public class ConsoleApplicationRunner implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(ConsoleApplicationRunner.class);

    @Autowired
    private GeneratorService generatorService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("application started with {} iteration cycles" + args[0]);
        int generatorCycles = Integer.parseInt(args[0]);
        generatorService.generate(generatorCycles);
        logger.info("I am done!");
    }
}
