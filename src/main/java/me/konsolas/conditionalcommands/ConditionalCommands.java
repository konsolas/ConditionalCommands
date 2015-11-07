package me.konsolas.conditionalcommands;

import me.konsolas.conditionalcommands.placeholders.AbstractPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ConditionalCommands extends JavaPlugin {
    public void onEnable() {
        for (Placeholder placeholder : Placeholder.values()) {
            placeholder.getPlaceholder().init(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 || (args.length > 1 && args.length < 5)) {
            sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] > Incorrect number of arguments.");
            sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] >" + ChatColor.GREEN + "   /cc help");
            return false;
        }

        if (args.length == 1) {
            if (args[0].equals("help")) {
                sender.sendMessage(ChatColor.GOLD + "--------=ConditionalCommands=--------");
                sender.sendMessage(ChatColor.GREEN + "  /cc help");
                sender.sendMessage(ChatColor.GREEN + "  /cc <player> unless \"" + ChatColor.LIGHT_PURPLE + "condition" + ChatColor.GREEN + "\" do \"" + ChatColor.LIGHT_PURPLE + "command" + ChatColor.GREEN + "\"");
                sender.sendMessage(ChatColor.GREEN + "  /cc <player> if \"" + ChatColor.LIGHT_PURPLE + "condition" + ChatColor.GREEN + "\" do \"" + ChatColor.LIGHT_PURPLE + "command" + ChatColor.GREEN + "\"");
                sender.sendMessage(ChatColor.GRAY + "e.g.");
                sender.sendMessage(ChatColor.GREEN + "  /cc konsolas unless -ping->100|-tps-<10.0 do kick konsolas");
                sender.sendMessage(ChatColor.GRAY + "Please note that conditions cannot include any spaces.");
                sender.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            } else {
                sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] > Incorrect subcommand.");
                sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] >" + ChatColor.GREEN + "   /cc help");
            }
            return false;
        }

        // Sub command
        String action = args[1];

        // Get the player
        Player placeholderFor = Bukkit.getPlayer(args[0]);
        if (placeholderFor == null) {
            sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] > Player " + args[0] + " doesn't appear to be online...");
            sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] >" + ChatColor.GREEN + "   /cc help");
            return false;
        }

        // Get the condition
        String modifiedStr = args[2];
        for (Placeholder placeholder : Placeholder.values()) {
            if (placeholder.getPlaceholder().shouldApply(modifiedStr)) {
                try {
                    modifiedStr = placeholder.getPlaceholder().doSubstitution(modifiedStr, placeholderFor);
                } catch (AbstractPlaceholder.PlaceholderException ex) {
                    sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] > Failed to apply a placeholder: " + ex.getMessage());
                    sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] >" + ChatColor.GREEN + "   /cc help");
                    return false;
                }
            }
        }

        // Get the command
        StringBuilder command = new StringBuilder();
        for (int i = 4; i < args.length; i++) {
            command.append(args[i]).append(' ');
        }

        // Make sure there's a 'do' third.
        if (!args[3].equals("do")) {
            sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] > Missing 'do' clause. Does the condition have spaces in it?");
            sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] >" + ChatColor.GREEN + "   /cc help");
            return false;
        }

        // Parse the expression
        Expression expression;
        try {
            expression = new Expression(modifiedStr);
        } catch (Expression.ParseException ex) {
            sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] > Failed to parse expression: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] > Roughly translated, that means you spelt something wrong or made a syntax error in the condition.");
            sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] >" + ChatColor.GREEN + "   /cc help");
            return false;
        }

        sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] > Successfully parsed expression: " + expression.toString());

        switch (action) {
            case "unless":
                if (!expression.evaluate()) {
                    dispatchCommand(sender, command.toString());
                } else {
                    sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] > Not dispatching command because \"" + args[1] + "\" evaluated to true");
                }
                break;
            case "if":
                if (expression.evaluate()) {
                    dispatchCommand(sender, command.toString());
                } else {
                    sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] > Not dispatching command because \"" + args[1] + "\" evaluated to false");
                }
                break;
            default:
                sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] > Incorrect subcommand.");
                sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] >" + ChatColor.GREEN + "   /cc help");
                return false;
        }

        return true;
    }

    private void dispatchCommand(CommandSender sender, String command) {
        try {
            sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] > Dispatching command \"" + command + "\"");
            this.getServer().dispatchCommand(sender, command);
        } catch (CommandException ex) {
            sender.sendMessage(ChatColor.GOLD + "[ConditionalCommands] > An error occured whilst executing the command. The stack trace has been printed in the console.");
            this.getLogger().warning("Failed to execute command. THIS IS NOT AN ERROR WITH CONDITIONALCOMMANDS!");
            ex.printStackTrace();
        }
    }
}