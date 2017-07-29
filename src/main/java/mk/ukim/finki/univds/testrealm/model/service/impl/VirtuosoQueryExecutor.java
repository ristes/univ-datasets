package mk.ukim.finki.univds.testrealm.model.service.impl;

import mk.ukim.finki.univds.testrealm.QueryResultsHolder;
import mk.ukim.finki.univds.testrealm.model.service.QueryExecutor;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

/**
 * {@link QueryExecutor} implementation that executes queries over virtuoso store.
 */
@Service
@Profile("virtuoso")
public class VirtuosoQueryExecutor implements QueryExecutor {

    private static final Logger logger = LoggerFactory.getLogger(VirtuosoQueryExecutor.class);

    @Value("${app.paths.datasets.prefix}")
    private String graphIriPrefix;

    @Value("${virtuoso.connection.string}")
    private String virtuosoUrl;

    @Value("${virtuoso.username}")
    private String virtuosoUsername;

    @Value("${virtuoso.password}")
    private String virtuosoPassword;

    /**
     * Equivalent to {@link TdbQueryExecutor#dataset}.
     */
    private VirtGraph virtuosoDataset;

    @Override
    public QueryResultsHolder executeAndStoreSelect(String queryString) {
        Query query = QueryFactory.create(queryString);

        VirtuosoQueryExecution queryExecution = VirtuosoQueryExecutionFactory.create(query, virtuosoDataset);
        Model results = queryExecution.execConstruct();
        long size = results.size();
        QueryResultsHolder queryResultsHolder = new QueryResultsHolder();
        queryResultsHolder.setModel(results);
        queryResultsHolder.setResultSize(size);
        queryExecution.close();
        return queryResultsHolder;
    }

    @Override
    public void openDataset(String dataSet) {
        if (virtuosoDataset != null) {
            closeDataset();
        }
        String graphName = graphIriPrefix + dataSet;
        logger.info("loading dataset: {}", graphName);
        virtuosoDataset = new VirtGraph(graphName, virtuosoUrl, virtuosoUsername, virtuosoPassword);
    }

    @Override
    public void closeDataset() {
        if (!virtuosoDataset.isClosed()) {
            virtuosoDataset.close();
        }
        virtuosoDataset = null;
    }

    @Override
    public long executeSelect(String dataSet, String query) {
        throw new NotImplementedException("Not implemented in VirtuosoQueryExecutor.java. So far not needed.");
    }

    @Override
    public void executeInsert(String dataSet, String query) {
        throw new NotImplementedException("Not implemented in VirtuosoQueryExecutor.java. So far not needed.");
    }

    @Override
    public Dataset getDataset() {
        throw new NotImplementedException("Not implemented in VirtuosoQueryExecutor.java. So far not needed.");
    }

    @Override
    public void deleteNamedGraph(String iri) {
        throw new NotImplementedException("Not implemented in VirtuosoQueryExecutor.java. So far not needed.");
    }
}
