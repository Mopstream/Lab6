package com.mopstream.server.commands;

import com.mopstream.common.interaction.NewLab;
import com.mopstream.server.utility.ResponseOutputer;
import com.mopstream.common.data.LabWork;
import com.mopstream.common.exceptions.QueueIsFullException;
import com.mopstream.common.exceptions.WrongAmountOfElementsException;
import com.mopstream.server.utility.CollectionManager;

import java.time.LocalDate;

/**
 * Command 'add'. Adds a new element to collection.
 */
public class AddCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public AddCommand(CollectionManager collectionManager) {
        super("add", "{element}", "добавить новый элемент в коллекцию");
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
            NewLab newLab = (NewLab) objectArgument;
            collectionManager.addToCollection(new LabWork(
                    collectionManager.generateNextId(),
                    newLab.getName(),
                    newLab.getCoordinates(),
                    LocalDate.now(),
                    newLab.getMinimalPoint(),
                    newLab.getDifficulty(),
                    newLab.getDiscipline()));
            ResponseOutputer.appendln("Лабораторная работа успешно добавлена!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("Переданный клиентом объект неверен!");
        } catch (QueueIsFullException e) {
            ResponseOutputer.appenderror("Коллекция переполнена. Клиент больше не может создавать объекты!");
        }
        return false;
    }
}