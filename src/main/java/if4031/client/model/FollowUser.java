package if4031.client.model;

import if4031.client.command.cassandra.FollowUserCassandraCommand;

/**
 * Created by nim_13512065 on 11/8/15.
 */
public class FollowUser implements CassandraModel {
    private final String username;
    private final String follower;

    public FollowUser (FollowUserCassandraCommand followUserCassandraCommand) {
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
