/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ta.asianmonitor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author 01PH1286.Carl.T
 */
public class AMController {

    public static String DOMAIN = "http://62.138.184.100/backend/export/oddsfeed.php?";
    private final HttpClient httpClient;

//    String prevDbOddsString = "";
    ConcurrentHashMap<String, String> matchString = new ConcurrentHashMap();

    public AMController() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public static void main(String[] args) {
        AMController am = new AMController();
        am.start();
    }

    private void start() {
        startOddsRequestor("client=188bet", 1, TimeUnit.MINUTES, "db");
        startOddsRequestor("client=188bet&inplay=1", 30, TimeUnit.SECONDS, "rb");
    }

    private void startOddsRequestor(String uri, int timeVal, TimeUnit tu, String type) {
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                getOdds(uri, type);
            }
        };
        ex.scheduleAtFixedRate(r, 0, timeVal, tu);
    }

    private void getOdds(String uri, String type) {

        System.out.println("getOdds " + type);
        String dbOddsString = "";
        try {
            dbOddsString = getAsync(uri);
        } catch (Exception e) {
            try {
                dbOddsString = getAsync(uri);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
//        String dbOddsString = "{\"request\":{\"client\":\"188bet\",\"timestamp\":1652360140,\"bookies\":\"all\"},\"data\":{\"105663536\":{\"sport\":\"ESport\",\"team1\":\"SunXet Club\",\"team2\":\"KRU\",\"evtime\":\"2022-05-14 21:00:00\",\"region\":\"Valorant\",\"regionId\":\"9510017647\",\"league\":\"Champions Tour. Latin America South\",\"leagueId\":10021442,\"odds\":{\"bet365\":{\"ou|2.5\":{\"ou\":2.5,\"bettype\":\"ou\",\"qo\":6.5,\"qu\":1.1},\"ahc|1.5\":{\"hcp\":1.5,\"bettype\":\"ahc\",\"q1\":6.5,\"q2\":1.1},\"cs|0\":{\"bettype\":\"cs\",\"h2a0\":\"21.00\",\"h2a1\":\"41.00\",\"h0a2\":\"1.10\",\"h1a2\":\"7.00\"},\"map1|0\":{\"bettype\":\"map1\",\"q1\":8.5,\"q2\":1.05}},\"ggbet\":{\"2way|0\":{\"bettype\":\"2way\",\"q1\":9.2,\"q2\":1.01},\"map1|0\":{\"bettype\":\"map1\",\"q1\":6.39,\"q2\":1.06}},\"ladbrokes\":{\"2way|0\":{\"bettype\":\"2way\",\"q1\":34,\"q2\":1.01},\"map1|0\":{\"bettype\":\"map1\",\"q1\":10,\"q2\":1.04}},\"pinnacle\":{\"2way|0\":{\"bettype\":\"2way\",\"q1\":14.27,\"q2\":1.01},\"ou|2.5\":{\"ou\":2.5,\"bettype\":\"ou\",\"qo\":5.04,\"qu\":1.12},\"ahc|1.5\":{\"hcp\":1.5,\"bettype\":\"ahc\",\"q1\":4.75,\"q2\":1.14},\"cs|0\":{\"bettype\":\"cs\",\"h2a0\":\"20.00\",\"h2a1\":\"16.00\",\"h0a2\":\"1.14\",\"h1a2\":\"5.25\"},\"map1|0\":{\"bettype\":\"map1\",\"q1\":7.87,\"q2\":1.05}}}},\"105663649\":{\"sport\":\"ESport\",\"team1\":\"Optix\",\"team2\":\"9z\",\"evtime\":\"2022-05-15 00:00:00\",\"region\":\"Valorant\",\"regionId\":\"9510017647\",\"league\":\"Champions Tour. Latin America South\",\"leagueId\":10021442,\"odds\":{\"bet365\":{\"2way|0\":{\"bettype\":\"2way\",\"q1\":3.4,\"q2\":1.3},\"ou|2.5\":{\"ou\":2.5,\"bettype\":\"ou\",\"qo\":2.2,\"qu\":1.61},\"ahc|-1.5\":{\"hcp\":-1.5,\"bettype\":\"ahc\",\"q1\":6,\"q2\":1.11},\"ahc|1.5\":{\"hcp\":1.5,\"bettype\":\"ahc\",\"q1\":1.72,\"q2\":2},\"cs|0\":{\"bettype\":\"cs\",\"h2a0\":\"6.00\",\"h2a1\":\"6.00\",\"h0a2\":\"2.00\",\"h1a2\":\"3.25\"},\"map1|0\":{\"bettype\":\"map1\",\"q1\":2.62,\"q2\":1.44}},\"ggbet\":{\"2way|0\":{\"bettype\":\"2way\",\"q1\":2.66,\"q2\":1.38},\"map1|0\":{\"bettype\":\"map1\",\"q1\":2.36,\"q2\":1.48}},\"pinnacle\":{\"2way|0\":{\"bettype\":\"2way\",\"q1\":2.81,\"q2\":1.36},\"ou|2.5\":{\"ou\":2.5,\"bettype\":\"ou\",\"qo\":2.3,\"qu\":1.52},\"ahc|-1.5\":{\"hcp\":-1.5,\"bettype\":\"ahc\",\"q1\":4.94,\"q2\":1.13},\"ahc|1.5\":{\"hcp\":1.5,\"bettype\":\"ahc\",\"q1\":1.7,\"q2\":1.99},\"cs|0\":{\"bettype\":\"cs\",\"h2a0\":\"4.90\",\"h2a1\":\"4.90\",\"h0a2\":\"1.91\",\"h1a2\":\"3.50\"},\"map1|0\":{\"bettype\":\"map1\",\"q1\":2.54,\"q2\":1.43}}}}}}";
//        System.out.println(dbOddsString);
        JSONObject jo = new JSONObject(dbOddsString).getJSONObject("data");
        Iterator<String> keys = jo.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject joMatch = (JSONObject) jo.get(key);
            if (jo.get(key) instanceof JSONObject) {
                if (!matchString.containsKey(key)) {    //match level not odds level
                    matchString.put(key, joMatch.toString());
                    System.out.println(type + " new odds: " + key + " " + joMatch.toString());
                } else {
                    if (!matchString.get(key).equals(joMatch.toString())) {
                        System.out.println(type + " updated odds: " + key + " " + joMatch.toString());
                    }
                }
            }
        }
    }

    private String getAsync(String uri) {
        String resp = "";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(DOMAIN + uri))
                    //                    .setHeader("Authorization", "Basic " + cred)
                    //                    .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36")
                    .setHeader("Content-Type", "application/json")
                    .build();

            CompletableFuture<HttpResponse<String>> response
                    = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            resp = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);

        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(AMController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            Logger.getLogger(AMController.class.getName()).log(Level.SEVERE, null, e);
        }
        return resp;
    }

}
