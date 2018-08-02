package com;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import io.krakens.grok.api.Match;
import org.apache.flume.Channel;
import org.apache.flume.Event;
import org.apache.flume.Sink;
import org.apache.flume.Transaction;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Map;

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


}
