package com.mopstream.server;

import com.mopstream.common.exceptions.ConnectionErrorException;
import com.mopstream.common.exceptions.OpeningServerSocketException;
import com.mopstream.server.commands.*;
import com.mopstream.server.utility.*;

import java.io.IOException;

/**
 * Main server class. Creates all server instances.
 *
 */
public class AppServer {
    public static final int PORT = 1337;
    public static final String ENV_VARIABLE = "LAB6";

    public static void main(String[] args) {

        FileManager fileManager = new FileManager(ENV_VARIABLE);
        CollectionManager collectionManager = new CollectionManager(fileManager);
        CommandManager commandManager = new CommandManager(
                new AddCommand(collectionManager),
                new ClearCommand(collectionManager),
                new ExecuteScriptCommand(),
                new ExitCommand(),
                new FilterByDisciplineCommand(collectionManager),
                new FilterLessThanMinimalPointCommand(collectionManager),
                new HeadCommand(collectionManager),
                new HelpCommand(),
                new InfoCommand(collectionManager),
                new PrintAscendingCommand(collectionManager),
                new RemoveByIdCommand(collectionManager),
                new RemoveFirstCommand(collectionManager),
                new RemoveLowerCommand(collectionManager),
                new ShowCommand(collectionManager),
                new UpdateCommand(collectionManager),
                new SaveCommand(collectionManager)
        );
        RequestHandler requestHandler = new RequestHandler(commandManager);
        ShutdownHandling.addCollectionSavingHook(requestHandler);
        NewServer server = new NewServer(PORT, requestHandler);
        server.run();
    }
}
