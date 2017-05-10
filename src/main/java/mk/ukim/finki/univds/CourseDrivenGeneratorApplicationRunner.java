package mk.ukim.finki.univds;

import mk.ukim.finki.univds.generator.CourseDrivenGeneratorService;
import mk.ukim.finki.univds.testrealm.CourseDrivenGeneratorEvaluation;
import mk.ukim.finki.univds.testrealm.QueryHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Run the course driven generator service.
 */
@Component
public class CourseDrivenGeneratorApplicationRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CourseDrivenGeneratorApplicationRunner.class);

    @Autowired
    private CourseDrivenGeneratorService generatorService;

    @Autowired
    private CourseDrivenGeneratorEvaluation evaluator;

    @Value("#{'${course.driven.evaluation.queries.path}'.split(',')}")
    private List<String> queryPaths;

    @Override
    public void run(String... args) throws Exception {

        int datasetCount = Integer.parseInt(args[0]);
        int courseCount = Integer.parseInt(args[1]);
        boolean generateDatasources = Boolean.parseBoolean(args[2]);
        if (generateDatasources) {
            generatorService.generate(datasetCount, courseCount);
            logger.info("data generation done.");
        }

        List<QueryHolder> queries = loadQueries();
        logger.info("evaluating data with query located in {}", queryPaths);
        evaluator.evaluate(datasetCount, courseCount, queries);
        logger.info("im done.");
    }

    private List<QueryHolder> loadQueries() {
        return queryPaths.stream().map(pathToQuery -> {
                try {
                    QueryHolder queryHolder = new QueryHolder();
                    queryHolder.setName(pathToQuery);
                    queryHolder.setQuery(loadQuery(pathToQuery));
                    return queryHolder;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
    }

    private String loadQuery(String pathToQuery) throws IOException {
        return Files.readAllLines(Paths.get(pathToQuery))
                .stream()
                .collect(Collectors.joining("\n"));
    }
}
