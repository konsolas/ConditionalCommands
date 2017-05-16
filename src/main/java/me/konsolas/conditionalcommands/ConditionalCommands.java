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
        getLogger().info("Initializing placeholders...");
        for (Placeholders placeholder : Placeholders.values()) {
            placeholder.getPlaceholder().init(this);
        }
        getLogger().info("Ready.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        boolean player = (sender instanceof Player);

        if (args.length == 0 || (args.length > 1 && args.length < 5)) {
            sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] > Incorrect number of arguments.");
            sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] >" + (player ? ChatColor.GREEN : "") + "   /cc help");
            return false;
        }



        if (args.length == 1) {
            if (args[0].equals("help")) {
                sender.sendMessage((player ? ChatColor.GOLD : "") + "--------=ConditionalCommands=--------");
                sender.sendMessage((player ? ChatColor.GREEN : "") + "  /cc help");
                sender.sendMessage((player ? ChatColor.GREEN : "") + "  /cc <player> unless \"" + (player ? ChatColor.LIGHT_PURPLE : "") + "condition"
                        + (player ? ChatColor.GREEN : "") + "\" do \"" + (player ? ChatColor.LIGHT_PURPLE : "") + "command" + (player ? ChatColor.GREEN : "") + "\"");
                sender.sendMessage((player ? ChatColor.GREEN : "") + "  /cc <player> if \"" + (player ? ChatColor.LIGHT_PURPLE : "") + "condition"
                        + (player ? ChatColor.GREEN : "") + "\" do \"" + (player ? ChatColor.LIGHT_PURPLE : "") + "command" + (player ? ChatColor.GREEN : "") + "\"");
                sender.sendMessage((player ? ChatColor.GRAY : "") + "e.g.");
                sender.sendMessage((player ? ChatColor.GREEN : "") + "  /cc konsolas unless -ping->100|-tps-<10.0 do kick konsolas");
                sender.sendMessage((player ? ChatColor.GRAY : "") + "Please note that conditions cannot include any spaces.");
                sender.sendMessage((player ? ChatColor.GOLD : "") + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            } else {
                sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] > Incorrect subcommand.");
                sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] >" + (player ? ChatColor.GREEN : "") + "   /cc help");
            }
            return false;
        }

        // Sub command
        String action = args[1];

        // Get the player
        Player placeholderFor = Bukkit.getPlayer(args[0]);
        if (placeholderFor == null) {
            sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] > Not dispatching command because " + args[0] + " is not online...");
            return true;
        }

        // Get the condition
        String modifiedStr = args[2];
        for (Placeholders placeholder : Placeholders.values()) {
            if (placeholder.getPlaceholder().shouldApply(modifiedStr)) {
                try {
                    modifiedStr = placeholder.getPlaceholder().doSubstitution(modifiedStr, placeholderFor);
                } catch (AbstractPlaceholder.PlaceholderException ex) {
                    sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] > Failed to apply a placeholder: " + ex.getMessage());
                    sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] >" + (player ? ChatColor.GREEN : "") + "   /cc help");
                    getLogger().severe("An error occurred whilst applying a placeholder.");
                    ex.printStackTrace();
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
            sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] > Missing 'do' clause. Make sure the condition has no spaces.");
            sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] >" + (player ? ChatColor.GREEN : "") + "   /cc help");
            return false;
        }

        // Parse the expression
        Expression expression;
        try {
            expression = new Expression(modifiedStr);
        } catch (Expression.ParseException ex) {
            sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] > Failed to parse expression: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] > Roughly translated, that means you spelt something wrong or made a syntax error in the condition.");
            sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] >" + (player ? ChatColor.GREEN : "") + "   /cc help");
            return false;
        }

        getLogger().info("Successfully parsed expression: " + expression.toString());

        switch (action) {
            case "unless":
                if (!expression.evaluate()) {
                    dispatchCommand(sender, command.toString());
                } else {
                    sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] > Not dispatching command because \"" + args[2] + "\" evaluated to true");
                }
                break;
            case "if":
                if (expression.evaluate()) {
                    dispatchCommand(sender, command.toString());
                } else {
                    sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] > Not dispatching command because \"" + args[2] + "\" evaluated to false");
                }
                break;
            default:
                sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] > Incorrect subcommand.");
                sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] >" + (player ? ChatColor.GREEN : "") + "   /cc help");
                return false;
        }

        return true;
    }

    private void dispatchCommand(CommandSender sender, String command) {
        boolean player = (sender instanceof Player);
        try {
            sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] > Dispatching command \"" + command + "\"");
            this.getServer().dispatchCommand(sender, command);
        } catch (CommandException ex) {
            sender.sendMessage((player ? ChatColor.GOLD : "") + "[ConditionalCommands] > An error occured whilst executing the command. The stack trace has been printed to the console.");
            this.getLogger().warning("Failed to execute command. THIS IS NOT AN ERROR WITH CONDITIONALCOMMANDS!");
            ex.printStackTrace();
        }
    }
}