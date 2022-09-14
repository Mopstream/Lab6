package com.mopstream.server.commands;

import com.mopstream.common.exceptions.WrongAmountOfElementsException;
import com.mopstream.server.utility.CollectionManager;
import com.mopstream.server.utility.ResponseOutputer;

/**
 * Command 'save'. Saves the collection to a file.
 */
public class SaveCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public SaveCommand(CollectionManager collectionManager) {
        super("save", "", "сохранить коллекцию в файл");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            collectionManager.saveCollection();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}