package org.nameserver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ServiceDetails {
    private String IPAddress;
    private int port;
    private String protocol;
    ServiceDetails populate(String serverResponse){
        JSONObject serverResponseJSONObject = new
                JSONObject(serverResponse);
        if (serverResponseJSONObject.has("kvs")) {
            JSONArray values =
                    serverResponseJSONObject.getJSONArray("kvs");
            JSONObject firstValue = (JSONObject)
                    values.get(0);
            String encodedValue = (String)
                    firstValue.get("value");
            byte[] serverDetailsBytes = Base64.getDecoder().decode(encodedValue.getBytes(StandardCharsets.UTF_8));
            JSONObject serverDetailsJson = new
                    JSONObject(new String(serverDetailsBytes));
            IPAddress =
                    serverDetailsJson.get("ip").toString();
            port =
                    Integer.parseInt(serverDetailsJson.get("port").toString( ));
            protocol =
                    serverDetailsJson.get("protocol").toString();
            return this;
        } else {
            return null;
        } }
    public String getIPAddress(){
        return IPAddress;
    }

    public int getPort(){
        return port;
    }
    public String getProtocol(){
        return protocol;
    }
}
