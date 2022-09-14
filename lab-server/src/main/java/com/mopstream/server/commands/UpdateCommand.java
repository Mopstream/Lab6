package com.mopstream.server.commands;

import com.mopstream.common.data.*;
import com.mopstream.common.exceptions.CollectionIsEmptyException;
import com.mopstream.common.exceptions.LabWorkNotFoundException;
import com.mopstream.common.exceptions.WrongAmountOfElementsException;
import com.mopstream.common.interaction.NewLab;
import com.mopstream.server.utility.CollectionManager;
import com.mopstream.server.utility.ResponseOutputer;

import java.time.LocalDate;

/**
 * Command 'update'. Updates the information about selected labwork.
 */
public class UpdateCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager) {
        super("update", "<ID> {element}", "обновить значение элемента коллекции по ID");
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
            if (stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();

            Long id = Long.parseLong(stringArgument);
            if (id <= 0) throw new NumberFormatException();
            LabWork oldLabWork = collectionManager.getById(id);
            if (oldLabWork == null) throw new LabWorkNotFoundException();

            NewLab newLab = (NewLab)objectArgument;



            String name = newLab.getName() == null ? oldLabWork.getName() : newLab.getName();
            Coordinates coordinates = newLab.getCoordinates() == null ? oldLabWork.getCoordinates() : newLab.getCoordinates();
            LocalDate creationDate = oldLabWork.getCreationDate();
            Long minimalPoint = newLab.getMinimalPoint() == -1 ? oldLabWork.getMinimalPoint() : newLab.getMinimalPoint();
            Difficulty difficulty = newLab.getDifficulty() == null ? oldLabWork.getDifficulty() : newLab.getDifficulty();
            Discipline discipline = newLab.getDiscipline() == null ? oldLabWork.getDiscipline() : newLab.getDiscipline();

            collectionManager.removeFromCollection(oldLabWork);
            collectionManager.addToCollection(new LabWork(id, name, coordinates, creationDate, minimalPoint, difficulty, discipline));
            ResponseOutputer.appendln("Лабораторная работа успешно изменена!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appenderror("ID должен быть представлен положительным числом!");
        } catch (LabWorkNotFoundException exception) {
            ResponseOutputer.appenderror("Лабораторной работы с таким ID в коллекции нет!");
        }
        return false;
    }
}