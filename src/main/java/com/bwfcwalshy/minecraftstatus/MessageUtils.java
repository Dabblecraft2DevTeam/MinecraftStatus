package com.bwfcwalshy.minecraftstatus;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.*;

public class MessageUtils {

    public static IMessage sendMessage(CharSequence message, IChannel channel) {
        RequestBuffer.RequestFuture<IMessage> future = RequestBuffer.request(() -> {
            try {
                return channel.sendMessage(message.toString().substring(0, Math.min(message.length(), 1999)));
            } catch (DiscordException | MissingPermissionsException e) {
                MinecraftStatus.LOGGER.error("Something went wrong!", e);
            }
            return null;
        });
        return future.get();
    }

    public static void sendMessage(EmbedBuilder message, IChannel channel) {
        RequestBuffer.request(() -> {
            try {
                return new MessageBuilder(MinecraftStatus.getInstance().getClient()).withEmbed(message.build())
                        .withChannel(channel).withContent("\u200B").send();
            } catch (MissingPermissionsException | DiscordException e) {
                MinecraftStatus.LOGGER.error("Uh oh!", e);
            }
            return null;
        }).get();
    }

    public static void sendPM(IUser user, CharSequence message) {
        RequestBuffer.request(() -> {
            try {
                return user.getOrCreatePMChannel().sendMessage(message.toString().substring(0, Math.min(message.length(), 1999)));
            } catch (MissingPermissionsException | DiscordException e) {
                MinecraftStatus.LOGGER.error("Uh oh!", e);
            }
            return null;
        }).get();
    }

    public static void sendPM(IUser user, EmbedBuilder message) {
        RequestBuffer.request(() -> {
            try {
                return new MessageBuilder(MinecraftStatus.getInstance().getClient()).withEmbed(message.build())
                        .withChannel(user.getOrCreatePMChannel()).withContent("\u200B").send();
            } catch (MissingPermissionsException | DiscordException e) {
                MinecraftStatus.LOGGER.error("Uh oh!", e);
            }
            return null;
        }).get();
    }
}
