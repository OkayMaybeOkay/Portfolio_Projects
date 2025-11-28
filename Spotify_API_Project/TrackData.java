package com.example;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

// This import is used to count the frequency of
// elements inside an ArrayList!
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;

// Class to get track info.
public class TrackData {
    
    // Properties to hold track info.
    private List<String> topTrackIds;
    private int explicitCount;
    private double lengthTrackAvg_ms;

    // Methods to get the properties
    public List<String> topTrackIds() {return topTrackIds;}
    public int explicitCount() {return explicitCount;}
    public double lengthTrackAvg_ms() {return lengthTrackAvg_ms;}

    // Constructor for the artist id, which then uses that to
    // create the other properties.
    public TrackData(String inArt, String accessToken) {

        try {

            // Once you get the token, use it to initialize the rest of the properties
            if (accessToken != null) {

                // Get the object of the top 10 tracks for an artist.
                Map<String, Object> results = searchSpotifyTracks(inArt, accessToken);

                // Use methods below to initialize all the properties.
                List<String> tempTrackIdList = getTrackIds(results);
                this.topTrackIds = tempTrackIdList;
                this.explicitCount = getExplicitCount(tempTrackIdList, accessToken);
                this.lengthTrackAvg_ms = getAvgLength_ms(tempTrackIdList, accessToken);

            } else {

                System.out.println("\nAccessToken could not be parsed, please try again.");

            }

        } catch (IOException | InterruptedException e) {
            System.err.println("\nAn operational error occurred: " + e.getMessage());
            e.printStackTrace();
        }

    }

    // HttpClient Class to do our requests.
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // To turn our JSON's in to Maps.
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Our main class to execute ArtistData and TrackData to solve our problem.
    
    /* public static void main(String[] args) {


        TrackData Maria = new TrackData("2sSGPbdZJkaSE2AbcGOACx");
        System.out.println(Maria.artistId());
        System.out.println(Maria.topTrackIds());
        System.out.println(Maria.explicitCount());
        System.out.println(Maria.lengthTrackAvg_ms());

    } */


    // Gets the access token.
    /* private static String getAccessToken(String clientId, String clientSecret) throws IOException, InterruptedException {
        // Base URL.
        String authUrl = "https://accounts.spotify.com/api/token";

        // Request body URL.
        String formData = "grant_type=client_credentials";

        // Make the URL request.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(authUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                // Authentication via basic authorization header (Client Credentials Flow)
                .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString(
                        (clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8)))
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        // Send the request, get the response.
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Checks if it failed. If not, send the token back if the expires number exists.
        if (response.statusCode() != 200) {
            System.err.println("\nFailed to get access token. Status: " + response.statusCode());
            System.err.println("\nResponse Body: " + response.body());
            return null;
        } else {

            @SuppressWarnings("unchecked")
            Map<String, Object> jsonMap = objectMapper.readValue(response.body(), Map.class);
            String accessToken = (String) jsonMap.get("access_token");
            Number expiresIn = (Number) jsonMap.get("expires_in");

            if (accessToken != null && expiresIn != null) {

                return accessToken;
                
            } else {

            System.err.println("\nCould not parse 'access_token' or 'expires_in' from authentication response.");
            return null;

            }

        } 
        
    } */

    // Gives the top 10 tracks as a map, we're looking for the id's of those top 10 songs.
    public static Map<String, Object> searchSpotifyTracks(String artistId, String accessToken) throws IOException, InterruptedException {
        
        // Base URL endpoint, needs the artist id.
        String base = "https://api.spotify.com/v1/artists/";

        // URL that has the artist id and the top tracks attacked.
        String encodedTopTracks = URLEncoder.encode(artistId, StandardCharsets.UTF_8) + "/top-tracks";

        // Our URI, created with the Base and the top tracks.
        URI uri = URI.create(base + encodedTopTracks);

        // Make a request.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                // MANDATORY: Include the Authorization header with the Bearer token
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        // Send the request, get a response.
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Handles non-200 codes
        if (response.statusCode() != 200) {
            System.err.printf("\nSpotify Search failed with status code: %d%n", response.statusCode());
            System.err.println("Response Body: " + response.body());
            return null;
        }

        // Parses the JSONObject, now we can use the data for another method.
        @SuppressWarnings("unchecked")
        Map<String, Object> jsonMap = objectMapper.readValue(response.body(), Map.class);
        
        return jsonMap;
    }

    // This method returns a list of the top 10 tracks of an artist by
    // taking in a JSON.
    public static List<String> getTrackIds(Map<String, Object> jsonMap) {

        // Turn the JSON into an object
        Object trackObj = jsonMap.get("tracks");

        // Check if the object is a list.
        if (trackObj instanceof List<?>) {

            // Turn it into a list of maps.
            List<Map<String, Object>> tracks = (List<Map<String, Object>>) trackObj;
            
            // Empty list to add ids.
            List<String> trackIds = new ArrayList<>();

            // Get each map's key (id) and take get the value, which is the id of the tracks.
            // Add it to the empty list.
            for (Map<String, Object> track : tracks) {

                String id = (String) track.get("id");
                trackIds.add(id);

            }

            return trackIds;


        } else {
            System.out.println("Could not find a nested 'followers' object.");
            return null;
        }

    }

    // Takes in the list of track Ids to get you the
    // number of explicit songs there are (to a max of 10).
    public static int getExplicitCount(List<String> trackId, String accessToken) throws IOException, InterruptedException {

        //List to hold all the true and falses of explicitness.
        List<Boolean> explicitTrack = new ArrayList<>();

        // To get 10 different JSONObjects and parse them for booleans.
        for (int i = 0; i < 10; i++) {

            // To get the features of a track.
            String endpoint = "https://api.spotify.com/v1/tracks/";

            // To specify which track
            String encodedTrackId = trackId.get(i);

            // Create a URI.
            URI uri = URI.create(endpoint + encodedTrackId);

            // Make a request.
            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                // MANDATORY: Include the Authorization header with the Bearer token
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();
        
            // The response of the request.
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
            if (response.statusCode() != 200) {
                System.err.printf("\nSpotify Search failed with status code: %d%n", response.statusCode());
                System.err.println("Response Body: " + response.body());
            }

            // Parse the JSON response body into a Map using Jackson ObjectMapper
            @SuppressWarnings("unchecked")
            Map<String, Object> jsonMap = objectMapper.readValue(response.body(), Map.class);
            
            explicitTrack.add(getTrackExplicit(jsonMap));
        }
        
        // Utilized the Collections import to count the frequency
        // of elements inside an ArrayList!
        int explicitCount = Collections.frequency(explicitTrack, true);

        return explicitCount;

    }

    // Gets the explicit value of a track.
    public static boolean getTrackExplicit(Map<String, Object> jsonMap) {

        Object explicitObj = jsonMap.get("explicit");

        if (explicitObj instanceof Boolean) {
            boolean explicit = (Boolean) explicitObj;
            //System.out.println("Successfully extracted explicit-ness: " + explicit);
            return explicit;
        } else {
            System.out.println("Could not find a String 'explicit' field.");
            return false;
        }


    }

    // Takes in the list of track Ids to get you the
    // number of average song length.
    public static double getAvgLength_ms(List<String> trackId, String accessToken) throws IOException, InterruptedException {

        List<Integer> length_ms = new ArrayList<>();
        double average = 0.0;
        double sum = 0.0;
        int i;

        for (i = 0; i < 10; i++) {

            // To get the features of a track.
            String endpoint = "https://api.spotify.com/v1/tracks/";

            // To specify which track
            String encodedTrackId = trackId.get(i);

            // Create a URI.
            URI uri = URI.create(endpoint + encodedTrackId);

            // Make a request.
            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                // MANDATORY: Include the Authorization header with the Bearer token
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();
        
            // The response of the request.
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
            if (response.statusCode() != 200) {
                System.err.printf("\nSpotify Search failed with status code: %d%n", response.statusCode());
                System.err.println("Response Body: " + response.body());
            }

            // Parse the JSON response body into a Map using Jackson ObjectMapper
            @SuppressWarnings("unchecked")
            Map<String, Object> jsonMap = objectMapper.readValue(response.body(), Map.class);
            
            length_ms.add(getTrackLength_ms(jsonMap));

        }


        for (i = 0; i < 10; i++) {

            sum += length_ms.get(i);

        }

        average = sum / length_ms.size();
        
        return average;

    }

    // Gets the length of a track.
    public static int getTrackLength_ms(Map<String, Object> jsonMap) {

        Object durationObj = jsonMap.get("duration_ms");

        if (durationObj instanceof Number) {
            int duration = ((Number) durationObj).intValue();
            //System.out.println("Successfully extracted duration_ms: " + duration);
            return duration;
        } else {
            System.out.println("Could not find a String 'duration_ms' field.");
            return 0;
        }

    }

}
