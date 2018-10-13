package co.aeria.quicksellwand.cmd;

import co.aeria.quicksellwand.Perms;
import co.aeria.quicksellwand.QuickSellWandPlugin;
import co.aeria.quicksellwand.config.MessageSender;
import co.aeria.quicksellwand.config.Messages;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class QuickSellWandCommand implements TabExecutor {

    private final QuickSellWandPlugin plugin;
    private final MessageSender msg;

    public QuickSellWandCommand(QuickSellWandPlugin plugin) {
        this.plugin = plugin;
        this.msg = plugin.getMessageSender();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if ("give".equalsIgnoreCase(args[0])) {
                if (!sender.hasPermission(Perms.COMMAND_GIVE)) {
                    msg.send(sender, Messages.NO_PERMISSION);
                    return true;
                }
                if (args.length > 1) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player != null) {
                        int usage = -1;
                        if (args.length > 2) {
                            Optional<Integer> usageOpt = asInteger(args[2]);
                            if (usageOpt.isPresent()) {
                                usage = usageOpt.get();
                            }
                        }
                        ItemStack item = plugin.getWandService().getNewWand(usage);
                        HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(item);
                        if (leftover.size() == 0) {
                            msg.send(sender, Messages.WAND_ADDED);
                        } else {
                            msg.send(sender, Messages.INVENTORY_FULL);
                        }
                        return true;
                    } else {
                        msg.send(sender, Messages.PLAYER_NOT_FOUND);
                        return true;
                    }
                }
            }
            if ("reload".equalsIgnoreCase(args[0])) {
                if (!sender.hasPermission(Perms.COMMAND_RELOAD)) {
                    msg.send(sender, Messages.NO_PERMISSION);
                    return true;
                }
                plugin.reload();
                msg.send(sender, Messages.CONFIG_RELOADED);
                return true;
            }
        }
        if (!sender.hasPermission(Perms.USE_COMMAND)) {
            msg.send(sender, Messages.NO_PERMISSION);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String[] args1 = new String[]{"give", "reload"};
            List<String> result = new ArrayList<>();
            for (String s : args1) {
                if (s.startsWith(args[0])) {
                    result.add(s);
                }
            }
            return result;
        }
        if (args.length == 2 && "give".equals(args[0])) {
            List<String> result = new ArrayList<>();
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                result.add(player.getName());
            }
            return result;
        }
        return null;
    }

    private static Optional<Integer> asInteger(String str) {
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

}
