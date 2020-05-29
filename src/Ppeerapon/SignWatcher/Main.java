package Ppeerapon.SignWatcher;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {
    ArrayList<UUID> SignWatcher = new ArrayList<>();
    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "SignWatcher is enable");
        getServer().getPluginManager().registerEvents(this,this);
        getCommand("SignWatcher").setExecutor(this);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "SignWatcher is disable");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("SignWatcher")){
            if (p.hasPermission("SignWatcher.use")) {
                UUID uuid = p.getUniqueId();
                if (!SignWatcher.contains(uuid)) {
                    SignWatcher.add(uuid);
                    p.sendMessage(ChatColor.GOLD + "[SW] SignWatcher Enable");
                } else {
                    SignWatcher.remove(uuid);
                    p.sendMessage(ChatColor.GOLD + "[SW] SignWatcher Disable");
                }
            } else {
                p.sendMessage(ChatColor.RED + "You don't have permission");
            }
        }
        return false;
    }

    @EventHandler
    private void Sign(SignChangeEvent e){
        Player p = e.getPlayer();
        String location = "World: " + e.getBlock().getWorld().getName() + " X: " + e.getBlock().getX() + " Y: "+ e.getBlock().getY() + " Z: " + e.getBlock().getZ();
        for (Player onlineplayer : Bukkit.getOnlinePlayers()){
            if (SignWatcher.contains(onlineplayer.getUniqueId())){
                onlineplayer.sendMessage(ChatColor.GOLD + "[SW] " + p.getName() + " placed sign at " + location + "\n" + ChatColor.WHITE + e.getLine(0) + "\n"
                        + e.getLine(1)+ "\n" + e.getLine(2)+ "\n" + e.getLine(3));
            }
        }
    }

    @EventHandler
    private void Left(PlayerQuitEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        SignWatcher.remove(uuid);
    }
}
