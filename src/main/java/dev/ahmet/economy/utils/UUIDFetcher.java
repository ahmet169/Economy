package dev.ahmet.economy.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;


public class UUIDFetcher {

    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
    private static final String NAME_URL = "https://api.mojang.com/user/profile/%s";
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
    private String name;
    private UUID id;

    private static final HashMap<String, UUID> uuidCache = new HashMap<>();
    private static final HashMap<UUID, String> nameCache = new HashMap<>();

    public static UUID getUUID(String name) {
        if(uuidCache.containsKey(name)) {
            return uuidCache.get(name);
        }
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(String.format(UUID_URL, name, System.currentTimeMillis() / 1000)))
                    .timeout(Duration.ofMillis(5000))
                    .GET()
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(httpResponse.body());
            UUIDFetcher data = gson.fromJson(httpResponse.body(), UUIDFetcher.class);
            System.out.println(data.toString()); //Mach mal debugging
            uuidCache.put(name, data.id);
            nameCache.put(data.id, name);
            return data.id;
        } catch (IOException | InterruptedException ignored) {
            return UUID.randomUUID();
        }
    }

    public static String getName(UUID uuid) {
        if(nameCache.containsKey(uuid)) {
            return nameCache.get(uuid);
        }
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(String.format(NAME_URL, UUIDTypeAdapter.fromUUID(uuid))))
                    .timeout(Duration.ofMillis(5000))
                    .GET()
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            UUIDFetcher currentNameData = gson.fromJson(httpResponse.body(), UUIDFetcher.class);

            uuidCache.put(currentNameData.name.toLowerCase(), uuid);
            nameCache.put(uuid, currentNameData.name);

            return currentNameData.name;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
