package if4031.client.model.response;

import if4031.client.model.Tweet;

import java.util.List;

/**
 * Created by nim_13512065 on 11/9/15.
 */
public class DisplayTweetResponse {
    private final List<Tweet> tweetList;

    public DisplayTweetResponse(List<Tweet> tweetList) {
        this.tweetList = tweetList;
    }

    public List<Tweet> getTweetList() {
        return tweetList;
    }
}
