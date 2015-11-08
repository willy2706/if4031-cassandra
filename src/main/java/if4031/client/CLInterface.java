package if4031.client;

import if4031.client.accessor.CassandraAccessor;
import if4031.client.command.*;
import if4031.client.command.cassandra.*;
import if4031.client.model.*;

import java.io.PrintStream;
import java.util.Scanner;

public class CLInterface {

    private final Scanner scanner;
    private final PrintStream out;
    private final CassandraAccessor cassandraAccessor;
    private final CommandParser commandParser = new CommandParser();

    CLInterface(Scanner _scanner, PrintStream _out, CassandraAccessor _cassandraAccessor) {
        scanner = _scanner;
        out = _out;
        cassandraAccessor = _cassandraAccessor;
        MENU[0] = "1. mendaftar user baru\n";
        MENU[1] = "2. follow a friend\n";
        MENU[2] = "3. menambah tweet\n";
        MENU[3] = "4. menampilkan tweet per user\n";
        MENU[4] = "5. menampilkan timeline per user\n";
        MENU[5] = "6. exit\n>>";
        for (int i = 0; i < 6; ++i) {
            COMMAND_PROMPT+=MENU[i];
        }
    }

    private void printMessages() {
//        List<Message> messageList = cassandraAccessor.getMessages();
//        if (messageList != null) {
//            for (Message message : messageList) {
//                out.println("[" + message.getChannel() + "] " + message.getBody());
//            }
//        }
    }

    void run() {
        // display welcome message
        out.println(WELCOME_MESSAGE);

        // main loop
        String commandString;
        while (true) {
            out.print(COMMAND_PROMPT);
            commandString = scanner.nextLine();
//            CommandParser.ParseResult parseResult = commandParser.parse(commandString);
            CommandParser.ParseResultCassandra parseResultCassandra = commandParser.parseCassandraCommand(commandString);
            CommandParser.ParseStatus status = parseResultCassandra.getStatus();
            if (status == CommandParser.ParseStatus.OK) {
                CassandraCommand cmd = parseResultCassandra.getCassandraCommand();
                processCassandra(cmd);

            } else if (status == CommandParser.ParseStatus.EXIT) {
                break;
            }

            printMessages();
        }
    }

    void processCassandra(CassandraCommand cassandraCommand) {
        if (cassandraCommand instanceof RegisterUserCassandraCommand) {
            RegisterUserCassandraCommand registerUserCassandraCommand = (RegisterUserCassandraCommand) cassandraCommand;
            User user = new User(registerUserCassandraCommand);
            cassandraAccessor.registerUser(user);
        } else if (cassandraCommand instanceof FollowUserCassandraCommand) {
            FollowUserCassandraCommand followUserCassandraCommand = (FollowUserCassandraCommand) cassandraCommand;
            FollowUser followUser = new FollowUser(followUserCassandraCommand);
            cassandraAccessor.followUser(followUser);
        } else if (cassandraCommand instanceof AddTweetCassandraCommand) {
            AddTweetCassandraCommand addTweetCassandraCommand = (AddTweetCassandraCommand) cassandraCommand;
            Tweet tweet = new Tweet(addTweetCassandraCommand);
            cassandraAccessor.tweet(tweet);
        } else if (cassandraCommand instanceof DisplayTweetCassandraCommand) {
            cassandraAccessor.displayTweet(new DisplayTweet());
        } else if (cassandraCommand instanceof DisplayTimelineCassandraCommand) {
            cassandraAccessor.displayTimeline(new DisplayTimeline());
        }
    }

    private static String PROGRAM_NAME = "Cassandra NoSQL";
    private static String WELCOME_MESSAGE = "Welcome to " + PROGRAM_NAME + "!\n";
    private static String[] MENU = new String[6];
    private static String COMMAND_PROMPT = "";
}
