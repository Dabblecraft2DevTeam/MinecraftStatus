package com.bwfcwalshy.minecraftstatus;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Checker extends TimerTask {

    private MinecraftStatus minecraftStatus = MinecraftStatus.getInstance();
    private JsonParser parser = new JsonParser();

    private static ConcurrentMap<Service, Integer> serviceStatus = new ConcurrentHashMap<>();

    @Override
    public void run() {
        MinecraftStatus.LOGGER.debug("Running check!");
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL("https://status.mojang.com/check").openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            JsonArray array = (JsonArray) parser.parse(br.readLine());

            for(JsonElement object : array){
                JsonObject obj = object.getAsJsonObject();
                String serviceUrl = obj.entrySet().iterator().next().getKey();
                Service service = Service.getService(serviceUrl);
                if(service != null){
                    String status = obj.get(serviceUrl).getAsString();
                    if(status.equalsIgnoreCase("green")) {
                        if (serviceStatus.containsKey(service)) {
                            int time = serviceStatus.get(service);
                            serviceStatus.remove(service);
                            alertChannels(service.getName() + " are back online! It was down for " + time + " minute" + (time == 1 ? "s" : "") + "!", Color.green);
                        }
                    }else if(status.equalsIgnoreCase("yellow")){
                        if(!serviceStatus.containsKey(service))
                            serviceStatus.put(service, -1);
                    }else{
                        if(serviceStatus.containsKey(service)){
                            serviceStatus.put(service, serviceStatus.get(service)+1);
                        }else{
                            serviceStatus.put(service, 0);
                            alertChannels(service.getName() + " have gone down!", Color.red);
                        }
                    }
                }else{
                    MinecraftStatus.LOGGER.error("Unknown service! " + serviceUrl);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConcurrentMap<Service, Integer> getServiceStatus(){
        return serviceStatus;
    }

    private void alertChannels(String message, Color color){
        for (IChannel channel : minecraftStatus.getStatusChannels()) {
            MessageUtils.sendMessage(new EmbedBuilder().withTitle("Status Update").withDesc(message).withColor(color), channel);
        }
    }
}
