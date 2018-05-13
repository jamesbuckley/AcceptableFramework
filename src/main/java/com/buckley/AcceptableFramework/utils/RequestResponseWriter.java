package com.buckley.AcceptableFramework.utils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.util.DefaultPropertiesPersister;

import javax.annotation.PreDestroy;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArraySet;

public class RequestResponseWriter {

    private static CopyOnWriteArraySet<HashMap<String, Object>> requestResponseMaps = new CopyOnWriteArraySet<>();
    private static String outputFile;
    private static String[] paramsToIgnore;

    @Value("${acceptable.output.filename}")
    public void setDatabase(String outputFileName) {
        outputFile = outputFileName;
    }

    @Value("${acceptable.query.param.ignore}")
    public static void setParamsToIgnore(String[] paramsToIgnoreProps) {
        paramsToIgnore = paramsToIgnoreProps;
    }

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
            DefaultPropertiesPersister p = new DefaultPropertiesPersister();
            p.store(prop, out, "Acceptable Recorde Requests");
            //prop.store(out, null);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static String formatQueryString(String originalQuery){
        // = environment.getProperty("acceptable.query.param.ignore", String[].class);
        String[] params = originalQuery.split("&", -1);
        String[] paramsCopy = (String[]) ArrayUtils.clone(params);

        for(int i=0; i< params.length; i++){
            if(ArrayUtils.contains(paramsToIgnore,params[i].substring(0, params[i].indexOf("=")))){
                paramsCopy = (String[]) ArrayUtils.removeElement(params, params[i]);
            }
        }
        Arrays.sort(paramsCopy);
        return StringUtils.join(paramsCopy, '&');
    }
}
