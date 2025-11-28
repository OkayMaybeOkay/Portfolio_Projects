//Spotify API

package com.example;
import java.io.Console;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// This class requires the Jackson Databind library
// (e.g., com.fasterxml.jackson.core:jackson-databind)
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SpotifyApiClient {

    // Create reusable components...
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available. Please run this from a terminal.");
            System.exit(1);
        }

        try {
            // Read credentials securely. readPassword returns char[].
            
            String clientId = new String(console.readPassword("What is your Spotify Client Id: "));
            String clientSecret = new String(console.readPassword("What is your Spotify Client Secret: "));

            String accessToken = getAccessToken(clientId, clientSecret);

            if (accessToken != null) {
                // Replaced sampleCall with the merged ArtistData functionality
                ArtistData.getArtistData(accessToken);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getAccessToken(String clientId, String clientSecret) throws IOException, InterruptedException {
        String authUrl = "https://accounts.spotify.com/api/token";

        // Build the form url-encoded request body
        String formData = "grant_type=client_credentials" +
                "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(authUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        // Send the request and get the response as a String
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.err.println("Failed to get access token. Status: " + response.statusCode());
            System.err.println(response.body());
            return null;
        }

        // Parse the JSON response body
        @SuppressWarnings("unchecked")
        Map<String, Object> jsonMap = objectMapper.readValue(response.body(), Map.class);
        String accessToken = (String) jsonMap.get("access_token");
        // Use Number to handle potential Integer or Long
        Number expiresIn = (Number) jsonMap.get("expires_in"); 

        if (accessToken != null && expiresIn != null) {
            long hours = expiresIn.longValue() / 3600; // 3600 = 60 * 60
            System.out.printf("Access Token Generated! It will expire in %d hour(s)%n", hours);
            return accessToken;
        } else {
            System.err.println("Could not parse 'access_token' or 'expires_in' from response.");
            return null;
        }
    }

}
