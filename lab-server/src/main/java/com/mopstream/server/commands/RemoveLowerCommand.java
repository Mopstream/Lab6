package com.mopstream.server.commands;

import com.mopstream.common.data.LabWork;
import com.mopstream.common.exceptions.*;
import com.mopstream.common.interaction.NewLab;
import com.mopstream.server.utility.CollectionManager;
import com.mopstream.server.utility.ResponseOutputer;

import java.time.LocalDate;

/**
 * Command 'remove_lower'. Removes elements lower than user entered.
 */
public class RemoveLowerCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public RemoveLowerCommand(CollectionManager collectionManager) {
        super("remove_lower", "{element}", "удалить из коллекции все элементы, меньшие, чем заданный");
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
            if (!stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            NewLab newLab = (NewLab) objectArgument;
            LabWork LabToFind = new LabWork(
                    (long) -1,
                    newLab.getName(),
                    newLab.getCoordinates(),
                    LocalDate.now(),
                    newLab.getMinimalPoint(),
                    newLab.getDifficulty(),
                    newLab.getDiscipline()
            );
            LabWork LabFromCollection = collectionManager.getByValue(LabToFind);
            if (LabFromCollection == null) throw new LabWorkNotFoundException();
            collectionManager.removeLower(LabFromCollection);
            ResponseOutputer.appendln("Лабы успешно удалены!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (LabWorkNotFoundException exception) {
            ResponseOutputer.appenderror("Лабораторной работы с такими параметрами в коллекции нет!");
        }
        return false;
    }
}