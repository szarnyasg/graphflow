package ca.waterloo.dsg.graphflow.query.operator;

import java.util.List;
import java.util.Map.Entry;

import org.antlr.v4.runtime.misc.Pair;

import ca.waterloo.dsg.graphflow.query.operator.aggregator.AbstractAggregator;
import ca.waterloo.dsg.graphflow.query.operator.aggregator.CountStar;
import ca.waterloo.dsg.graphflow.query.output.MatchQueryOutput;
import ca.waterloo.dsg.graphflow.util.StringToIntKeyMap;

/**
 * Operator for grouping MATCH query outputs by zero more keys and aggregating each group by one
 * or more values.
 */
public class GroupByAndAggregate extends PropertyReadingOperator {

    private static String GROUP_BY_KEY_DELIMETER = "-";
    
    private List<EdgeOrVertexPropertyDescriptor> valuesToGroupBy;
    private List<Pair<EdgeOrVertexPropertyDescriptor, AbstractAggregator>> valueAggregatorPairs;
    private StringToIntKeyMap groupByKeys;

    /**
     * Default constructor.
     *
     * @param nextOperator next operator to append outputs to.
     * @param valuesToGroupBy descriptions of the list of values to group by.
     * @param valueAggregatorPairs descriptions of the values to aggregate and the aggregator to
     * use for these values.
     */
    public GroupByAndAggregate(AbstractDBOperator nextOperator,
        List<EdgeOrVertexPropertyDescriptor> valuesToGroupBy,
        List<Pair<EdgeOrVertexPropertyDescriptor, AbstractAggregator>> valueAggregatorPairs) {
        super(nextOperator, valuesToGroupBy);
        this.valuesToGroupBy = valuesToGroupBy;
        this.valueAggregatorPairs = valueAggregatorPairs;
        this.groupByKeys = new StringToIntKeyMap();
    }

    @Override
    public void append(MatchQueryOutput matchQueryOutput) {
        clearAndFillStringBuilder(matchQueryOutput, GROUP_BY_KEY_DELIMETER);
        String groupByKey = stringBuilder.toString();
        int index = groupByKeys.getKeyAsIntOrInsert(groupByKey);
        for (Pair<EdgeOrVertexPropertyDescriptor, AbstractAggregator> valueAggregatorPair :
            valueAggregatorPairs) {
            if (valueAggregatorPair.b instanceof CountStar) {
                valueAggregatorPair.b.aggregate(index, 1 /* we aggregate count(*) by 1 */);
                System.out.println("Aggreating count(*): groupByKey: " + groupByKey);
                continue;
            }
            System.out.println("valueAggregatorPair.a: " + valueAggregatorPair.a);
            Object propertyOrId = getPropertyOrId(matchQueryOutput, valueAggregatorPair.a);
            System.out.println("Aggreating: groupByKey: " + groupByKey + " value: "
                + propertyOrId);
            valueAggregatorPair.b.aggregate(index, propertyOrId);
        }
    }
    
    @Override
    public void done() {
        for (Entry<String, Integer> groupByKeyAndIndex : groupByKeys.entrySet()) {
            String groupByKey = groupByKeyAndIndex.getKey();
            int index = groupByKeyAndIndex.getValue();
            stringBuilder.delete(0, stringBuilder.length());
            stringBuilder.append(groupByKey);
            for (Pair<EdgeOrVertexPropertyDescriptor, AbstractAggregator> valueAggregatorPair :
                valueAggregatorPairs) {
                stringBuilder.append(" " + valueAggregatorPair.b.getStringValue(index));
            }
            nextOperator.append(stringBuilder.toString());
        }
        nextOperator.done();
    }

    @Override
    protected String getHumanReadableOperator() {
        StringBuilder stringBuilder = new StringBuilder("GroupByAndAggregate:\n");
        appendListAsCommaSeparatedString(stringBuilder, valuesToGroupBy, "valuesToGroupBy");
        appendListAsCommaSeparatedString(stringBuilder, valueAggregatorPairs,
            "valueAggregatorPairs");
        return stringBuilder.toString();
    }    
}