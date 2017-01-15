package com.bwfcwalshy.minecraftstatus;

public enum Service {

    MINECRAFT_NET("Minecraft.net", "minecraft.net"),
    SESSIONS("Mojang Sessions", "session.minecraft.net"),
    ACCOUNT("Mojang Accounts", "account.mojang.com"),
    AUTH("Mojang Authentication", "auth.mojang.com"),
    SKINS("Skins", "skins.minecraft.net"),
    AUTH_SERVER("Authentication Server", "authserver.mojang.com"),
    SESSION_SERVER("Session Server", "sessionserver.mojang.com"),
    API("API", "api.mojang.com"),
    TEXTURES("Textures", "textures.minecraft.net"),
    MOJANG_COM("Mojang.com", "mojang.com");

    public static Service[] values = values();

    private String name;
    private String url;
    Service(String prettyName, String url){
        this.name = prettyName;
        this.url = url;
    }

    @Override
    public String toString(){
        return name + " (" + url + ")";
    }

    public String getName(){
        return this.name;
    }

    public String getUrl(){
        return this.url;
    }

    public static Service getService(String serviceUrl) {
        for(Service service : values){
            if(service.getUrl().equalsIgnoreCase(serviceUrl)){
                return service;
            }
        }
        return null;
    }

    public static Service getServiceByName(String serviceName) {
        for(Service service : values){
            if(service.getName().equalsIgnoreCase(serviceName))
                return service;
        }
        return null;
    }
}
