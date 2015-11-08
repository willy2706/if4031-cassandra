package if4031.client.model;

import if4031.client.command.cassandra.AddTweetCassandraCommand;

import java.util.UUID;

/**
 * Created by nim_13512065 on 11/8/15.
 */
public class Tweet implements CassandraModel  {
    private String username;
    private String body;
    private UUID tweet_id;
    public Tweet(AddTweetCassandraCommand addTweetCassandraCommand) {
        this.setUsername(addTweetCassandraCommand.getUsername());
        this.setBody(addTweetCassandraCommand.getBody());
        this.setTweet_id(UUID.randomUUID());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public UUID getTweet_id() {
        return tweet_id;
    }

    public void setTweet_id(UUID tweet_id) {
        this.tweet_id = tweet_id;
    }
}
