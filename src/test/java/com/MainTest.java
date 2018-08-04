package com;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import io.krakens.grok.api.Match;
import org.apache.commons.lang.StringUtils;
import org.apache.flume.Channel;
import org.apache.flume.Event;
import org.apache.flume.Sink;
import org.apache.flume.Transaction;
import org.apache.flume.event.SimpleEvent;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainTest {

    @Test
    public void testApache() throws Exception{
        /* Create a new grokCompiler instance */
        GrokCompiler grokCompiler = GrokCompiler.newInstance();
        grokCompiler.registerDefaultPatterns();

        /* Grok pattern to compile, here httpd logs */
        final Grok grok = grokCompiler.compile("%{COMBINEDAPACHELOG}");

        /* Line of log to match */
        String log = "112.169.19.192 - - [06/Mar/2013:01:36:30 +0900] \"GET / HTTP/1.1\" 200 44346 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.152 Safari/537.22\"";

        Match gm = grok.match(log);

        /* Get the map with matches */
        final Map<String, Object> capture = gm.capture();

        System.out.println(capture);
        System.out.println("json data:");
        String json = new GsonBuilder().create().toJson(capture);
        System.out.println(json);

    }

    @Test
    public void testNginx() throws Exception{
        /* Create a new grokCompiler instance */
        GrokCompiler grokCompiler = GrokCompiler.newInstance();
        grokCompiler.registerDefaultPatterns();


        /* Grok pattern to compile, here httpd logs */
        final Grok grok = grokCompiler.compile("%{COMBINEDAPACHELOG} %{QS:x_forwarded_for}");

        /* Line of log to match */
        String log = "121.33.193.84 - - [31/Jul/2018:07:52:04 +0000] \"GET /inline.455de01cf16bff500aa5.bundle.js HTTP/1.1\" 200 1395 \"http://qt-pc.zhuwenda.com:50001/pages/fund/fund-baseinfo?reuse=true\" \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.170 Safari/537.36\" \"-\"";
        //String log = "你好";
        Match gm = grok.match(log);

        /* Get the map with matches */
        final Map<String, Object> capture = gm.capture();

        System.out.println(capture);
        System.out.println("json data:");
        String json = new GsonBuilder().create().toJson(capture);
        System.out.println(json);
    }

    @Test
    public void testConnection() throws Exception{

        String hosts_configured = "efk2.zhuwenda.com:9200";
        List<String> es_hosts=Arrays.asList(hosts_configured.split(","));
        List<HttpHost> hosts = new ArrayList<>(es_hosts.size());
        for (String hostslist: es_hosts) {
            String hostname = hostslist.split(":")[0];
            int port = Integer.parseInt(hostslist.split(":")[1]);
            System.out.println("Connecting to "+hostname+":"+port);
            hosts.add(new HttpHost(hostname,port,"http"));
        }
        RestClientBuilder builder = RestClient.builder(hosts.toArray(new HttpHost[hosts.size()]));

        RestClient restClient = builder.build();

        System.out.println(restClient);
    }

}
