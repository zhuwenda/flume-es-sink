# Flume With Morphline
Flume With Morphline
=============================

简介
--

集成了morphline、elasticsearch rest sink

可直接以docker方式运行，也可将编译出来的后插件（`target/flume/plugins.d`)，放置到flume插件中运行，运行flume

`http://kitesdk.org/docs/current/morphlines/morphlines-reference-guide.html`
`http://kitesdk.org/docs/current/morphlines/index.html`

安装
--

Build the jar and add it alongside with the dependencies in the flume lib directory.

配置
--

Add the sink to the flume configuration as follows:
```
        ....
        agent.sinks.es.type = com.legion.flume.sink.ElasticsearchSink
        agent.sinks.es.hosts = host1:port1,host2:port2
        agent.sinks.es.indexName = test
        agent.sinks.es.indexType = bar_type
        agent.sinks.es.batchSize = 500
        ...
```

运行
--
`docker run -it -v $(pwd)/agent.properties:/opt/flume/conf/agent.properties zhuwenda/flume-with-morphline`

or debug logger:`docker run -it -e OPTIONS=-Dflume.root.logger=DEBUG,console -v $(pwd)/agent.properties:/opt/flume/conf/agent.properties zhuwenda/flume-with-morphline`

or `docker run -it -e OPTIONS=-Dflume.root.logger=DEBUG,console -v $(pwd)/agent-test.properties:/opt/flume/conf/agent.properties --name test --rm -v $(pwd)/morphline.conf:/opt/flume/morphline.conf -v $(pwd)/t.log:/opt/flume/t.log zhuwenda/flume-with-morphline`
