package com.gabrielhd.credits.Commands;

import com.gabrielhd.credits.Main;
import com.gabrielhd.credits.Managers.ConfigManager;
import com.gabrielhd.credits.Menu.Submenus.VIPMenu;
import com.gabrielhd.credits.Player.CPlayer;
import com.gabrielhd.common.TopPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CreditsCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if ((sender instanceof Player)) {
            Player player = (Player) sender;

            if (args.length <= 0 || args.length >= 4) {
                for (String string : ConfigManager.getSettings().getStringList("Messages.HelpCmd")) {
                    player.sendMessage(Main.Color(string));
                }
                if (player.hasPermission("creditsys.admin")) {
                    for (String string : ConfigManager.getSettings().getStringList("Messages.HelpAdminCmd")) {
                        player.sendMessage(Main.Color(string));
                    }
                }
                return true;
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("loja")) {
                    new VIPMenu(player).openInventory(player);
                    return true;
                }

                if (args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("balance")) {
                    CPlayer cPlayer = Main.getPlayerManager().getCPlayer(player);
                    if (cPlayer != null) {
                        player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.CreditsBalance")).replace("%credits%", String.valueOf(cPlayer.getCredits())).replace("%player%", player.getName()));
                        return true;
                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("baltop")) {
                    List<TopPlayer> datas = new ArrayList<>(Main.getTop().values());
                    datas.sort(Comparator.comparingInt(TopPlayer::getCredits));
                    Collections.reverse(datas);

                    sender.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.TopTitle")));
                    for (int i = 0; i < 10; i++) {
                        if (i >= datas.size()) {
                            break;
                        }
                        if (datas.get(i).getCredits() >= 1) {
                            sender.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.TopLineFormat")).replace("%position%", String.valueOf((i + 1))).replace("%player%", datas.get(i).getPlayer()).replace("%credits%", String.valueOf(datas.get(i).getCredits())));
                        }
                    }
                    return true;
                }
                return true;
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("giveall")) {
                    if (!player.hasPermission("creditsys.admin")) {
                        player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.NoPermissions")));
                        return true;
                    }
                    if (this.isInt(args[1])) {
                        int number = Integer.parseInt(args[1]);
                        if (number >= 1) {
                            for (Player online : Bukkit.getOnlinePlayers()) {
                                CPlayer cPlayer = Main.getPlayerManager().getCPlayer(online);
                                if (cPlayer != null) cPlayer.setCredits(cPlayer.getCredits() + number);
                                online.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.BroadcastGiveall")).replace("%amount%", String.valueOf(number)));
                            }
                        }
                        return true;
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("reset")) {
                    if (!player.hasPermission("creditsys.admin")) {
                        player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.NoPermissions")));
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        CPlayer cPlayer = Main.getPlayerManager().getCPlayer(target);
                        if (cPlayer != null) {
                            cPlayer.setCredits(0);
                            player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.ResetCredits")).replace("%player%", target.getName()));
                            target.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.ResetCredits")).replace("%player%", target.getName()));
                        }
                        return true;
                    } else {
                        player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.PlayerNotOnline")));
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("balance")) {
                    if (!player.hasPermission("creditsys.ver")) {
                        player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.NoPermissions")));
                        return true;
                    }

                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        CPlayer cPlayer = Main.getPlayerManager().getCPlayer(target);
                        if (cPlayer != null) {
                            player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.CreditsBalance")).replace("%credits%", String.valueOf(cPlayer.getCredits())).replace("%player%", target.getName()));
                        }
                        return true;
                    } else {
                        player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.PlayerNotOnline")));
                    }
                    return true;
                }
                for (String string : ConfigManager.getSettings().getStringList("Messages.HelpCmd")) {
                    player.sendMessage(Main.Color(string));
                }
                if (player.hasPermission("creditsys.admin")) {
                    for (String string : ConfigManager.getSettings().getStringList("Messages.HelpAdminCmd")) {
                        player.sendMessage(Main.Color(string));
                    }
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("give")) {
                if (!player.hasPermission("creditsys.admin")) {
                    player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.NoPermissions")));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null && this.isInt(args[2])) {
                    int number = Integer.parseInt(args[2]);
                    if (number >= 1) {
                        CPlayer cPlayer = Main.getPlayerManager().getCPlayer(target);
                        if (cPlayer != null) {
                            cPlayer.setCredits(cPlayer.getCredits() + number);
                            target.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.ReceiveCredits")).replace("%amount%", String.valueOf(number)));
                            player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.GiveCredits")).replace("%amount%", String.valueOf(number)));
                        }
                    }
                } else {
                    player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.PlayerNotOnline")));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("set")) {
                if (!player.hasPermission("creditsys.admin")) {
                    player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.NoPermissions")));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null && this.isInt(args[2])) {
                    int number = Integer.parseInt(args[2]);
                    CPlayer cPlayer = Main.getPlayerManager().getCPlayer(target);
                    if (cPlayer != null) {
                        cPlayer.setCredits(number);
                        player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.SetCredits")).replace("%amount%", String.valueOf(number)).replace("%player%", target.getName()));
                    }
                } else {
                    player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.PlayerNotOnline")));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("take")) {
                if (!player.hasPermission("creditsys.admin")) {
                    player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.NoPermissions")));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null && this.isInt(args[2])) {
                    int number = Integer.parseInt(args[2]);
                    CPlayer cPlayer = Main.getPlayerManager().getCPlayer(target);
                    if (cPlayer != null) cPlayer.setCredits(cPlayer.getCredits() - number);
                    player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.TakeCredits")).replace("%amount%", String.valueOf(number)).replace("%player%", target.getName()));
                } else {
                    player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.PlayerNotOnline")));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("dar")) {
                if (ConfigManager.getSettings().getBoolean("DarCommand")) {
                    CPlayer cPlayer = Main.getPlayerManager().getCPlayer(player);
                    if (cPlayer != null && this.isInt(args[2])) {
                        Player target = Bukkit.getPlayer(args[1]);
                        int amount = Integer.parseInt(args[2]);
                        if (target != null) {
                            if (target == player) {
                                player.sendMessage(Main.Color("&cYou can't pay yourself"));
                                return true;
                            }
                            if (amount >= 1) {
                                CPlayer killerData = Main.getPlayerManager().getCPlayer(target);
                                if (killerData == null) {
                                    Main.getPlayerManager().createCPlayer(target);

                                    killerData = Main.getPlayerManager().getCPlayer(target);
                                }
                                if (cPlayer.getCredits() >= amount) {
                                    player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.SentCredits")).replace("%amount%", String.valueOf(amount)).replace("%player%", target.getName()));
                                    target.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.PayerCredits")).replace("%amount%", String.valueOf(amount)).replace("%player%", player.getName()));
                                    killerData.setCredits(killerData.getCredits() + amount);
                                    cPlayer.setCredits(cPlayer.getCredits() - amount);
                                } else {
                                    player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.InsufficientPoints")));
                                }
                            }
                        } else {
                            player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.PlayerNotOnline")));
                        }
                        return true;
                    }
                    return true;
                } else {
                    player.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.DarCommandDisable")));
                }
                return true;
            }
            for (String string : ConfigManager.getSettings().getStringList("Messages.HelpCmd")) {
                player.sendMessage(Main.Color(string));
            }
            if (player.hasPermission("creditsys.admin")) {
                for (String string : ConfigManager.getSettings().getStringList("Messages.HelpAdminCmd")) {
                    player.sendMessage(Main.Color(string));
                }
            }
            return true;
        } else {
            if (args.length <= 0 || args.length >= 4) {
                for (String string : ConfigManager.getSettings().getStringList("Messages.HelpAdminCmd")) {
                    sender.sendMessage(Main.Color(string));
                }
                return true;
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("giveall")) {
                    if (this.isInt(args[1])) {
                        int number = Integer.parseInt(args[1]);
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            CPlayer cPlayer = Main.getPlayerManager().getCPlayer(online);
                            if (cPlayer != null) cPlayer.setCredits(cPlayer.getCredits() + number);
                            online.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.BroadcastGiveall")).replace("%amount%", String.valueOf(number)));
                        }
                        return true;
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("reset")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        CPlayer cPlayer = Main.getPlayerManager().getCPlayer(target);
                        if (cPlayer != null) {
                            cPlayer.setCredits(0);
                            sender.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.ResetCredits")).replace("%player%", target.getName()));
                            target.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.ResetCredits")).replace("%player%", target.getName()));
                        }
                        return true;
                    } else {
                        sender.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.PlayerNotOnline")));
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("balance")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        CPlayer cPlayer = Main.getPlayerManager().getCPlayer(target);
                        if (cPlayer != null) {
                            sender.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.CreditsBalance")).replace("%credits%", String.valueOf(cPlayer.getCredits())).replace("%player%", target.getName()));
                        }
                        return true;
                    } else {
                        sender.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.PlayerNotOnline")));
                    }
                    return true;
                }
                for (String string : ConfigManager.getSettings().getStringList("Messages.HelpAdminCmd")) {
                    sender.sendMessage(Main.Color(string));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("give")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null && this.isInt(args[2])) {
                    int number = Integer.parseInt(args[2]);
                    CPlayer cPlayer = Main.getPlayerManager().getCPlayer(target);
                    if (cPlayer != null) {
                        cPlayer.setCredits(cPlayer.getCredits() + number);
                        target.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.ReceiveCredits")).replace("%amount%", String.valueOf(number)));
                        sender.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.GiveCredits")).replace("%amount%", String.valueOf(number)));
                    }
                } else {
                    sender.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.PlayerNotOnline")));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("set")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null && this.isInt(args[2])) {
                    int number = Integer.parseInt(args[2]);
                    CPlayer cPlayer = Main.getPlayerManager().getCPlayer(target);
                    if (cPlayer != null) {
                        cPlayer.setCredits(number);
                        sender.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.SetCredits")).replace("%amount%", String.valueOf(number)).replace("%player%", target.getName()));
                    }
                } else {
                    sender.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.PlayerNotOnline")));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("take")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null && this.isInt(args[2])) {
                    int number = Integer.parseInt(args[2]);
                    CPlayer cPlayer = Main.getPlayerManager().getCPlayer(target);
                    if (cPlayer != null) cPlayer.setCredits(cPlayer.getCredits() - number);
                    sender.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.TakeCredits")).replace("%amount%", String.valueOf(number)).replace("%player%", target.getName()));
                } else {
                    sender.sendMessage(Main.Color(ConfigManager.getSettings().getString("Messages.PlayerNotOnline")));
                }
                return true;
            }
            for (String string : ConfigManager.getSettings().getStringList("Messages.HelpAdminCmd")) {
                sender.sendMessage(Main.Color(string));
            }
        }
        return false;
    }

    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
