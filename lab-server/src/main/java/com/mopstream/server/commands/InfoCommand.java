package com.mopstream.server.commands;

import java.time.LocalDate;

import com.mopstream.common.exceptions.WrongAmountOfElementsException;
import com.mopstream.server.utility.CollectionManager;
import com.mopstream.server.utility.ResponseOutputer;

/**
 * Command 'info'. Prints information about the collection.
 */
public class InfoCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "", "вывести информацию о коллекции");
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
            LocalDate lastInitTime = collectionManager.getLastInitTime();
            String lastInitTimeString = (lastInitTime == null) ? "в данной сессии инициализации еще не происходило" : lastInitTime.toString();
            LocalDate lastSaveTime = collectionManager.getLastSaveTime();
            String lastSaveTimeString = (lastSaveTime == null) ? "в данной сессии сохранения еще не происходило" :
                    lastSaveTime.toString();

            ResponseOutputer.appendln("Сведения о коллекции:");
            ResponseOutputer.appendln(" Тип: " + collectionManager.collectionType());
            ResponseOutputer.appendln(" Количество элементов: " + collectionManager.collectionSize());
            ResponseOutputer.appendln(" Дата последнего сохранения: " + lastSaveTimeString);
            ResponseOutputer.appendln(" Дата последней инициализации: " + lastInitTimeString);
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}