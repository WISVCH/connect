FROM tomcat:8.5-jre8
MAINTAINER Mark Janssen <mark@praseodym.net>

RUN rm -rf /usr/local/tomcat/webapps/*

RUN curl -so /usr/local/share/ca-certificates/wisvch.crt https://ch.tudelft.nl/certs/wisvch.crt && \
    chmod 644 /usr/local/share/ca-certificates/wisvch.crt && \
    update-ca-certificates

RUN mkdir -p /opt/newrelic && \
    curl -so /tmp/newrelic.zip https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip && \
    unzip -j /tmp/newrelic.zip newrelic/newrelic.jar newrelic/newrelic.yml -d /opt/newrelic && \
    sed -i 's/My Application/Connect/g' /opt/newrelic/newrelic.yml

ENV CATALINA_OPTS="-javaagent:/opt/newrelic/newrelic.jar"
EXPOSE 8443

ADD wisvch-connect-overlay/target/connect.war /usr/local/tomcat/webapps/ROOT.war
