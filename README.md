# FlumeElasticsearchRestSink
Flume elasticsearch REST sink
=============================

简介
--

This is a simple Elasticsearch flume sink based on the official java rest client by elastic.co https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/index.html

The sink should be compatible with all elasticsearch versions, it has been tested with elasticsearch 6.0.0

可直接以docker方式运行，也可将编译出来的后插件（`target/flume/plugins.d`)，放置到flume插件中运行，运行flume

安装
--

Build the jar and add it alongside with the dependencies in the flume lib directory.

配置
--

Add the sink to the flume configuration as follows:
```
        ....
        agent.sinks.es.type = com.flumetest.elasticsearch.ElasticsearchSink
        agent.sinks.es.hosts = host1:port1,host2:port2
        agent.sinks.es.indexName = test
        agent.sinks.es.indexType = bar_type
        agent.sinks.es.batchSize = 500
        ...
```

运行
--
`docker run -it flume`

or debug logger:`docker run -it -e OPTIONS=-Dflume.root.logger=DEBUG,console flume`
