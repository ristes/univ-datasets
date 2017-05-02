package mk.ukim.finki.univds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Riste Stojanov
 */

@EnableAutoConfiguration
@SpringBootApplication(exclude = GeneratorConsoleApplicationRunner.class)
public class TestConfig {


  public static void main(String[] args) {
    SpringApplication.run(TestConfig.class, args);
  }
}
