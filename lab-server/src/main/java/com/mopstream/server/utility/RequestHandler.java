package com.mopstream.server.utility;

import com.mopstream.common.interaction.Request;
import com.mopstream.common.interaction.Response;
import com.mopstream.common.interaction.ResponseCode;

/**
 * Handles requests.
 */
public class RequestHandler {
    private CommandManager commandManager;

    public RequestHandler(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * Handles requests.
     *
     * @param request Request to be processed.
     * @return Response to request.
     */
    public Response handle(Request request) {
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                request.getCommandObjectArgument());
        return new Response(responseCode, ResponseOutputer.getAndClear());
    }

    /**
     * Executes a command from a request.
     *
     * @param command               Name of command.
     * @param commandStringArgument String argument for command.
     * @param commandObjectArgument Object argument for command.
     * @return Command execute status.
     */
    private ResponseCode executeCommand(String command, String commandStringArgument,
                                        Object commandObjectArgument) {
        switch (command) {
            case "":
                break;
            case "add":
                if (!commandManager.add(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "clear":
                if (!commandManager.clear(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "execute_script":
                if (!commandManager.executeScript(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "exit":
                if (!commandManager.exit(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "filter_by_discipline":
                if (!commandManager.filterByDiscipline(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "filter_less_than_minimal_point":
                if (!commandManager.filterLessThanMinimalPoint(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "help":
                if (!commandManager.help(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "head":
                if (!commandManager.head(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "info":
                if (!commandManager.info(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "print_ascending":
                if (!commandManager.printAscending(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "remove_by_id":
                if (!commandManager.removeById(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "remove_first":
                if (!commandManager.removeFirst(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "remove_lower":
                if (!commandManager.removeLower(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "show":
                if (!commandManager.show(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "update":
                if (!commandManager.update(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
                break;
            case "save":
                if(!commandManager.save(commandStringArgument, commandObjectArgument))
                    return ResponseCode.ERROR;
            default:
                ResponseOutputer.appendln("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                return ResponseCode.ERROR;
        }
        return ResponseCode.OK;
    }
}