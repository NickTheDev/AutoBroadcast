/*
Copyright 2020 NickTheDev

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.nickthedev.broadcasting.command;

import com.nickthedev.broadcasting.command.args.Reload;
import com.nickthedev.broadcasting.command.args.Say;
import com.nickthedev.broadcasting.command.args.Toggle;
import com.nickthedev.broadcasting.command.args.Version;
import com.nickthedev.broadcasting.util.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages command {@link Argument}s and executing them.
 *
 * @author NickTheDev
 * @since 1.0
 */
public class CommandManager implements CommandExecutor, TabCompleter {

    private final Map<ArgInfo, Argument> arguments = new HashMap<>();

    /**
     * Creates a new command manager and registers all default arguments.
     */
    public CommandManager() {
        Arrays.asList(new Help(), new Say(), new Reload(), new Toggle(), new Version()).forEach(this::register);
    }

    /**
     * Gets the argument info with the specified name.
     *
     * @param name Name of the argument.
     * @return Argument with the specified name.
     */
    private Optional<ArgInfo> getArgument(String name) {
        return arguments.keySet().stream().filter(args -> args.name().equals(name)).findFirst();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            Chat.to(sender, Chat.ERROR, "Please specify an argument, or use /ab help for a list.");

            return false;
        }

        if(!getArgument(args[0]).isPresent()) {
            Chat.to(sender, Chat.ERROR, "Unknown command, use /ab help for a list of commands.");

            return false;
        }

        ArgInfo info = getArgument(args[0]).get();

        if(!info.permission().isEmpty() && !sender.hasPermission(info.permission())) {
            Chat.to(sender, Chat.ERROR, "You do not have permission ot use this command.");

            return false;
        }

        try {
            arguments.get(info).execute(sender, args);

        } catch (CommandException e) {
            Chat.to(sender, Chat.ERROR, e.getMessage());

            return false;
        }

        return true;
    }

    /**
     * Registers the specified argument to this command manager.
     *
     * @param argument Argument to register.
     * @param <T> Type of argument.
     */
    private <T extends Argument> void register(T argument) {
        if(arguments.containsValue(argument)) {
            return;
        }

        arguments.put(argument.getClass().getAnnotation(ArgInfo.class), argument);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
       return args.length < 2 ? arguments.keySet().stream().map(ArgInfo::name).collect(Collectors.toList()) : null;
    }

    /**
     * Help argument which tells a command sender how to use arguments. This is nested in the command manager
     * class because it needs to take advantage of the argument map to access all argument help messages.
     *
     * @author NickTheDev
     * @since 1.0
     */
    @ArgInfo(name = "help", help = "Displays a list of all commands.")
    private final class Help implements Argument {

        @Override
        public void execute(CommandSender sender, String[] args) throws CommandException {
            Chat.to(sender, Chat.INFO, "Here's a list of commands:");

            arguments.keySet().forEach(info -> Chat.to(sender, "   &e&l/ab " + info.name() + ": &f" + info.help()));
        }

    }

}
