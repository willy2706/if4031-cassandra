package if4031.client.model;

import java.util.UUID;

/**
 * Created by nim_13512065 on 11/9/15.
 */
public class Tweet {
    private final String username; //ini adalah orang yang tweet
    private final UUID timeuuid;
    private final UUID tweet_id;
    private final String body;

    public Tweet(String username, UUID timeuuid, UUID tweet_id, String body) {
        this.username = username;
        this.timeuuid = timeuuid;
        this.tweet_id = tweet_id;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public UUID getTweet_id() {
        return tweet_id;
    }

    public UUID getTimeuuid() {
        return timeuuid;
    }

    public String getUsername() {
        return username;
    }
}
