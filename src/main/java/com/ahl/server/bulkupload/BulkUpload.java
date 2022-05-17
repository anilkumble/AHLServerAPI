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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

public class BulkUpload {

    private static Map<Integer, ObjectId> teamMap = new HashMap<>();

    private static Gson gson = new Gson();

    private static String season;

    private static String playerDetailCSV = "playerdetail.csv";

    private static String fixtureDetailCSV = "fixtures.csv";

    private static String home = System.getProperty("user.home");

    private static String hostInfo;

    private static String playerDetailPath  = home + File.separator + "Desktop" + File.separator + playerDetailCSV;

    private static String fixtureDetailPath = home + File.separator + "Desktop" + File.separator + fixtureDetailCSV;

    private static  String GET_CURRENT_TOURNAMENT = "/api/tournament?season="+season+"&type=AHL";

    private static String GET_CURRENT_TEAMS = "/api/teams?tournament=";

    private static String CREATE_PLAYER = "/api/bulk/player";

    private static String UPDATE_PLAYER = "/api/player/";

    private static String CREATE_RELATION = "/api/player-relation";

    private static String GET_PLAYER = "/api/teams?tournamentId=";

    private static String CREATE_FIXTURE = "/api/match";

    private static Scanner sc = new Scanner(System.in);

    private static StorageClient storageClient;

    private static boolean proceed = true;

    public static void main(String[] args) throws Exception {

        System.out.println("Enter Host info : ");
        hostInfo = sc.nextLine();

        System.out.println("Enter Season");
        season = sc.next();

        setApis();

        while(proceed){

            System.out.println("1 - bulk insert");
            System.out.println("2 - exit");

            int type = sc.nextInt();

            switch (type){
                case 1:
                    startBulkDataInsert();
                    break;
                case 2:
                    proceed = false;
                    break;
                default:
                    System.out.println("Invalid Data");
                    break;
            }
        }

    }

    private static void setApis() {

        if(hostInfo.contains("localhost")){
            hostInfo = "http://"+hostInfo;
        }
        else{
            hostInfo = "https://"+hostInfo;
        }
        GET_CURRENT_TOURNAMENT  =   hostInfo+"/api/tournament?season="+season+"&type=AHL";
        GET_CURRENT_TEAMS       =   hostInfo+GET_CURRENT_TEAMS;
        CREATE_PLAYER           =   hostInfo+CREATE_PLAYER;
        CREATE_FIXTURE          =   hostInfo+CREATE_FIXTURE;
        CREATE_RELATION         =   hostInfo+CREATE_RELATION;
        GET_PLAYER              =   hostInfo+GET_PLAYER;
        UPDATE_PLAYER           =   hostInfo+UPDATE_PLAYER;

    }

    private static void startBulkDataInsert() throws Exception{

        while(proceed){

            System.out.println("1 - insert player + image");
            System.out.println("2 - insert player alone");
            System.out.println("3 - image alone");
            System.out.println("4 - insert fixtures");
            System.out.println("5 - convert date time to millis");
            System.out.println("6 - exit");

            int type = sc.nextInt();

            switch (type){
                case 1:
                    loadPlayer(true);
                    proceed = false;
                    break;
                case 2:
                    loadPlayer(false);
                    proceed = false;
                    break;
                case 3:
                    loadProfile();
                    break;
                case 4:
                    loadFixtures();
                    proceed = false;
                    break;
                case 5:
                    convertToMillis();
                    proceed = false;
                    break;
                default:
                    System.out.println("Invalid Data");
                    break;
            }
        }

    }

    private static void convertToMillis() throws Exception {

        int dateIndex, timeIndex;
        System.out.println("Enter below index values \nDateIndex\nTimeIndex");
        dateIndex = sc.nextInt();
        timeIndex = sc.nextInt();

        BufferedReader br = new BufferedReader(new FileReader(new File(fixtureDetailPath)));

        String line = br.readLine();
        while (line != null) {

            line = line.trim();
            String[] fixtureData = line.split(",");
            String date = fixtureData[dateIndex];
            String time = fixtureData[timeIndex];
            String dateTime = date+" "+time;
            Date format=new SimpleDateFormat("dd.MM.yyyy HH.mm").parse(dateTime);
            System.out.println(format.getTime());
            line = br.readLine();

        }
    }

    private static void loadProfile() throws Exception{

        int profileIndex, playerIdIndex;

        System.out.println("Expected file :: " + playerDetailPath);
        BufferedReader br = new BufferedReader(new FileReader(new File(playerDetailPath)));

        System.out.println("set index for below fields\nProfile\nPlayerId");

        profileIndex    =   sc.nextInt();
        playerIdIndex   =   sc.nextInt();

        JsonObject jsonObject = new JsonObject();

        String line = "";
        String cvsSplitBy = ",";

        line = br.readLine();
        while (line != null) {
            line = line.trim();
            String[] playerData = line.split(cvsSplitBy);
            insertPlayerProfile(jsonObject, playerData[profileIndex].trim(), playerData[playerIdIndex].trim());
        }
    }

    private static void loadPlayer(boolean includeProfile) throws Exception{

        int nameIndex, positionIndex, teamIndex, profileIndex=-1;

        System.out.println("Expected file :: " + playerDetailPath);
        BufferedReader br = new BufferedReader(new FileReader(new File(playerDetailPath)));

        Tournament currentTournament = getCurrentTournament();

        setTeamMap(currentTournament.getId());

        System.out.println("set index for below fields\nName\nPosition\nTeam");
        if(includeProfile) {
            System.out.println("Profile");
        }

        nameIndex = sc.nextInt();
        positionIndex = sc.nextInt();
        teamIndex = sc.nextInt();

        if(includeProfile) {
            profileIndex = sc.nextInt();
        }

        String line = "";
        String cvsSplitBy = ",";

        line = br.readLine();
        while (line != null) {

            line = line.trim();
            String[] playerData = line.split(cvsSplitBy);

            JsonObject playerJson = new JsonObject();
            playerJson.addProperty("name",playerData[nameIndex].trim());
            playerJson.addProperty("position",playerData[positionIndex].trim());

            String playerId = createPlayer(playerJson);
            if(includeProfile){
                insertPlayerProfile(playerJson, playerData[profileIndex].trim(), playerId);
            }

            JsonObject relJson = new JsonObject();
            relJson.addProperty("teamId",teamMap.get(Integer.parseInt(playerData[teamIndex].trim())).toHexString());
            relJson.addProperty("playerId",playerId);
            createRelation(relJson);

            line = br.readLine();
        }
    }

    private static void setTeamMap(ObjectId tournamentId) throws Exception{

        System.out.println("Fetching all teams for current tournament");
        GET_CURRENT_TEAMS+=tournamentId+"&category=all";
        Type founderListType = new TypeToken<ArrayList<Team>>(){}.getType();
        ArrayList<Team> teamList = gson.fromJson(doGET(GET_CURRENT_TEAMS), founderListType);
        System.out.println("Team info - "+teamList);
        for(Team team : teamList){
            teamMap.put(team.getTeamTag().getTeamTagId(), team.getId());
        }
    }

    private static Tournament getCurrentTournament() throws Exception{
        System.out.println("Fetching current tournament info for season "+season);
        Tournament tournament = gson.fromJson(doGET(GET_CURRENT_TOURNAMENT), Tournament.class);
        System.out.println("current tournament id - "+tournament.getId());
        return tournament;
    }

    private static void loadFixtures() throws Exception{

        int dateMillisIndex, team1Index, team2Index;

        System.out.println("set index for below fields\ntimeStampMillis\nTeam1\nTeam2");
        dateMillisIndex = sc.nextInt();
        team1Index = sc.nextInt();
        team2Index = sc.nextInt();

        ObjectId tournamentId = getCurrentTournament().getId();
        setTeamMap(tournamentId);

        System.out.println("Expected file :: " + fixtureDetailPath);
        BufferedReader br = new BufferedReader(new FileReader(new File(fixtureDetailPath)));

        String line = br.readLine();
        while (line != null) {

            line = line.trim();
            String[] fixtureData = line.split(",");

            JsonObject fixtureJson = new JsonObject();
            fixtureJson.addProperty("matchDateTime",fixtureData[dateMillisIndex].trim());
            fixtureJson.addProperty("team1",teamMap.get(Integer.parseInt(fixtureData[team1Index].trim())).toHexString());
            fixtureJson.addProperty("team2",teamMap.get(Integer.parseInt(fixtureData[team2Index].trim())).toHexString());
            fixtureJson.addProperty("tournamentId",tournamentId.toHexString());
            createFixtures(fixtureJson);
            line = br.readLine();

        }
    }

    private static void insertPlayerProfile(JsonObject playerJson, String imageName, String playerId) throws Exception{

        String imageLink = uploadImage(imageName, playerId);

        playerJson.addProperty("profile",imageLink);
        updatePlayer(playerJson, playerId);
    }

    private static String createPlayer(JsonObject playerJson) throws Exception{
        return doPOST(CREATE_PLAYER, playerJson.toString());
    }

    private static String createFixtures(JsonObject fixtureJson) throws Exception{
        return doPOST(CREATE_FIXTURE, fixtureJson.toString());
    }

    private static void updatePlayer(JsonObject player, String playerId) throws Exception{
        doPUT(UPDATE_PLAYER+playerId, player.toString());
    }

    private static void createRelation(JsonObject relationJson) throws Exception {
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

    private static String uploadImage(String fileName, String playerId) throws Exception {

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

    private static String doGET(String uri) throws Exception {
        URL obj = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
//        System.out.println("GET Response Code :: " + responseCode + " for URI :: "+uri);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getString(con.getInputStream());
        }else {
            throw new Exception(getString(con.getErrorStream()));
        }
    }

    private static String doPOST(String uri, String payLoad) throws Exception {
        URL obj = new URL(uri);
        String encoded = Base64.getEncoder().encodeToString(("ahl2020"+":"+"ahlserver@bala").getBytes(StandardCharsets.UTF_8));  //Java 8
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Basic "+encoded);

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

    private static String doPUT(String uri, String payLoad) throws Exception {
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
//        System.out.println("PUT Response Code :: " + responseCode + " for URI :: "+uri);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getString(con.getInputStream());
        } else {
            throw new Exception(getString(con.getErrorStream()));
        }
    }

    private static String getString(InputStream inputStream) throws Exception{

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
