package com.bwfcwalshy.minecraftstatus.commands;

import com.bwfcwalshy.minecraftstatus.MessageUtils;
import com.bwfcwalshy.minecraftstatus.MinecraftStatus;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.BotInviteBuilder;

import java.util.EnumSet;

public class InviteCommand implements Command {

    @Override
    public void onCommand(IUser sender, IChannel channel, IMessage message, String[] args) {
        MessageUtils.sendMessage("You can invite me to your guild using this link! "
                + new BotInviteBuilder(MinecraftStatus.getInstance().getClient()).withPermissions(EnumSet.of(Permissions.SEND_MESSAGES)).build(), channel);
    }

    @Override
    public String getCommand() {
        return "invite";
    }

    @Override
    public String getDescription() {
        return "Invite me to your server!";
    }
}
