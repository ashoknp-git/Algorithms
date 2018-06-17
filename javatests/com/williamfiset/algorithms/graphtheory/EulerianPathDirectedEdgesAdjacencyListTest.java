package javatests.com.williamfiset.algorithms.graphtheory;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import com.williamfiset.algorithms.graphtheory.EulerianPathDirectedEdgesAdjacencyList;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import org.junit.*;

public class EulerianPathDirectedEdgesAdjacencyListTest {
  
  EulerianPathDirectedEdgesAdjacencyList solver;

  @Before
  public void setUp() {
    solver = null;
  }

  // Initialize graph with 'n' nodes.
  public static List<List<Integer>> initializeEmptyGraph(int n) {
    List<List<Integer>> graph = new ArrayList<>(n);
    for (int i = 0; i < n; i++) 
      graph.add(new ArrayList<>());
    return graph;
  }

  // Add directed edge to graph.
  public static void addDirectedEdge(List<List<Integer>> graph, int from, int to) {
    graph.get(from).add(to);
  }

  public static void verifyEulerianPath(List<List<Integer>> graph) {
    EulerianPathDirectedEdgesAdjacencyList solver;
    solver = new EulerianPathDirectedEdgesAdjacencyList(graph);
    int n = graph.size();
    int[] ordering = solver.getEulerianPath();
    
    if (n == 0) {
      assertThat(ordering.length).isEqualTo(0);
      return;
    }
    System.out.println(Arrays.toString(ordering));
    // Make sure solver actually found an Eulerian Path.
    assertThat(ordering).isNotNull();

    // Make sure tour starts and begins on start and end nodes.
    // assertThat(ordering[0]).isEqualTo(solver.start);
    // assertThat(ordering[ordering.length-1]).isEqualTo(solver.end);

    // Count the frequency of each edge
    Map<Long, Integer> map = new HashMap<>();
    for (int from = 0; from < n; from++) {
      for (int to : graph.get(from)) {
        long hash = ((long)from) << 32 | to;
        Integer count = map.get(hash);
        if (count == null) count = 0;
        map.put(hash, count + 1);
      }
    }

    for (int i = 1; i < ordering.length; i++) {
      int from = ordering[i-1];
      int to = ordering[i];
      
      // Make sure edge has not yet been taken
      long hash = ((long)from) << 32 | to;
      Integer count = map.get(hash);
      assertThat(count).isNotNull();
      assertThat(count).isGreaterThan(0);
      map.put(hash, count - 1);
    }

    // Make sure all edges were used
    for (long hash : map.keySet()) {
      int count = map.get(hash);
      assertThat(count).isEqualTo(0);
    }

  }

  @Test
  public void testGraphAllEqualEdgeFrequency() {
    int n = 2;
    List<List<Integer>> graph = initializeEmptyGraph(n);
    addDirectedEdge(graph, 0, 1);
    addDirectedEdge(graph, 0, 1);
    addDirectedEdge(graph, 1, 0);
    addDirectedEdge(graph, 1, 0);

    EulerianPathDirectedEdgesAdjacencyList solver;
    solver = new EulerianPathDirectedEdgesAdjacencyList(graph);
    assertThat(solver.getEulerianPath()).isNotNull();
  }

  @Test
  public void testInvalidGraph1() {
    int n = 2;
    List<List<Integer>> graph = initializeEmptyGraph(n);
    addDirectedEdge(graph, 0, 1);
    addDirectedEdge(graph, 0, 1);

    EulerianPathDirectedEdgesAdjacencyList solver;
    solver = new EulerianPathDirectedEdgesAdjacencyList(graph);
    assertThat(solver.getEulerianPath()).isNull();
  }

  @Test
  public void testInvalidGraph2() {
    int n = 3;
    List<List<Integer>> graph = initializeEmptyGraph(n);
    addDirectedEdge(graph, 0, 1);
    addDirectedEdge(graph, 1, 0);
    addDirectedEdge(graph, 1, 2);
    addDirectedEdge(graph, 2, 0);
    addDirectedEdge(graph, 2, 0);

    EulerianPathDirectedEdgesAdjacencyList solver;
    solver = new EulerianPathDirectedEdgesAdjacencyList(graph);
    assertThat(solver.getEulerianPath()).isNull();
  }

  @Test
  public void testOneNodeSelfLoopGraph() {
    int n = 1;
    List<List<Integer>> graph = initializeEmptyGraph(n);
    addDirectedEdge(graph, 0, 0);
    
    solver = new EulerianPathDirectedEdgesAdjacencyList(graph);

    int[] ordering = solver.getEulerianPath();
    EulerianPathDirectedEdgesAdjacencyList solver;
    solver = new EulerianPathDirectedEdgesAdjacencyList(graph);
    assertThat(solver.getEulerianPath()).isNull();
  }

  @Test
  public void testTwoNodeGraph() {

  }

  @Test
  public void testSimpleGraph() {
    int n = 5;
    List<List<Integer>> graph = initializeEmptyGraph(n);

    addDirectedEdge(graph, 0, 1);
    addDirectedEdge(graph, 1, 2);
    addDirectedEdge(graph, 1, 3);
    addDirectedEdge(graph, 1, 4);
    addDirectedEdge(graph, 2, 1);
    addDirectedEdge(graph, 4, 1);

    verifyEulerianPath(graph);
  }

  // There should be an Eulerian path on this directed graph from node 1 to node 0;
  @Test
  public void testSomewhatComplexPath() {
    int n = 9;
    List<List<Integer>> graph = initializeEmptyGraph(n);

    // Component connecting edges
    addDirectedEdge(graph, 6, 7);
    addDirectedEdge(graph, 4, 1);
    addDirectedEdge(graph, 7, 0);
    addDirectedEdge(graph, 1, 5);
    addDirectedEdge(graph, 1, 3);

    addDirectedEdge(graph, 1, 2);
    addDirectedEdge(graph, 1, 2);
    addDirectedEdge(graph, 2, 1);
    addDirectedEdge(graph, 2, 1);

    addDirectedEdge(graph, 3, 4);
    addDirectedEdge(graph, 3, 4);
    addDirectedEdge(graph, 4, 3);

    addDirectedEdge(graph, 5, 6);
    addDirectedEdge(graph, 5, 6);
    addDirectedEdge(graph, 6, 5);

    addDirectedEdge(graph, 7, 8);
    addDirectedEdge(graph, 8, 7);

    verifyEulerianPath(graph);
  }

}