package com.bwfcwalshy.minecraftstatus.commands;

import com.bwfcwalshy.minecraftstatus.Checker;
import com.bwfcwalshy.minecraftstatus.MessageUtils;
import com.bwfcwalshy.minecraftstatus.MinecraftStatus;
import com.bwfcwalshy.minecraftstatus.Service;
import com.bwfcwalshy.minecraftstatus.sql.SQLController;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentMap;

public class StatusCommand implements Command {

    private ConcurrentMap map = Checker.getServiceStatus();

    @Override
    public void onCommand(IUser sender, IChannel channel, IMessage message, String[] args) {
        if(args.length == 0){
            // Java does not have a primitive with 3 states... So an int will do. *oops* #LazyDev
            int state = 0;
            EmbedBuilder builder = new EmbedBuilder();
            for(Service service : Service.values){
                String status;
                if(map.containsKey(service)){
                    int time = (int) map.get(service);
                    if(time == -1){
                        status = "Issues :warning:";
                        state = 1;
                    }else{
                        status = "Offline for " + time + " minute" + (time > 1 ? "s" : "") + " :x:";
                        state = 2;
                    }
                }else{
                    status = "Online :white_check_mark:";
                }
                builder.appendField(service.toString(), status, false);
            }
            MessageUtils.sendMessage(builder.withColor((state == 0 ? Color.green : state == 1 ? Color.orange : Color.red)), channel);
        }else{
            if(args.length == 1) {
                if (sender.getPermissionsForGuild(channel.getGuild()).contains(Permissions.ADMINISTRATOR)) {
                    if (args[0].equalsIgnoreCase("here")) {
                        MessageUtils.sendMessage(new EmbedBuilder().withTitle("Changed Channels").withDesc("Set Minecraft Status alerts to appear in this channel!")
                                .withColor(Color.green), channel);
                        try {
                            SQLController.runSqlTask((conn) -> {
                                PreparedStatement statement = conn.prepareStatement("UPDATE channels SET channel_id = ? WHERE guild_id = ?");
                                statement.setString(1, channel.getID());
                                statement.setString(2, channel.getGuild().getID());
                                if (statement.executeUpdate() == 0) {
                                    statement = conn.prepareStatement("INSERT INTO channels (channel_id, guild_id) VALUES (?, ?)");
                                    statement.setString(1, channel.getID());
                                    statement.setString(2, channel.getGuild().getID());
                                }
                            });
                            MinecraftStatus.getInstance().updateStatusChannels();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return;
                    } else if (args[0].equalsIgnoreCase("off")) {
                        MessageUtils.sendMessage(new EmbedBuilder().withTitle("Changed Channels").withDesc("Minecraft Status alerts will no longer appear for this guild!")
                                .withColor(Color.red), channel);
                        try {
                            SQLController.runSqlTask((conn) -> {
                                PreparedStatement statement = conn.prepareStatement("DELETE FROM channels WHERE guild_id = ?");
                                statement.setString(1, channel.getGuild().getID());
                                statement.execute();
                            });
                            MinecraftStatus.getInstance().updateStatusChannels();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            }
            String serviceName = "";
            for(String arg : args){
                serviceName += arg + " ";
            }
            serviceName = serviceName.trim();

            if(Service.getServiceByName(serviceName) != null){
                Service service = Service.getServiceByName(serviceName);
                int state = 0;
                String status;
                if(map.containsKey(service)){
                    if((int) map.get(service) == -1){
                        status = "Issues :warning:";
                        state = 1;
                    }else{
                        status = "Offline for " + map.get(service) + " minute" + ((int) map.get(service) == 1 ? "s" : "") + " :x:";
                        state = 2;
                    }
                }else{
                    status = "Online :white_check_mark:";
                }
                MessageUtils.sendMessage(new EmbedBuilder().appendField(service.toString(), status, false).withColor((state == 0 ? Color.green : state == 1 ? Color.orange : Color.red))
                        , channel);
            }
        }
    }

    @Override
    public String getCommand() {
        return "status";
    }

    @Override
    public String getDescription() {
        return "Check the status of all Minecraft Services";
    }
}
