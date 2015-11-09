package if4031.client.accessor;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import if4031.client.model.Timeline;
import if4031.client.model.Tweet;
import if4031.client.model.User;
import if4031.client.model.request.*;
import if4031.client.model.response.DisplayTimelineResponse;
import if4031.client.model.response.DisplayTweetResponse;
import if4031.client.util.TableName;

import java.lang.reflect.Field;
import java.util.*;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;


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

//    public void insertModel(CassandraModelRequest cassandraModel) {
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

    public void registerUser(RegisterUserRequest registerUserRequest) {
        Insert statement = QueryBuilder.insertInto(getKeyspace(), TableName.USERS);
        for (Field field : registerUserRequest.getClass().getDeclaredFields()) {
            field.setAccessible(true); // if you want to modify private fields
            try {
                statement.value(field.getName(), field.get(registerUserRequest));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        getSession().execute(statement);
    }

    public void followUser(FollowUserRequest followUserRequest) {
        Insert statement = QueryBuilder.insertInto(getKeyspace(), TableName.FRIENDS);
        statement.value("username", followUserRequest.getUsername())
                .value("friend",followUserRequest.getFollower())
                .value("since", followUserRequest.getTimestamp());
        getSession().execute(statement);

        statement = QueryBuilder.insertInto(getKeyspace(), TableName.FOLLOWERS);
        statement.value("username", followUserRequest.getUsername())
                .value("follower",followUserRequest.getFollower())
                .value("since", followUserRequest.getTimestamp());
        getSession().execute(statement);
    }

    public void tweet(AddTweetRequest addTweetRequest) {
        Insert statement = QueryBuilder.insertInto(getKeyspace(), TableName.TWEETS);
        for (Field field : addTweetRequest.getClass().getDeclaredFields()) {
            field.setAccessible(true); // if you want to modify private fields
            try {
                statement.value(field.getName(), field.get(addTweetRequest));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        getSession().execute(statement);

        Statement statements = QueryBuilder.select().all().from(getKeyspace(), TableName.FOLLOWERS)
                .where(eq("follower", addTweetRequest.getUsername()));
        ResultSet results = getSession().execute(statements);
        for (Row row : results) {
            String username = row.getString("username");
            QueryBuilder.insertInto(getKeyspace(), TableName.TIMELINE)
                    .value("text", username)
                    .value("time", addTweetRequest.getTime())
                    .value("tweet_id", addTweetRequest.getTweet_id());
            getSession().execute(statement);
        }
    }

    public DisplayTweetResponse displayTweet(DisplayTweetRequest displayTweetRequest) {
        Statement statements = QueryBuilder.select().all().from(getKeyspace(), TableName.USERS);
        List<Row> userRows = getSession().execute(statements).all();
        Map<User,List<Tweet>> userListMap = new HashMap<>();
        for (Row userRow: userRows) {
            String username = userRow.getString("username");
            User user = new User(username);

            Statement statement = QueryBuilder.select().all().from(getKeyspace(), TableName.TWEETS)
                    .where(eq("username", user.getUsername()));

            List<Tweet> tweetList = new ArrayList<>();
            List<Row> tweetRows = getSession().execute(statement).all();
            for (Row tweetRow : tweetRows) {
                UUID tweetuuid = tweetRow.getUUID("tweet_id");
                String whoTweet = tweetRow.getString("username");
                String tweetBody = tweetRow.getString("body");
                Tweet tweet = new Tweet(whoTweet, null, tweetuuid, tweetBody);
                tweetList.add(tweet);
            }
            userListMap.put(user, tweetList);
        }
        DisplayTweetResponse displayTweetResponse = new DisplayTweetResponse(userListMap);
        return displayTweetResponse;
    }

    public DisplayTimelineResponse displayTimeline(DisplayTimelineRequest displayTimeline) {
        Statement statements = QueryBuilder.select().all().from(getKeyspace(), TableName.USERS);
        List<Row> userRows = getSession().execute(statements).all();
        DisplayTimelineResponse displayTimelineResponse = new DisplayTimelineResponse();

        for (Row userRow: userRows) {
            String username = userRow.getString("username");
            User user = new User(username);

            Statement statement = QueryBuilder.select().all().from(getKeyspace(), TableName.TIMELINE)
                    .where(eq("username", user.getUsername()));

            List<Row> timelineRows = getSession().execute(statement).all();
            List<Tweet> tweetList = new ArrayList<>();
            for (Row timelineRow : timelineRows) {
                UUID tweetuuid = timelineRow.getUUID("tweet_id");
                UUID timeuuid = timelineRow.getUUID("timeuuid");
                Statement tweetStatement = QueryBuilder.select().all().from(getKeyspace(), TableName.TWEETS)
                        .where(eq("tweet_id", tweetuuid));
                Row tweetRow = getSession().execute(tweetStatement).one();
                String whoTweet = tweetRow.getString("username");
                String tweetBody = tweetRow.getString("body");
                Tweet tweet = new Tweet(whoTweet, timeuuid, tweetuuid, tweetBody);
                tweetList.add(tweet);
            }

            Timeline timeline = new Timeline(tweetList);
            displayTimelineResponse.addUserAndTweets(user, timeline);
        }
        return displayTimelineResponse;
    }
}
