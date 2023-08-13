FROM maven:3-jdk-8 as builder

COPY . /src
WORKDIR /src
ARG VERSION
RUN mvn versions:set -DnewVersion=${VERSION}
RUN mvn package

FROM tomcat:9-jre8-slim

RUN rm -rf /usr/local/tomcat/webapps/*

COPY context.xml /usr/local/tomcat/conf/context.xml
COPY --from=builder /src/target/connect.war /usr/local/tomcat/webapps/ROOT.war

RUN chown 998 /usr/local/tomcat/webapps /usr/local/tomcat/temp
USER 998