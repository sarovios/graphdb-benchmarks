package eu.socialsensor.insert;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.batch.BatchGraph;

import eu.socialsensor.graphdatabases.GraphDatabase;
import eu.socialsensor.graphdatabases.TitanGraphDatabase;
import eu.socialsensor.main.GraphDatabaseBenchmark;

/**
 * Implementation of massive Insertion in Titan
 * graph database
 * 
 * @author sotbeis
 * @email sotbeis@iti.gr
 * 
 */
public class TitanMassiveInsertion implements Insertion {
	
	private BatchGraph<TitanGraph> batchGraph = null;
	
	private Logger logger = Logger.getLogger(TitanMassiveInsertion.class);
	
	public static void main(String[] args) {
		GraphDatabase graph = new TitanGraphDatabase();
		graph.createGraphForMassiveLoad(GraphDatabaseBenchmark.TITANDB_PATH);
		graph.massiveModeLoading("./data/youtubeEdges.txt");
		graph.shutdown();
		
		graph.delete(GraphDatabaseBenchmark.TITANDB_PATH);
	}
	
	public TitanMassiveInsertion(BatchGraph<TitanGraph> batchGraph) {
		this.batchGraph = batchGraph;
	}
	
	@Override
	public void createGraph(String datasetDir) {
		logger.setLevel(Level.INFO);
		logger.info("Loading data in massive mode in Titan database . . . .");
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(datasetDir)));
			String line;
			int lineCounter = 1;
			Vertex srcVertex, dstVertex;
			while((line = reader.readLine()) != null) {
				if(lineCounter > 4) {
					String[] parts = line.split("\t");
					
					srcVertex = getOrCreate(Integer.valueOf(parts[0]));
					dstVertex = getOrCreate(Integer.valueOf(parts[1]));
					
					srcVertex.addEdge("similar", dstVertex);
				}
				lineCounter++;
			}
			reader.close();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private Vertex getOrCreate(Integer value) {
		Vertex vertex = batchGraph.getVertex(value);
		if(vertex == null) {
			vertex = batchGraph.addVertex(value);
			vertex.setProperty("nodeId", value);
		}
		return vertex;
		
	}


}
