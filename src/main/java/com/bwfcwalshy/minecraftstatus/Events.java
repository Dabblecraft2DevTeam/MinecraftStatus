package com.bwfcwalshy.minecraftstatus;

import com.bwfcwalshy.minecraftstatus.commands.Command;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class Events {

    private MinecraftStatus minecraftStatus = MinecraftStatus.getInstance();

    @EventSubscriber
    public void onRecieve(MessageReceivedEvent e){
        if(e.getMessage().getContent().startsWith(".")){
            String message = e.getMessage().getContent();
            String command = message.substring(1);
            String[] args = new String[0];
            if(message.contains(" ")){
                command = command.substring(0, message.indexOf(" ")-1);
                args = message.substring(message.indexOf(" ")+1).split(" ");
            }

            for(Command cmd : minecraftStatus.getCommands()){
                if(cmd.getCommand().equalsIgnoreCase(command)){
                    cmd.onCommand(e.getAuthor(), e.getChannel(), e.getMessage(), args);
                }
            }
        }
    }

    @EventSubscriber
    public void onReady(ReadyEvent event){
        minecraftStatus.run();
    }
}
