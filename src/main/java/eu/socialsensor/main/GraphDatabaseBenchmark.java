package eu.socialsensor.main;

import eu.socialsensor.benchmarks.Benchmark;
import eu.socialsensor.utils.Utils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Main class for the execution of GraphDatabaseBenchmark.
 * Choose one of the following benchmarks by removing the comments
 * 
 * @author sotbeis
 * @email sotbeis@iti.gr
 *
 */
public class GraphDatabaseBenchmark {

	//TODO: move to enums
	public final static String TITAN = "titan";
	public final static String ORIENTDB = "orientdb";
	public final static String NEO4J = "neo4j";
	public final static String SPARKSEE = "sparksee";
	
	public static boolean TITAN_SELECTED = false;
	public static boolean ORIENTDB_SELECTED = false;
	public static boolean NEO4J_SELECTED = false;
	public static boolean SPARKSEE_SELECTED = false;
			
	public final static String ORIENTDB_PATH = "graphDBs/OrientDB";
	public final static String TITANDB_PATH = "graphDBs/TitanDB";
	public final static String NEO4JDB_PATH = "graphDBs/Neo4jDB";	
	public final static String SPARKSEEDB_PATH = "graphDBs/SparkseeDB";
	
	public final static String MASSIVE_INSERTION_BENCHMARK = "MIW";
	public final static String SINGLE_INSERTION_BENCHMARK = "SIW";
	public final static String FIND_NEIGHBOURS_BENCHMARK = "QW-FN";
	public final static String FIND_ADJACENT_NODES_BENCHMARK = "QW-FA";
	public final static String FIND_SHORTEST_PATH_BENCHMARK = "QW-FS";
	public final static String CLUSTERING_BENCHMARK = "CW";
	public final static String ALL_BENCHMARK = "ALL";
	
	public static int SCENARIOS = 0;
	
	public static String RESULTS_PATH;
		
	public static Properties inputPropertiesFile = new Properties();
	
	/**
	 * This is the main function. Set the proper property file and run
	 * @throws ExecutionException 
	 */
	public static void main(String[] args) throws ExecutionException {
		
		Logger logger =  Logger.getLogger(GraphDatabaseBenchmark.class.getName());
		logger.setLevel(Level.INFO);
		
		InputStream input = null;
		
		try {
			if(args.length !=1) {
				input = new FileInputStream("config/input.properties");
			}
			else {
				input = new FileInputStream(args[0]);
			}
			inputPropertiesFile.load(input);
			String dataset = inputPropertiesFile.getProperty("DATASET");
			String benchmarkProperty = inputPropertiesFile.getProperty("BENCHMARK");
			String selectedDatabases = inputPropertiesFile.getProperty("DATABASES");
			String benchmarkClass = null;
			Constructor<?> constructor = null;
			Benchmark benchmark = null;
			
			Utils utils = new Utils();
			utils.selectDatabases(selectedDatabases);
			GraphDatabaseBenchmark.SCENARIOS = utils.calculateFactorial(selectedDatabases.split(",").length);
			GraphDatabaseBenchmark.RESULTS_PATH = inputPropertiesFile.getProperty("RESULTS_PATH");
			
			File resultsPath = new File(GraphDatabaseBenchmark.RESULTS_PATH);
			if(!resultsPath.exists()) {
				resultsPath.mkdirs();
			}
			
			if(benchmarkProperty.equals(MASSIVE_INSERTION_BENCHMARK)) {
				logger.info("Massive Insertion Benchmark Selected");
				benchmarkClass = inputPropertiesFile.getProperty("MIW_CLASS");
				constructor = Class.forName(benchmarkClass).getConstructor(String.class);
				benchmark = (Benchmark) constructor.newInstance(dataset);
			}
			else if(benchmarkProperty.equals(SINGLE_INSERTION_BENCHMARK)) {
				logger.info("Single Insertion Benchmark Selected");
				benchmarkClass = inputPropertiesFile.getProperty("SIW_CLASS");
				constructor = Class.forName(benchmarkClass).getConstructor(String.class);
				benchmark = (Benchmark) constructor.newInstance(dataset);
			}
			else {
				if(benchmarkProperty.equals(FIND_NEIGHBOURS_BENCHMARK)) {
					logger.info("Find Neighbours of All Nodes Benchmark Selected");
					utils.createDatabases(dataset);
					benchmarkClass = inputPropertiesFile.getProperty("QW-FN_CLASS");
				}
				else if(benchmarkProperty.equals(FIND_ADJACENT_NODES_BENCHMARK)) {
					logger.info("Find Adjacent Nodes of All Edges Benchmark Selected");
					utils.createDatabases(dataset);
					benchmarkClass = inputPropertiesFile.getProperty("QW-FA_CLASS");
				}
				else if(benchmarkProperty.equals(FIND_SHORTEST_PATH_BENCHMARK)) {
					logger.info("Find Shortest Path Benchmark Selected");
					utils.createDatabases(dataset);
					benchmarkClass = inputPropertiesFile.getProperty("QW-FS_CLASS");
				}
				else if(benchmarkProperty.equals(CLUSTERING_BENCHMARK)) {
					logger.info("Clustering Benchmark Selected");
					utils.createDatabases(dataset);
					benchmarkClass = inputPropertiesFile.getProperty("CW_CLASS");
				}
				constructor = Class.forName(benchmarkClass).getConstructor();
				benchmark = (Benchmark) constructor.newInstance();
			}
			benchmark.startBenchmark();
			utils.deleteDatabases();
			input.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		} 
		catch (SecurityException e) {
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		catch (InstantiationException e) {
			e.printStackTrace();
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
