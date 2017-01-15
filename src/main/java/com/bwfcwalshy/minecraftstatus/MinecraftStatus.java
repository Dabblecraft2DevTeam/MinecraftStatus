package com.bwfcwalshy.minecraftstatus;

import com.bwfcwalshy.minecraftstatus.commands.Command;
import com.bwfcwalshy.minecraftstatus.commands.InviteCommand;
import com.bwfcwalshy.minecraftstatus.commands.StatusCommand;
import com.bwfcwalshy.minecraftstatus.sql.SQLController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;

public class MinecraftStatus {

    public static final Logger LOGGER = LoggerFactory.getLogger(MinecraftStatus.class);
    private static MinecraftStatus instance;
    public static String passwd;

    private IDiscordClient client;
    private Set<Command> commands;
    private Set<IChannel> statusChannels;

    public static void main(String[] args){
        try {
            Properties properties = new Properties();

            File file = new File("bot.prop");
            if(!file.exists()){
                file.createNewFile();

                properties.setProperty("Token", "EMPTY");
                properties.setProperty("SQL-Password", "EMPTY");
                properties.store(new FileOutputStream(file), null);
            }

            properties.load(new FileInputStream(file));
            if(properties.getProperty("Token").equals("EMPTY")){
                LOGGER.error("You have not set the bot token! Bot is shutting down!");
            }else{
                passwd = properties.getProperty("SQL-Password");
                (instance = new MinecraftStatus()).init(properties.getProperty("Token"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(String token) {
        client = new ClientBuilder().withToken(token).login();
        client.getDispatcher().registerListener(new Events());

        statusChannels = new HashSet<>();
        commands = new HashSet<>();
    }

    protected void run() {
        registerCommand(new StatusCommand());
        registerCommand(new InviteCommand());

        updateStatusChannels();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Checker(), 10, 60000);

        LOGGER.info("MinecraftStatus booted");
    }

    private void registerCommand(Command command) {
        this.commands.add(command);
    }

    public static MinecraftStatus getInstance(){
        return instance;
    }

    public IDiscordClient getClient(){
        return this.client;
    }

    public Set<Command> getCommands() {
        return commands;
    }

    public Set<IChannel> getStatusChannels() {
        return statusChannels;
    }

    public void updateStatusChannels() {
        try {
            SQLController.runSqlTask((conn) -> {
                ResultSet set = conn.createStatement().executeQuery("SELECT * FROM channels");
                statusChannels.clear();
                while(set.next()) {
                    statusChannels.add(client.getGuildByID(set.getString("guild_id")).getChannelByID(set.getString("channel_id")));
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
