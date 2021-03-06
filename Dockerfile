FROM openjdk:8-jre-alpine

# Build variables
ARG FLUME_VERSION="1.8.0"
ARG SINK_VERSION=1.0.3

# Environment variables
ENV FLUME_AGENT_NAME "agent"
ENV FLUME_CONFIG_PATH "/opt/flume/conf"
ENV FLUME_CONFIG_FILE "/opt/flume/conf/agent.properties"
ENV OPTIONS ""

WORKDIR /opt/flume


#RUN apk update && apk add bash && rm -rf /var/cache/apk/*
RUN apk add bash && rm -rf /var/cache/apk/*

# Install flume
#ADD http://archive.apache.org/dist/flume/$FLUME_VERSION/apache-flume-$FLUME_VERSION-bin.tar.gz  /tmp
#RUN \
#  tar --strip-components 1 -xzf /tmp/apache-flume-$FLUME_VERSION-bin.tar.gz && \
#  rm -f /tmp/apache-flume-$FLUME_VERSION-bin.tar.gz

COPY docker-data/apache-flume-1.8.0-bin.tar.gz ./
RUN tar --strip-components 1 -xzf apache-flume-1.8.0-bin.tar.gz


# Copy resource
COPY docker-data/grok-dictionaries/ ./grok-dictionaries/
COPY docker-data/GeoLite2-City_20180807/ ./GeoLite2-City/

# Copy all plugins
COPY target/flume/ .


# Create flume user and switch to it
RUN adduser flume -h /opt/flume -s /bin/false -D flume
RUN chown -R flume /opt/flume/conf
USER flume


CMD exec /opt/flume/bin/flume-ng agent -n $FLUME_AGENT_NAME -c $FLUME_CONFIG_PATH -f $FLUME_CONFIG_FILE $OPTIONS

