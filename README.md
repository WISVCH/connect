# WISVCH Connect [![Build Status](https://travis-ci.org/WISVCH/connect.svg)](https://travis-ci.org/WISVCH/connect)
OpenID Connect for W.I.S.V. 'Christiaan Huygens'

## Development

This project is a WAR overlay for [MITREid Connect](https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server) version 1.2, which is still in development. Therefore we use a Git submodule instead of a Maven Central dependency to include the MITREid Connect project. Once there is a stable 1.2 release, we should migrate away from the submodule.

After cloning issue a `git submodule update --init --recursive` command to set up the Git submodules,
then you should be able to run `mvn package`.

Configuration is done through a properties file; see `config/application-example.properties`. Configure the location of the properties file with `-Dspring.config.location=/path/to/config/application.properties` as a JVM parameter.

In development, an HSQLDB instance is launched with demo data. To log in, use `admin:password`. Spring profile `production` is used in production; to enable, add `-Dspring.profiles.active=production` as a JVM parameter.