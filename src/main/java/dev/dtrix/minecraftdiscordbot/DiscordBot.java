package dev.dtrix.minecraftdiscordbot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;

public class DiscordBot extends ListenerAdapter {

    public static void initBot(String token) {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(token);
        builder.addEventListener(new DiscordBot());
        try {
            Bukkit.getLogger().log(Level.INFO, "Starting bot...");
            builder.buildAsync();
        } catch (
                LoginException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        String[] args = message.split(" ");
        if(args.length == 2 && args[0].equalsIgnoreCase("!player")) {
            Bukkit.getLogger().log(Level.SEVERE, "Player info asked");
            String username = args[1];
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(username);
            eb.setColor(Color.red);
            eb.addField("Pseudo", username, false);

            if(Bukkit.getPlayer(username) == null) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(username);
                eb.addField("Argent", MCDPlugin.getEcon().format(MCDPlugin.getEcon().getBalance(player)), false);
            } else {
                eb.addField("Argent", MCDPlugin.getEcon().format(MCDPlugin.getEcon().getBalance(Bukkit.getPlayer(username))), false);
            }

            if(Bukkit.getPlayer(username) == null) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(username);
                eb.addField("Grade", MCDPlugin.getPermissions().getPrimaryGroup("", player), false);
            } else {
                eb.addField("Grade", MCDPlugin.getPermissions().getPrimaryGroup(Bukkit.getPlayer(username)), false);
            }

            eb.setImage("https://minotar.net/body/" + username +  "/100.png");
            event.getChannel().sendMessage(eb.build()).queue();
        }
    }
}
