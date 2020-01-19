package com.ahl.server.bulkupload;

import com.ahl.server.entity.Team;
import com.ahl.server.entity.Tournament;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.bson.types.ObjectId;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BulkUpload {

    private static Map<Integer, ObjectId> teamMap = new HashMap<>();

    private static Gson gson = new Gson();

    private static String season = "2020";

    private static String csvFileName = "playerdetail.csv";

    private static final String GET_CURRENT_TOURNAMENT = "http://localhost:8080/api/tournament?season="+season+"&type=AHL";

    private static String GET_CURRENT_TEAMS = "http://localhost:8080/api/teams?tournamentId=";

    private static final String CREATE_PLAYER = "http://localhost:8080/api/bulk/player";

    private String UPDATE_PLAYER = "http://localhost:8080/api/player/";

    private static final String CREATE_RELATION = "http://localhost:8080/api/player-relation";

    private static String home = System.getProperty("user.home");

    private static StorageClient storageClient;


    public static void main(String[] args) throws Exception {

        initStore();

        BulkUpload bulkUpload = new BulkUpload();

        Tournament tournament = gson.fromJson(bulkUpload.doGET(GET_CURRENT_TOURNAMENT), Tournament.class);

        GET_CURRENT_TEAMS+=tournament.getId();

        Type founderListType = new TypeToken<ArrayList<Team>>(){}.getType();
        ArrayList<Team> teamList = gson.fromJson(bulkUpload.doGET(GET_CURRENT_TEAMS), founderListType);

        for(Team team : teamList){
            teamMap.put(team.getTeamTag().getTeamTagId(), team.getId());
        }

        System.out.println(teamList.toString());

        File csvFile = new File(home + File.separator + "Desktop" + File.separator + csvFileName);
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        br = new BufferedReader(new FileReader(csvFile));
        line = br.readLine();
        while (line != null) {

            line = line.trim();
            String[] playerInfo = line.split(cvsSplitBy);

            JsonObject playerJson = new JsonObject();
            playerJson.addProperty("name",playerInfo[0]);
            playerJson.addProperty("position",playerInfo[1]);

            String playerId = bulkUpload.createPlayer(playerJson);
            String imageLink = bulkUpload.uploadImage(playerInfo[3], playerId);

            playerJson.addProperty("profile",imageLink);
            bulkUpload.updatePlayer(playerJson, playerId);

            JsonObject relJson = new JsonObject();
            relJson.addProperty("teamId",teamMap.get(Integer.parseInt(playerInfo[2])).toHexString());
            relJson.addProperty("playerId",playerId);
            bulkUpload.createRelation(relJson);

            line = br.readLine();
        }

    }

    private String createPlayer(JsonObject playerJson) throws Exception{
        return doPOST(CREATE_PLAYER, playerJson.toString());
    }

    private void updatePlayer(JsonObject player, String playerId) throws Exception{
        doPUT(UPDATE_PLAYER+playerId, player.toString());
    }

    private void createRelation(JsonObject relationJson) throws Exception {
        doPOST(CREATE_RELATION, relationJson.toString());
    }

    private  static void initStore() throws Exception{

        FileInputStream firebaseToken = new FileInputStream("src/main/firebase.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(firebaseToken))
                .setStorageBucket("dileepkosur3524.appspot.com")
                .build();
        FirebaseApp fireApp = FirebaseApp.initializeApp(options);
        storageClient = StorageClient.getInstance(fireApp);

    }

    private String uploadImage(String fileName, String playerId) throws Exception {

        File file = new File(home + File.separator + "Desktop" + File.separator + "profile" + File.separator + fileName);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);

        String blobString = "Player/" +playerId + "-" + fileName ;
        Blob blob = storageClient.bucket("dileepkosur3524.appspot.com")
                .create(blobString, inputStream, Bucket.BlobWriteOption.userProject("dileepkosur3524"));
        blob.getStorage().createAcl(blob.getBlobId(), Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        inputStream.close();

        return blob.getMediaLink();
    }

    private String doGET(String uri) throws Exception {
        URL obj = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode + " for URI :: "+uri);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getString(con.getInputStream());
        }else {
            throw new Exception(getString(con.getErrorStream()));
        }
    }

    private String doPOST(String uri, String payLoad) throws Exception {
        URL obj = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(payLoad.getBytes());
        os.flush();
        os.close();

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode + " for URI :: "+uri);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getString(con.getInputStream());
        }else {
            throw new Exception(getString(con.getErrorStream()));
        }
    }

    private String doPUT(String uri, String payLoad) throws Exception {
        URL obj = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");

        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(payLoad.getBytes());
        os.flush();
        os.close();

        int responseCode = con.getResponseCode();
        System.out.println("PUT Response Code :: " + responseCode + " for URI :: "+uri);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getString(con.getInputStream());
        } else {
            throw new Exception(getString(con.getErrorStream()));
        }
    }

    private String getString(InputStream inputStream) throws Exception{

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
