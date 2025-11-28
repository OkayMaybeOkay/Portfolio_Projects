package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ArtistData {

    private String name;
    private String id; //artist id
    private int followers;
    private int genreCount;
    private int popularity;
    private List<String> topTrackIds; //top track ids

    //constructor
    public ArtistData(String name, String id, int followers, int genreCount, int popularity) {
        this.name = name;
        this.id = id;
        this.followers = followers;
        this.genreCount = genreCount;
        this.popularity = popularity;
        this.topTrackIds = new ArrayList<>(); //maybe Alist? maybe hashmap? depends on u
    }

    public String getName() { return name; }
    public String getId() {return id; }
    public int getFollowers() { return followers; }
    public int getGenreCount() { return genreCount; }
    public int getPopularity() { return popularity; }
    public List<String> getTopTrackIds() {return topTrackIds; }
    
    //retrieves info of top tracks from the artists
    /* public void getTopTracks(HttpClient httpClient, String accessToken) throws IOException, InterruptedException {
        String topTracksUrl = "https://api.spotify.com/v1/artists/" + this.id + "/top-tracks?market=US";
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(topTracksUrl))
            .header("Authorization", "Bearer " + accessToken)
            .GET()
            .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode tracks = root.path("tracks");

            for (int i = 0; i < Math.min(10, tracks.size()); i++) {
                String trackId = tracks.get(i).path("id").asText();
                if (!trackId.Empty()) {
                    this.topTracksIds.add(trackId);
                }
            }
        } else {
            System.out.printf("Failed to get top tracks for '%s'. Status: %d%n", this.name, response.statusCode());
        }
    } */

    public static void getArtistData(String accessToken) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        //gets user input for artist name
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter first artist name: ");
            String artist1 = scanner.nextLine();

            System.out.print("Enter second artist name: ");
            String artist2 = scanner.nextLine();

            //gets info from 2 artists
            ArtistData data1 = fetchArtistInfo(httpClient, objectMapper, accessToken, artist1);
            ArtistData data2 = fetchArtistInfo(httpClient, objectMapper, accessToken, artist2);
            TrackData data3 = new TrackData(data1.getId(), accessToken);
            TrackData data4 = new TrackData(data2.getId(), accessToken);


            //results
            System.out.println("\nArtist vs Artist results: \n");
            printArtistDetails(data1, accessToken);
            printArtistDetails(data2, accessToken);

            compareArtists(data1, data2, data3, data4);

            //gives info to tracks class
            //TrackInformation rankTracks = new TrackInformation(httpClient, accessToken);
            //rankTracks.rankByDanceabilityAndEnergy(data1);
            //rankTracks.rankByDanceabilityAndEnergy(data2);
        }  
    }

    private static ArtistData fetchArtistInfo(HttpClient httpClient, ObjectMapper objectMapper,
                                              String accessToken, String artistName)
            throws IOException, InterruptedException {

        String encodedName = URLEncoder.encode(artistName, StandardCharsets.UTF_8);
        String searchUrl = "https://api.spotify.com/v1/search?q=" + encodedName + "&type=artist&limit=1";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(searchUrl))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode artistNode = root.path("artists").path("items").get(0);

            if (artistNode == null) {
                System.out.println("No artist found for " + artistName);
                return null;
            }

            String name = artistNode.path("name").asText();
            String id = artistNode.path("id").asText(); // getting your aritst id
            int followers = artistNode.path("followers").path("total").asInt();
            int popularity = artistNode.path("popularity").asInt();
            int genreCount = artistNode.path("genres").size();

            return new ArtistData(name, id, followers, genreCount, popularity);
        } else {
            System.out.printf("Failed to fetch artist '%s'. Status: %d%n", artistName, response.statusCode());
            System.out.println(response.body());
            return null;
        }
    }

    private static void printArtistDetails(ArtistData data, String accessToken) {
        if (data == null) return;
        TrackData data2 = new TrackData(data.getId(), accessToken);
        System.out.println("Artist: " + data.getName());
        System.out.println("Followers: " + data.getFollowers());
        System.out.println("Genre Diversity: " + data.getGenreCount());
        System.out.println("Popularity Score: " + data.getPopularity());
        System.out.println("Explicit Counter: " + data2.explicitCount());
        System.out.println("Length Track Average: " + data2.lengthTrackAvg_ms() / 1000);
        System.out.println();

    }

    private static void compareArtists(ArtistData a1, ArtistData a2, TrackData b1, TrackData b2) {

        int score1 = 0, score2 = 0;

         // Followers
        if (a1.getFollowers() > a2.getFollowers()) score1++;
        else if (a2.getFollowers() > a1.getFollowers()) score2++;

        // Genre Diversity
        if (a1.getGenreCount() > a2.getGenreCount()) score1++;
        else if (a2.getGenreCount() > a1.getGenreCount()) score2++;

        // Popularity Score
        if (a1.getPopularity() > a2.getPopularity()) score1++;
        else if (a2.getPopularity() > a1.getPopularity()) score2++;

        // Explicit Count
        if (b1.explicitCount() > b2.explicitCount()) score1++;
        else if (b2.explicitCount() > b1.explicitCount()) score2++;

            // Average Length
        if (b1.lengthTrackAvg_ms() > b2.lengthTrackAvg_ms()) score1++;
        else if (b2.lengthTrackAvg_ms() > b1.lengthTrackAvg_ms()) score2++;

            System.out.println("COMPARISON RESULTS:");
            System.out.println(a1.getName() + " score: " + score1);
            System.out.println(a2.getName() + " score: " + score2);
            if (score1 > score2) {
                System.out.println(a1.getName() + " is the better artist!");
            } else if (score2 > score1) {
                System.out.println(a2.getName() + " is the better artist!");
            } else {
                System.out.println("It's a tie!");
            }
    }

}
