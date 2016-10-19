package ca.waterloo.dsg.graphflow.graphmodel;

import ca.waterloo.dsg.graphflow.util.IntArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Tests graphmodel.Graph class.
 */
public class GraphTest {
  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void getInstance() throws Exception {
    String testFile = "src/test/Fixtures/graph.json";
    File file = new File(testFile);

    Graph g = Graph.getInstance(file);
    Assert.assertEquals(6, g.getVertexCount());
    System.out.println(g);
  }

  @Test
  public void getVertices() throws Exception {
    String testFile = "src/test/Fixtures/graph.json";
    File file = new File(testFile);

    Graph g = Graph.getInstance(file);
    IntArrayList vertices = g.getVertices(true);
    Assert.assertEquals(6, vertices.size());
  }
}
