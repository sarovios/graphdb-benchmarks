package eu.socialsensor;

import eu.socialsensor.graphdatabases.GraphDatabase;
import eu.socialsensor.graphdatabases.Neo4jGraphDatabase;
import org.junit.Before;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 * Neo4j graph database tests
 *
 * @author sotbeis
 * @email sot.beis@gmail.com
 */
public class Test {

    GraphDatabase gdb;

    @Before
    public void setup() {
        gdb = new Neo4jGraphDatabase();
    }

    @org.junit.Test
    public void testOpen() {
        gdb.open(getClass().getResource("test.gdb").getPath());
    }
}
