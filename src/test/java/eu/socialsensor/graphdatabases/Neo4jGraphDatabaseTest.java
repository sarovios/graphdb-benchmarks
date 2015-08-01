package eu.socialsensor.graphdatabases;


import eu.socialsensor.utils.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class Neo4jGraphDatabaseTest {

    private GraphDatabase gdb;
    private final String DB_PATH = "./src/test/resources/test.gdb";

    @Before
    public void setup() {
        gdb = new Neo4jGraphDatabase();
    }

    @After
    public void shutDownAndClearResources() {
        gdb.shutdown();
        Utils.deleteRecursively(new File(DB_PATH));
    }

    @Test(expected = RuntimeException.class)
    public void testOpen() {
        gdb.open(DB_PATH);
        new GraphDatabaseFactory()
                .newEmbeddedDatabase(DB_PATH);
        Utils.deleteRecursively(new File(DB_PATH));
    }

//    @Test
//    public void testCreateGraphForSingleLoad() {
//        gdb.createGraphForMassiveLoad(DB_PATH);
//        gdb.
//    }


}
