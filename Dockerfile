FROM maven:3.5-jdk-8 as builder

COPY . /src
WORKDIR /src
RUN mvn package

FROM tomcat:9-jre8-slim

RUN rm -rf /usr/local/tomcat/webapps/*

ADD https://ch.tudelft.nl/certs/wisvch.crt /usr/local/share/ca-certificates/wisvch.crt
RUN chmod 0644 /usr/local/share/ca-certificates/wisvch.crt && update-ca-certificates

ADD https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic.jar /opt/newrelic/
ADD https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic.yml /opt/newrelic/
RUN sed -i 's/My Application/Connect/g' /opt/newrelic/newrelic.yml

ENV CATALINA_OPTS="-javaagent:/opt/newrelic/newrelic.jar"

COPY --from=builder /src/wisvch-connect-overlay/target/connect.war /usr/local/tomcat/webapps/ROOT.war
