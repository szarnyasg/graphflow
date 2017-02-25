package ca.waterloo.dsg.graphflow.query.operator;

import ca.waterloo.dsg.graphflow.query.executors.GenericJoinExecutor;
import ca.waterloo.dsg.graphflow.query.output.MatchQueryOutput;

import java.util.List;

/**
 * Base class for various database operators, such as projections, as well as output sinks, such
 * as {@link FileOutputSink}. Contains the following methods and fields:
 * <ul>
 * <li> Different append methods that accept different query outputs that this operator will
 * process.
 * <li> A field for storing a possibly null next operator whose append methods this operator
 * should call. Database operators may generate further query outputs as they process the query
 * outputs that are appended to it.
 * </ul>
 */
public abstract class AbstractDBOperator {

    protected AbstractDBOperator nextOperator;

    /**
     * Default constructor.
     *
     * @param nextOperator possibly null next database operator that this operator should append
     * query outputs to.
     */
    public AbstractDBOperator(AbstractDBOperator nextOperator) {
        this.nextOperator = nextOperator;
    }

    /**
     * @param nextOperator the {@link AbstractDBOperator} to which the
     * {@link GenericJoinExecutor}'s output is appended to.
     */
    public void setNextOperator(AbstractDBOperator nextOperator) {
        this.nextOperator = nextOperator;
    }

    public void append(MatchQueryOutput matchQueryOutput) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " does not " +
            "support the append(MatchQueryOutput matchQueryOutputs) method.");
    }

    public void append(String stringQueryOutput) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " does not " +
            "support the append(String stringQueryOutput) method.");
    }

    /**
     * @return a String human readable representation of an operator excluding its next operator.
     */
    protected abstract String getHumanReadableOperator();

    /**
     * @return a String human readable representation of an operator and all of its next operators.
     */
    public String getHumanReadablePlan() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getHumanReadableOperator());
        int level = 1;
        AbstractDBOperator operator = nextOperator;
        while (null != operator) {
            stringBuilder.append(getIndentedString("nextOperator -> " +
                operator.getHumanReadableOperator(), level++));
            operator = operator.nextOperator;
        }
        return stringBuilder.toString();
    }

    /**
     * This method first converts a list of objects to a comma separated String that is: (1)
     * prefixed with a tab and the given prefix string and ends with a new line. Then it appends
     * the string to the given {@link StringBuilder}.
     *
     * @param stringBuilder {@link StringBuilder} object to append to.
     * @param objects The list of objects to convert to comma separated String.
     * @param prefix A String that will be the prefix of the appended String.
     */
    protected static <T> void appendListAsCommaSeparatedString(StringBuilder stringBuilder,
        List<T> objects, String prefix) {
        stringBuilder.append("\t").append(prefix).append(": {");
        for (Object object : objects) {
            stringBuilder.append(" ").append(object);
        }
        stringBuilder.append(" }\n");
    }

    /**
     * @param numTabsToIndent the number of tab indentations to have.
     * @return a String that indents each line of the given unindented String by
     * {@code numTabsToIndent} tabs.
     */
    protected String getIndentedString(String unindentedString, int numTabsToIndent) {
        String indentation = "";
        for (int i = 0; i < numTabsToIndent; ++i) {
            indentation += "\t";
        }
        StringBuilder stringBuilder = new StringBuilder();
        String[] lines = unindentedString.split("\n");
        for (String line : lines) {
            stringBuilder.append(indentation).append(line).append("\n");
        }
        return stringBuilder.toString();
    }
}