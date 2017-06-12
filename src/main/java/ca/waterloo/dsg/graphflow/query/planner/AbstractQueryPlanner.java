package ca.waterloo.dsg.graphflow.query.planner;

import ca.waterloo.dsg.graphflow.query.plans.QueryPlan;
import ca.waterloo.dsg.graphflow.query.structuredquery.StructuredQuery;

/**
 * Interface definition for creating {@code AbstractQueryPlanner}s for different operations.
 */
abstract class AbstractQueryPlanner {

    StructuredQuery structuredQuery;

    AbstractQueryPlanner(StructuredQuery structuredQuery) {
        this.structuredQuery = structuredQuery;
    }

    abstract QueryPlan plan();
}
