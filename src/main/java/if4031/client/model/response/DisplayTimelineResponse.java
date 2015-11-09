package if4031.client.model.response;

import if4031.client.model.Timeline;
import if4031.client.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nim_13512065 on 11/9/15.
 */
public class DisplayTimelineResponse implements CassandraModelResponse {
    private List<Map<User, Timeline>> tweets;

    public DisplayTimelineResponse() {
        tweets = new ArrayList<>();
    }

    public void addtweets(User user, Timeline timeline) {
        Map<User, Timeline> userTimelineMap = new HashMap<User, Timeline>();
        userTimelineMap.put(user, timeline);
        tweets.add(userTimelineMap);
    }

    public List<Map<User, Timeline>> getTweets() {
        return tweets;
    }

    public void setTweets(List<Map<User, Timeline>> tweets) {
        this.tweets = tweets;
    }
}
