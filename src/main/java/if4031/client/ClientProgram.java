package if4031.client;

import if4031.client.accessor.CassandraAccessor;
import if4031.client.config.ClientConfiguration;
import if4031.client.config.PropertyConfiguration;

import java.io.IOException;
import java.util.Scanner;


public class ClientProgram {

    private static final String PROPERTY_FILE = "/client.properties";

    public static void main(String[] args) throws IOException, InterruptedException {
        ClientConfiguration configuration = new PropertyConfiguration(PROPERTY_FILE); // throws IOException
        String brokerAddress = configuration.getString("brokerAddress");
        String zookeeperAddress = configuration.getString("zookeeperAddress");
        CassandraAccessor cassandraAccessor = new CassandraAccessor(brokerAddress, zookeeperAddress);

        Scanner scanner = new Scanner(System.in);
        CLInterface clInterface = new CLInterface(scanner, System.out, cassandraAccessor);

        cassandraAccessor.start();
        clInterface.run();

        cassandraAccessor.stop();
        System.exit(0);
    }
}
