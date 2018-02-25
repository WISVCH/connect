FROM maven:3.5-jdk-8 as builder

COPY . /src
WORKDIR /src
RUN mvn package

FROM tomcat:9-jre8-slim

RUN rm -rf /usr/local/tomcat/webapps/*

ADD https://ch.tudelft.nl/certs/wisvch.crt /usr/local/share/ca-certificates/wisvch.crt
RUN chmod 0644 /usr/local/share/ca-certificates/wisvch.crt && update-ca-certificates

ADD https://search.maven.org/remote_content?g=com.datadoghq&a=dd-java-agent&v=LATEST /opt/datadog/dd-java-agent.jar
ENV CATALINA_OPTS="-javaagent:/opt/datadog/dd-java-agent.jar"
ENV DD_SERVICE_NAME="connect"

COPY --from=builder /src/wisvch-connect-overlay/target/connect.war /usr/local/tomcat/webapps/ROOT.war
