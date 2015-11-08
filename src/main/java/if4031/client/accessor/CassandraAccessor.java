package if4031.client.accessor;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import if4031.client.model.*;
import if4031.client.util.TableName;

import java.lang.reflect.Field;


public class CassandraAccessor implements Accessor {

    private final Session session;
    private final Cluster cluster;

    private final String keyspace;
    public CassandraAccessor(String brokerAddress, String zookeeperAddress) {
        keyspace = "willy65";
        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = getCluster().connect(getKeyspace());
    }
    public Session getSession() {
        return session;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void start() {
    }

    public void stop() {
    }

//    public void insertModel(CassandraModel cassandraModel) {
//        Insert statement = QueryBuilder.insertInto(getKeyspace(), cassandraModel.getTableName());
//        for (Field field : cassandraModel.getClass().getDeclaredFields()) {
//            if (field.getName() != "tableName") {
//                field.setAccessible(true); // if you want to modify private fields
//                try {
//                    statement.value(field.getName(), field.get(cassandraModel));
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        getSession().execute(statement);
//    }

    public void registerUser(User user) {
        Insert statement = QueryBuilder.insertInto(getKeyspace(), TableName.USERS);
        for (Field field : user.getClass().getDeclaredFields()) {
            if (field.getName() != "tableName") {
                field.setAccessible(true); // if you want to modify private fields
                try {
                    statement.value(field.getName(), field.get(user));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        getSession().execute(statement);
    }

    public void followUser(FollowUser followUser) {
        //TODO
    }

    public void tweet(Tweet tweet) {
        //TODO
    }

    public void displayTweet(DisplayTweet displayTweet) {
        //TODO
    }

    public void displayTimeline(DisplayTimeline displayTimeline) {
        //TODO
    }
}
