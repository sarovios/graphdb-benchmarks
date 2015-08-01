package eu.socialsensor.benchmarks;

import eu.socialsensor.graphdatabases.GraphDatabase;
import eu.socialsensor.graphdatabases.Neo4jGraphDatabase;
//import eu.socialsensor.graphdatabases.OrientGraphDatabase;
import eu.socialsensor.graphdatabases.SparkseeGraphDatabase;
import eu.socialsensor.graphdatabases.TitanGraphDatabase;
//import eu.socialsensor.insert.Neo4jSingleInsertion;
//import eu.socialsensor.insert.OrientSingleInsertion;
import eu.socialsensor.insert.SparkseeSingleInsertion;
import eu.socialsensor.insert.TitanSingleInsertion;
import eu.socialsensor.main.GraphDatabaseBenchmark;
import eu.socialsensor.utils.PermuteMethod;
import eu.socialsensor.utils.Utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * SingleInsertionBenchmak implementation
 * 
 * @author sotbeis
 * @email sotbeis@iti.gr
 */
public class SingleInsertionBenchmark implements Benchmark {
	
	public static String INSERTION_TIMES_OUTPUT_PATH;
	
	private String INSERTION_TIMES_OUTPUT_FILE = "SIWResults";
	private String DATASET_PATH;
		
	private Logger logger = Logger.getLogger(SingleInsertionBenchmark.class);
	
	public SingleInsertionBenchmark(String datasetPath) {
		this.DATASET_PATH = datasetPath;
	}
	
	@Override
	public void startBenchmark() {
		logger.setLevel(Level.INFO);
		System.out.println("");
		logger.info("Executing Single Insertion Benchmark . . . .");
		
		String resultsFolder = GraphDatabaseBenchmark.RESULTS_PATH;
		INSERTION_TIMES_OUTPUT_PATH = resultsFolder + INSERTION_TIMES_OUTPUT_FILE;
		
		Utils utils = new Utils();
		Class<SingleInsertionBenchmark> c = SingleInsertionBenchmark.class;
		Method[] methods = utils.filter(c.getDeclaredMethods(), "SingleInsertionBenchmark");
		PermuteMethod permutations = new PermuteMethod(methods);
		int cntPermutations = 1;
		while(permutations.hasNext()) {
			System.out.println("");
			logger.info("Scenario " + cntPermutations++);
			for(Method permutation : permutations.next()) {
				try {
					permutation.invoke(this, null);
					utils.clearGC();
				} 
				catch (IllegalAccessException e) {
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
		logger.info("Single Insertion Benchmark finished");
		
		System.out.println("");
		logger.info("Write results to " + resultsFolder);
		if(GraphDatabaseBenchmark.TITAN_SELECTED) {
			List<List<Double>> titanInsertionTimesOfEachScenario = new ArrayList<List<Double>>(GraphDatabaseBenchmark.SCENARIOS);
			utils.getDocumentsAs2dList(titanInsertionTimesOfEachScenario, TitanSingleInsertion.INSERTION_TIMES_OUTPUT_PATH);
			List<Double> titanMeanInsertionTimes = utils.calculateMeanList(titanInsertionTimesOfEachScenario);
			utils.writeTimes(titanMeanInsertionTimes, TitanSingleInsertion.INSERTION_TIMES_OUTPUT_PATH);
			utils.deleteMultipleFiles(TitanSingleInsertion.INSERTION_TIMES_OUTPUT_PATH, GraphDatabaseBenchmark.SCENARIOS);
		}
		
//		if(GraphDatabaseBenchmark.ORIENTDB_SELECTED) {
//			List<List<Double>> orientInsertionTimesOfEachScenario = new ArrayList<List<Double>>(GraphDatabaseBenchmark.SCENARIOS);
//			utils.getDocumentsAs2dList(orientInsertionTimesOfEachScenario, OrientSingleInsertion.INSERTION_TIMES_OUTPUT_PATH);
//			List<Double> orientMeanInsertionTimes = utils.calculateMeanList(orientInsertionTimesOfEachScenario);
//			utils.writeTimes(orientMeanInsertionTimes, OrientSingleInsertion.INSERTION_TIMES_OUTPUT_PATH);
//			utils.deleteMultipleFiles(OrientSingleInsertion.INSERTION_TIMES_OUTPUT_PATH, GraphDatabaseBenchmark.SCENARIOS);
//		}
		
//		if(GraphDatabaseBenchmark.NEO4J_SELECTED) {
//			List<List<Double>> neo4jInsertionTimesOfEachScenario = new ArrayList<List<Double>>(GraphDatabaseBenchmark.SCENARIOS);
//			utils.getDocumentsAs2dList(neo4jInsertionTimesOfEachScenario, Neo4jSingleInsertion.INSERTION_TIMES_OUTPUT_PATH);
//			List<Double> neo4jMeanInsertionTimes = utils.calculateMeanList(neo4jInsertionTimesOfEachScenario);
//			utils.writeTimes(neo4jMeanInsertionTimes, Neo4jSingleInsertion.INSERTION_TIMES_OUTPUT_PATH);
//			utils.deleteMultipleFiles(Neo4jSingleInsertion.INSERTION_TIMES_OUTPUT_PATH, GraphDatabaseBenchmark.SCENARIOS);
//		}
		
		if(GraphDatabaseBenchmark.SPARKSEE_SELECTED) {
			List<List<Double>> sparkseeInsertionTimesOfEachScenario = new ArrayList<List<Double>>(GraphDatabaseBenchmark.SCENARIOS);
			utils.getDocumentsAs2dList(sparkseeInsertionTimesOfEachScenario, SparkseeSingleInsertion.INSERTION_TIMES_OUTPUT_PATH);
			List<Double> sparkseeMeanInsertionTimes = utils.calculateMeanList(sparkseeInsertionTimesOfEachScenario);
			utils.writeTimes(sparkseeMeanInsertionTimes, SparkseeSingleInsertion.INSERTION_TIMES_OUTPUT_PATH);
			utils.deleteMultipleFiles(SparkseeSingleInsertion.INSERTION_TIMES_OUTPUT_PATH, GraphDatabaseBenchmark.SCENARIOS);
		}
				
	}
	
	@SuppressWarnings("unused")
	private void titanSingleInsertionBenchmark() {
		GraphDatabase titanGraphDatabase = new TitanGraphDatabase();
		titanGraphDatabase.createGraphForSingleLoad(GraphDatabaseBenchmark.TITANDB_PATH);
		titanGraphDatabase.singleModeLoading(DATASET_PATH);
		titanGraphDatabase.shutdown();
		titanGraphDatabase.delete(GraphDatabaseBenchmark.TITANDB_PATH);
	}
		
//	@SuppressWarnings("unused")
//	private void orientdbSingleInsertionBenchmark() {
//		GraphDatabase orientGraphDatabase = new OrientGraphDatabase();
//		orientGraphDatabase.createGraphForSingleLoad(GraphDatabaseBenchmark.ORIENTDB_PATH);
//		orientGraphDatabase.singleModeLoading(DATASET_PATH);
//		orientGraphDatabase.shutdown();
//		orientGraphDatabase.delete(GraphDatabaseBenchmark.ORIENTDB_PATH);
//	}
	
	@SuppressWarnings("unused")
	private void neo4jSingleInsertionBenchmark() {
		GraphDatabase neo4jGraphDatabase = new Neo4jGraphDatabase();
		neo4jGraphDatabase.createGraphForSingleLoad(GraphDatabaseBenchmark.NEO4JDB_PATH);
		neo4jGraphDatabase.singleModeLoading(DATASET_PATH);
		neo4jGraphDatabase.shutdown();
		neo4jGraphDatabase.delete(GraphDatabaseBenchmark.NEO4JDB_PATH);
	}
	
	@SuppressWarnings("unused")
	private void sparkseeSingleInsertionBenchmark() {
		GraphDatabase sparkseeGraphDatabase = new SparkseeGraphDatabase();
		sparkseeGraphDatabase.createGraphForSingleLoad(GraphDatabaseBenchmark.SPARKSEEDB_PATH);
		long start = System.currentTimeMillis();
		sparkseeGraphDatabase.singleModeLoading(DATASET_PATH);
		long time = System.currentTimeMillis() - start;
		System.out.println(time / 1000.0);
		System.out.println(sparkseeGraphDatabase.getNodeCount());
		System.out.println(sparkseeGraphDatabase.getGraphWeightSum());
		sparkseeGraphDatabase.shutdown();
		sparkseeGraphDatabase.delete(GraphDatabaseBenchmark.SPARKSEEDB_PATH);
	}

}
