package ca.waterloo.dsg.graphflow.graph;

import ca.waterloo.dsg.graphflow.util.ShortArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Encapsulates an {@code Iterator} over the diff edges of the {@code Graph}.
 */
public class DiffEdgesIterator implements Iterator<int[]> {

    private List<int[]> diffEdges;
    private ShortArrayList diffEdgeTypes;
    private short edgeType;
    private int next = -1;

    /**
     * Constructor for {@link DiffEdgesIterator} with all possible vertex and edge filters
     * specified.
     *
     * @param diffEdges The set of edges that were added or deleted from the graph.
     * @param diffEdgeTypes The type IDs of the added or deleted edges.
     * @param edgeType The type ID which the selected edge should equal.
     */
    public DiffEdgesIterator(List<int[]> diffEdges, ShortArrayList diffEdgeTypes, short edgeType) {
        this.diffEdges = diffEdges;
        this.diffEdgeTypes = diffEdgeTypes;
        this.edgeType = edgeType;
        setIndexToNextEdge();
    }

    @Override
    public boolean hasNext() {
        return next < diffEdges.size();
    }

    @Override
    public int[] next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        int[] result = diffEdges.get(next);
        setIndexToNextEdge();
        return result;
    }

    private void setIndexToNextEdge() {
        next++;
        while (next < diffEdges.size()) {
            // Find the next edge {@code e=(u, v)} where {@code e}'s type is {@code edgeType}.
            // {@code edgeType} is the argument that was given during the construction of this
            // {@link DiffEdgesIterator}. If there is no such {@code e=(u, v)}, the {@code next}
            // index is set to the size of {@code diffEdges.size()}.

            if (TypeStore.ANY_TYPE == edgeType || diffEdgeTypes.get(next) == edgeType) {
                return;
            }
            next++;
        }
    }
}
