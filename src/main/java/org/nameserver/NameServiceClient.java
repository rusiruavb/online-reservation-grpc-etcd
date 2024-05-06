package org.nameserver;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class NameServiceClient {
    private EtcdClient etcdClient;

    public NameServiceClient (String nameServiceAddress)
            throws IOException {
        etcdClient = new EtcdClient(nameServiceAddress);
    }

    public static String buildServerDetailsEntry(String serviceAddress, int port, String protocol){
        return new JSONObject()
                .put("ip", serviceAddress)
                .put("port", Integer.toString(port)) .put("protocol", protocol) .toString();
    }

    public ServiceDetails findService(String serviceName) throws InterruptedException, IOException {
        System.out.println("Searching for details of service :" + serviceName);
        String etcdResponse = etcdClient.get(serviceName);
        ServiceDetails serviceDetails = new ServiceDetails().populate(etcdResponse);
        while (serviceDetails == null) {
            System.out.println("Couldn't find details of service " + serviceName + ", retrying in 3 seconds."); Thread.sleep(5000);
            etcdResponse = etcdClient.get(serviceName);
            serviceDetails = new ServiceDetails().populate(etcdResponse);
        }
        return serviceDetails;
    }

    public void registerService(String serviceName, String IPAddress, int port, String protocol) throws IOException {
        String serviceInfoValue = buildServerDetailsEntry(IPAddress, port, protocol);
        etcdClient.put(serviceName, serviceInfoValue);
    }
}
