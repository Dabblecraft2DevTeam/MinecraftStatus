package com.bwfcwalshy.minecraftstatus.commands;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public interface Command {

    void onCommand(IUser sender, IChannel channel, IMessage message, String[] args);

    String getCommand();

    String getDescription();

}
