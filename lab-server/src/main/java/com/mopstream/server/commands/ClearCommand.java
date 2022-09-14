package com.mopstream.server.commands;

import com.mopstream.common.exceptions.WrongAmountOfElementsException;
import com.mopstream.server.utility.CollectionManager;
import com.mopstream.server.utility.ResponseOutputer;

/**
 * Command 'clear'. Clears the collection.
 */
public class ClearCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        super("clear","", "очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument) {
        boolean result = false;
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            collectionManager.clearCollection();
            ResponseOutputer.appendln("Коллекция очищена!");
            result = true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        }
        return result;
    }
}