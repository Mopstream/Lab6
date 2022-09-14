package com.mopstream.server.utility;

import java.util.ArrayList;
import java.util.List;

import com.mopstream.server.commands.Command;

/**
 * Operates the commands.
 */
public class CommandManager {

    private final List<Command> commands = new ArrayList<>();
    private final Command addCommand;
    private final Command clearCommand;
    private final Command executeScriptCommand;
    private final Command exitCommand;
    private final Command filterByDisciplineCommand;
    private final Command filterLessThanMinimalPointCommand;
    private final Command headCommand;
    private final Command helpCommand;
    private final Command infoCommand;
    private final Command printAscendingCommand;
    private final Command removeByIdCommand;
    private final Command removeFirstCommand;
    private final Command removeLowerCommand;
    private final Command showCommand;
    private final Command updateCommand;
    private final Command saveCommand;

    public CommandManager(Command addCommand, Command clearCommand, Command executeScriptCommand, Command exitCommand, Command filterByDisciplineCommand,
                          Command filterLessThanMinimalPointCommand, Command headCommand, Command helpCommand, Command infoCommand, Command printAscendingCommand,
                          Command removeByIdCommand, Command removeFirstCommand, Command removeLowerCommand,
                          Command showCommand, Command updateCommand, Command saveCommand) {
        this.addCommand = addCommand;
        this.clearCommand = clearCommand;
        this.executeScriptCommand = executeScriptCommand;
        this.exitCommand = exitCommand;
        this.filterByDisciplineCommand = filterByDisciplineCommand;
        this.filterLessThanMinimalPointCommand = filterLessThanMinimalPointCommand;
        this.headCommand = headCommand;
        this.helpCommand = helpCommand;
        this.infoCommand = infoCommand;
        this.printAscendingCommand = printAscendingCommand;
        this.removeByIdCommand = removeByIdCommand;
        this.removeFirstCommand = removeFirstCommand;
        this.removeLowerCommand = removeLowerCommand;
        this.showCommand = showCommand;
        this.updateCommand = updateCommand;
        this.saveCommand = saveCommand;

        commands.add(addCommand);
        commands.add(clearCommand);
        commands.add(executeScriptCommand);
        commands.add(exitCommand);
        commands.add(filterByDisciplineCommand);
        commands.add(filterLessThanMinimalPointCommand);
        commands.add(headCommand);
        commands.add(helpCommand);
        commands.add(infoCommand);
        commands.add(printAscendingCommand);
        commands.add(removeByIdCommand);
        commands.add(removeFirstCommand);
        commands.add(removeLowerCommand);
        commands.add(showCommand);
        commands.add(updateCommand);
        commands.add(saveCommand);
    }


    /**
     * @return List of manager's commands.
     */
    public List<Command> getCommands() {
        return commands;
    }

    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean help(String stringArgument, Object objectArgument) {
        if (helpCommand.execute(stringArgument, objectArgument)) {
            for (Command command : commands) {
                if (!command.equals(saveCommand)) {
                    ResponseOutputer.appendtable(command.getName() + " " + command.getUsage(), command.getDescription());
                }
            }
            return true;
        } else return false;
    }

    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean info(String stringArgument, Object objectArgument) {
        return infoCommand.execute(stringArgument, objectArgument);
    }

    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean show(String stringArgument, Object objectArgument) {
        return showCommand.execute(stringArgument, objectArgument);
    }

    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean add(String stringArgument, Object objectArgument) {
        return addCommand.execute(stringArgument, objectArgument);
    }

    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean update(String stringArgument, Object objectArgument) {
        return updateCommand.execute(stringArgument, objectArgument);
    }

    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean removeById(String stringArgument, Object objectArgument) {
        return removeByIdCommand.execute(stringArgument, objectArgument);
    }

    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean clear(String stringArgument, Object objectArgument) {
        return clearCommand.execute(stringArgument, objectArgument);
    }


    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean executeScript(String stringArgument, Object objectArgument) {
        return executeScriptCommand.execute(stringArgument, objectArgument);
    }

    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean exit(String stringArgument, Object objectArgument) {
        return exitCommand.execute(stringArgument, objectArgument);
    }

    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean removeFirst(String stringArgument, Object objectArgument) {
        return removeFirstCommand.execute(stringArgument, objectArgument);
    }

    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean head(String stringArgument, Object objectArgument) {
        return headCommand.execute(stringArgument, objectArgument);
    }

    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean removeLower(String stringArgument, Object objectArgument) {
        return removeLowerCommand.execute(stringArgument, objectArgument);
    }

    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean filterByDiscipline(String stringArgument, Object objectArgument) {
        return filterByDisciplineCommand.execute(stringArgument, objectArgument);
    }

    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean filterLessThanMinimalPoint(String stringArgument, Object objectArgument) {
        return filterLessThanMinimalPointCommand.execute(stringArgument, objectArgument);
    }

    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean printAscending(String stringArgument, Object objectArgument) {
        return printAscendingCommand.execute(stringArgument, objectArgument);
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean save(String stringArgument, Object objectArgument) {
        return saveCommand.execute(stringArgument, objectArgument);
    }

    @Override
    public String toString() {
        return "CommandManager (вспомогательный класс для работы с командами)";
    }
}