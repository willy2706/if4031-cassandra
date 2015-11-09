package if4031.client.model.request;

import if4031.client.command.cassandra.FollowUserCassandraCommand;

/**
 * Created by nim_13512065 on 11/8/15.
 */
public class FollowUserRequest implements CassandraModelRequest {
    private final String username;
    private final String follower;

    public FollowUserRequest(FollowUserCassandraCommand followUserCassandraCommand) {
        this.username = followUserCassandraCommand.getUsername();
        this.follower = followUserCassandraCommand.getFollower();
    }

    public String getUsername() {
        return username;
    }

    public String getFollower() {
        return follower;
    }

}
