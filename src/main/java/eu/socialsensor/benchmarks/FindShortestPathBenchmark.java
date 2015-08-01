package eu.socialsensor.benchmarks;

import eu.socialsensor.graphdatabases.GraphDatabase;
import eu.socialsensor.graphdatabases.Neo4jGraphDatabase;
//import eu.socialsensor.graphdatabases.OrientGraphDatabase;
import eu.socialsensor.graphdatabases.SparkseeGraphDatabase;
import eu.socialsensor.graphdatabases.TitanGraphDatabase;
import eu.socialsensor.main.GraphDatabaseBenchmark;
import eu.socialsensor.utils.PermuteMethod;
import eu.socialsensor.utils.Utils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * FindShortestPathBenchmark implementation
 * @author sotbeis
 * @email sotbeis@iti.gr
 */
public class FindShortestPathBenchmark implements Benchmark {
	
	public static Set<Integer> generatedNodes;
	
	public static final String QW_FS_RESULTS = "QW-FSResults.txt";
	
	private Logger logger = Logger.getLogger(FindShortestPathBenchmark.class);
	
	private double[] orientTimes = new double[GraphDatabaseBenchmark.SCENARIOS];
	private double[] titanTimes = new double[GraphDatabaseBenchmark.SCENARIOS];
	private double[] neo4jTimes = new double[GraphDatabaseBenchmark.SCENARIOS];
	private double[] sparkseeTimes = new double[GraphDatabaseBenchmark.SCENARIOS];
	
	private int titanScenarioCount = 0;
	private int orientScenarioCount = 0;
	private int neo4jScenarioCount = 0;
	private int sparkseeScenarioCount = 0;
	
	@Override
	public void startBenchmark() {
		
		logger.setLevel(Level.INFO);
		logger.info("Executing Find Shortest Path Benchmark . . . .");
		Utils utils = new Utils();
		generateRandomNodes();
		
		utils.clearGC();
		
		Class<FindShortestPathBenchmark> c = FindShortestPathBenchmark.class;
		Method[] methods = utils.filter(c.getDeclaredMethods(), "FindShortestPathBenchmark");
		PermuteMethod permutations = new PermuteMethod(methods);
		int cntPermutations = 1;
		while(permutations.hasNext()) {
			logger.info("Scenario " + cntPermutations++);
			for(Method permutation : permutations.next()) {
				try {
					permutation.invoke(this, null);
					utils.clearGC();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				catch (IllegalArgumentException e) {
						e.printStackTrace();
				}
				catch (InvocationTargetException e) {
						e.printStackTrace();
				}
			}
		}
		
		System.out.println("");
		logger.info("Find Shortest Path Benchmark finished");
		
		utils.writeResults(titanTimes, orientTimes, neo4jTimes, sparkseeTimes, QW_FS_RESULTS, 
				"Find Shortest Path");
	}
	
//	@SuppressWarnings("unused")
//	private void orientdbFindShortestPathBenchmark() {
//		GraphDatabase orientGraphDatabase = new OrientGraphDatabase();
//		orientGraphDatabase.open(GraphDatabaseBenchmark.ORIENTDB_PATH);
//		long start = System.currentTimeMillis();
//		orientGraphDatabase.shorestPathQuery();
//		long orientTime = System.currentTimeMillis() - start;
//		orientGraphDatabase.shutdown();
//		orientTimes[orientScenarioCount] = orientTime / 1000.0;
//		orientScenarioCount++;
//	}
	
	@SuppressWarnings("unused")
	private void titanFindShortestPathBenchmark() {
		GraphDatabase titanGraphDatabase = new TitanGraphDatabase();
		titanGraphDatabase.open(GraphDatabaseBenchmark.TITANDB_PATH);
		long start = System.currentTimeMillis();
		titanGraphDatabase.shorestPathQuery();
		long titanTime = System.currentTimeMillis() - start;
		titanGraphDatabase.shutdown();
		titanTimes[titanScenarioCount] = titanTime / 1000.0;
		titanScenarioCount++;
	}
	
	@SuppressWarnings("unused")
	private void neo4jFindShortestPathBenchmark() {
		GraphDatabase neo4jGraphDatabase = new Neo4jGraphDatabase();
		neo4jGraphDatabase.open(GraphDatabaseBenchmark.NEO4JDB_PATH);
		long start = System.currentTimeMillis();
		neo4jGraphDatabase.shorestPathQuery();
		long neo4jTime = System.currentTimeMillis() - start;
		neo4jGraphDatabase.shutdown();
		neo4jTimes[neo4jScenarioCount] = neo4jTime / 1000.0;
		neo4jScenarioCount++;
	}
	
	@SuppressWarnings("unused")
	private void sparkseeFindShortestPathBenchmark() {
		GraphDatabase sparkseeGraphDatabase = new SparkseeGraphDatabase();
		sparkseeGraphDatabase.open(GraphDatabaseBenchmark.SPARKSEEDB_PATH);
		long start = System.currentTimeMillis();
		sparkseeGraphDatabase.shorestPathQuery();
		long sparkseeTime = System.currentTimeMillis() - start;
		sparkseeGraphDatabase.shutdown();
		sparkseeTimes[sparkseeScenarioCount] = sparkseeTime / 1000.0;
		sparkseeScenarioCount++;
	}
	
	public void generateRandomNodes() {
		Random rand = new Random();
		generatedNodes = new HashSet<Integer>();
		int max = 1000;
		int min = 2;
		int numberOfGeneratedNodes = 100;
		GraphDatabase graphDatabase = null;
		if(GraphDatabaseBenchmark.TITAN_SELECTED) {
			graphDatabase = new TitanGraphDatabase();
			graphDatabase.open(GraphDatabaseBenchmark.TITANDB_PATH);
		}
//		else if(GraphDatabaseBenchmark.ORIENTDB_SELECTED) {
//			graphDatabase = new OrientGraphDatabase();
//			graphDatabase.open(GraphDatabaseBenchmark.ORIENTDB_PATH);
//		}
		else if(GraphDatabaseBenchmark.NEO4J_SELECTED) {
			graphDatabase = new Neo4jGraphDatabase();
			graphDatabase.open(GraphDatabaseBenchmark.NEO4JDB_PATH);
		}
		else if(GraphDatabaseBenchmark.SPARKSEE_SELECTED) {
			graphDatabase = new SparkseeGraphDatabase();
			graphDatabase.open(GraphDatabaseBenchmark.SPARKSEEDB_PATH);
		}
		while(generatedNodes.size() < numberOfGeneratedNodes) {
			int randomNum = rand.nextInt((max - min) +1) + min;
			if(graphDatabase.nodeExists(randomNum)) {
				generatedNodes.add(randomNum);
			}
		}
		graphDatabase.shutdown();
	}

}
