package com.buckley.AcceptableFramework.utils;

import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PreDestroy;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArraySet;

public class RequestResponseWriter {

    private static String outputFile = "temp.properties";

    private static CopyOnWriteArraySet<HashMap<String, Object>> requestResponseMaps = new CopyOnWriteArraySet<>();

    public static void addToRequestResponseMaps(HashMap<String, Object> requestResponse){
        requestResponseMaps.add(requestResponse);
    }

    @PreDestroy
    public static void createURLMapEntry()  {

        Properties prop = new Properties();
        try(FileWriter fw = new FileWriter(outputFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            int requestNumber = 0;
            for (HashMap map: requestResponseMaps){
                String responseBody = (String)map.get("response");
                HttpRequestBase request = (HttpRequestBase)map.get("request");
                String queryString = map.get("query") != null ? (String)map.get("query") : "";
                String baseKey = "app.requests["+requestNumber+"].";
                prop.setProperty(baseKey+"url", request.getURI().getPath());
                prop.setProperty(baseKey+"query", queryString);
                prop.setProperty(baseKey+"response", responseBody);
                requestNumber++;
            }
            prop.store(out, null);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
