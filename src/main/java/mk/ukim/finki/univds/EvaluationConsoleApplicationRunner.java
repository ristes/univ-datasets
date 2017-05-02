package mk.ukim.finki.univds;

import mk.ukim.finki.univds.testrealm.MainTestCaseEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Test evaluations runner.
 */
@Component
public class EvaluationConsoleApplicationRunner implements CommandLineRunner {

    @Autowired
    private MainTestCaseEvaluationService evaluationService;

    @Override
    public void run(String... args) throws Exception {
        evaluationService.processScenario();
    }
}
