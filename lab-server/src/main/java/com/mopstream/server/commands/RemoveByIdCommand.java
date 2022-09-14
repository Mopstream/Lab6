package com.mopstream.server.commands;

import com.mopstream.common.data.LabWork;
import com.mopstream.common.exceptions.LabWorkNotFoundException;
import com.mopstream.common.exceptions.WrongAmountOfElementsException;
import com.mopstream.common.exceptions.CollectionIsEmptyException;
import com.mopstream.server.utility.CollectionManager;
import com.mopstream.server.utility.ResponseOutputer;

/**
 * Command 'remove_by_id'. Removes the element by its ID.
 */
public class RemoveByIdCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public RemoveByIdCommand(CollectionManager collectionManager) {
        super("remove_by_id", "<ID>", "удалить элемент из коллекции по ID");
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
            if (stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            Long id = Long.parseLong(stringArgument);
            LabWork labWorkToRemove = collectionManager.getById(id);
            if (labWorkToRemove == null) throw new LabWorkNotFoundException();
            collectionManager.removeFromCollection(labWorkToRemove);
            ResponseOutputer.appendln("Лабораторная работа успешно удалена!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appenderror("ID должен быть представлен числом!");
        } catch (LabWorkNotFoundException exception) {
            ResponseOutputer.appenderror("Лабораторной работы с таким ID в коллекции нет!");
        }
        return false;
    }
}