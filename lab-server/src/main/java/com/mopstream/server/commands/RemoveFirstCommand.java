package com.mopstream.server.commands;

import com.mopstream.common.exceptions.CollectionIsEmptyException;
import com.mopstream.common.exceptions.WrongAmountOfElementsException;
import com.mopstream.server.utility.CollectionManager;
import com.mopstream.server.utility.ResponseOutputer;

/**
 * Command 'remove_greater'. Removes head of the collection.
 */
public class RemoveFirstCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public RemoveFirstCommand(CollectionManager collectionManager) {
        super("remove_first", "", "удалить первый элемент из коллекции");
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
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            collectionManager.removeFirst();
            ResponseOutputer.appendln("Лаба успешно удалена!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        }
        return false;
    }
}