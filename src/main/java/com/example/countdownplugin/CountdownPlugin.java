package com.example.countdownplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.entity.Firework;
import java.util.Random;
import org.bukkit.Color;

public class CountdownPlugin extends JavaPlugin {
    private boolean countdownActive = false;
    private boolean fireworksActive = false;

    @Override
    public void onEnable() {
        getLogger().info("CountdownPlugin has been enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("CountdownPlugin has been disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("countdown")) {
            if (sender.hasPermission("countdownplugin.countdown")) {
                startCountdown();
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }
        } else if (command.getName().equalsIgnoreCase("stopfirework")) {
            if (sender.hasPermission("countdownplugin.stopfirework")) {
                stopFireworks();
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "你没有权限使用该命令！");
                return true;
            }
        }
        return false;
    }

    private void startCountdown() {
        if (countdownActive) {
            return;
        }
        countdownActive = true;
        new BukkitRunnable() {
            int timeLeft = 60;

            @Override
            public void run() {
                if (timeLeft > 10) {
                    if (timeLeft % 30 == 0 || timeLeft == 15) {
                        Bukkit.broadcastMessage(ChatColor.GREEN + "距离2025年春节还有 " + timeLeft + " 秒");
                    }
                } else if (timeLeft > 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(ChatColor.RED.toString() + timeLeft, "", 10, 20, 10);
                    }
                } else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(ChatColor.RED + "春节快乐！", "", 10, 70, 20);
                    }
                    startFireworks();
                    countdownActive = false;
                    cancel();
                }
                timeLeft--;
            }
        }.runTaskTimer(this, 0, 20);
    }

    private void startFireworks() {
        fireworksActive = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!fireworksActive) {
                    cancel();
                    return;
                }
                Random random = new Random();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Location loc = player.getLocation().add(0, 15, 0); // 15 blocks above the player
                    Firework fw = loc.getWorld().spawn(loc, Firework.class);
                    FireworkMeta meta = fw.getFireworkMeta();
                    Color color = Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256)); // Random color
                    meta.addEffect(FireworkEffect.builder().withColor(color).with(Type.BALL_LARGE).trail(true).build());
                    fw.setFireworkMeta(meta);
                }
            }
        }.runTaskTimer(this, 0, 40);
    }

    private void stopFireworks() {
        fireworksActive = false;
    }
}
