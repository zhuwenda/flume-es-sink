package com.legion.flume.interceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiLineInterceptor implements Interceptor {

    private final Matcher regex;
    private final boolean negate;
    private final What what;
    private final Charset charset;

    public MultiLineInterceptor(String regex, boolean negate, String charset, String what ){
        this.regex = Pattern.compile(regex).matcher("");
        this.negate = negate;
        this.charset = charset == null ? Charset.forName("utf-8") : Charset.forName(charset);
        this.what = What.valueOf(what);
    }

    @Override
    public void initialize() {
        System.out.println("****");
        System.out.println("拦截器：初始化");
        System.out.println("----");
    }

    @Override
    public Event intercept(Event event) {
        System.out.println("****");
        System.out.println("拦截器：接收单个事件");
        System.out.println("----");
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        System.out.println("****");
        System.out.println("拦截器：接收多个事件，事件数量："+events.size());
        for(Event event : events){
            System.out.println("header:");
            System.out.println(event.getHeaders().toString());
            System.out.println("body:");
            System.out.println(new String(event.getBody(),charset).toString());
        }

        List<Event> newEventList = multiLine(events);
        System.out.println("处理后：事件数量："+newEventList.size());
        for(Event event : newEventList){
            System.out.println("header:");
            System.out.println(event.getHeaders().toString());
            System.out.println("body:");
            System.out.println(new String(event.getBody(),charset).toString());
        }

        System.out.println("----");

        return newEventList;
    }

    public List<Event> multiLine(List<Event> oldList){
        List<Event> newList = new ArrayList<>();
        StringBuilder lines = null;
        Event firstEvent = oldList.get(0);
        for (int i = 0; i < oldList.size(); i++) {
            String line = new String(oldList.get(i).getBody(),charset);
            System.out.println("第["+i+"]行的数据："+line);
            if (lines == null) {
                lines = new StringBuilder(line);
            } else {
                boolean isMatch = regex.reset(line).matches();
                System.out.println("第["+i+"]行匹配状态："+isMatch);
                if (negate) {
                    isMatch = !isMatch;
                }
              /*
              not match && previous --> do next
              not match && next     --> do previous
              match && previous     --> do previous
              match && next         --> do next
              */
                boolean doPrevious = (what == What.previous);
                if (!isMatch) {
                    doPrevious = !doPrevious;
                }

                if (doPrevious) { // do previous
                    lines.append('\n');
                    lines.append(line);
                } else {          // do next
                    if(lines.length() > 0){
                        firstEvent.setBody(lines.toString().getBytes(charset));
                        newList.add(firstEvent);
                    }
                    lines.setLength(0);
                    lines.append(line);
                    firstEvent = oldList.get(i);
                }
            }
        }
        System.out.println("inputStream读取结束");
        if (lines != null && lines.length() > 0) {
            firstEvent.setBody(lines.toString().getBytes(charset));
            newList.add(firstEvent);
        }

        return newList;
    }



    @Override
    public void close() {
        System.out.println("****");
        System.out.println("拦截器：关闭");
        System.out.println("----");

    }

    private static enum What {
        previous,
        next
    }

    /**
     * Builder which builds new instances of the HostInterceptor.
     */
    public static class Builder implements Interceptor.Builder {

        private String regex;
        private boolean negate;
        private String charset;
        private String what;

        @Override
        public Interceptor build() {
            return new MultiLineInterceptor(regex,negate,charset,what);
        }

        @Override
        public void configure(Context context) {


            this.regex = context.getString("regex");
            this.negate = context.getBoolean("negate", false);
            this.charset = context.getString("charset", "utf-8");
            this.what = context.getString("what",What.previous.name());


        }

    }
}
