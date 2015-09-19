# WISVCH Connect [![Build Status](https://travis-ci.org/WISVCH/connect.svg)](https://travis-ci.org/WISVCH/connect)

[OpenID Connect](http://openid.net/connect/) for W.I.S.V. 'Christiaan Huygens'.

OpenID Connect (OIDC) lets you log into a remote site or application using your identity without exposing your
credentials. In addition, OIDC can provide this application with additional user information like full name or phone
number. For W.I.S.V. 'Christiaan Huygens', this OIDC implementation is configured to allow login through NetID and CH
Accounts (LDAP). Both login methods are linked to the [Dienst2](https://github.com/WISVCH/dienst2) member administration
to verify identity. Dienst2 is also the source of any additional user information.

This code is hosted for production use at [connect.ch.tudelft.nl](https://connect.ch.tudelft.nl/). Developers can
register their own applications by logging in there. For a sample implementation, take a look at the [MITREid Connect
simple-web-app](https://github.com/mitreid-connect/simple-web-app). 

## Development

This project is a WAR overlay for [MITREid Connect](https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server)
version 1.2.0 (the current stable version), which is included from Maven Central.

After cloning issue a `git submodule update --init --recursive` command to set up the Git submodules,
then you should be able to run `mvn package`.

Configuration is done through a properties file; see `config/application-example.properties`. Configure the location
of the properties file with `-Dspring.config.location=/path/to/config/application.properties` as a JVM parameter.

In development, an HSQLDB instance is launched with demo data. To log in, use `admin:password`. Spring profile
`production` is used in production; to enable, add `-Dspring.profiles.active=production` as a JVM parameter.