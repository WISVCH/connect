# WISVCH Connect [![Build Status](https://travis-ci.org/WISVCH/connect.svg)](https://travis-ci.org/WISVCH/connect) [![Download](https://api.bintray.com/packages/wisvch/maven/wisvch-connect/images/download.svg)][4] [![Docker Repository on Quay](https://quay.io/repository/wisvch/connect/status "Docker Repository on Quay")][8]

[OpenID Connect][1] for W.I.S.V. 'Christiaan Huygens'.

OpenID Connect (OIDC) lets you log into a remote site or application using your identity without exposing your
credentials. In addition, OIDC can provide this application with additional user information like full name or phone
number. For W.I.S.V. 'Christiaan Huygens', this OIDC implementation is configured to allow login through TU Delft NetID
(SAML) and CH Accounts (LDAP). Both login methods are linked to the [Dienst2][2] member
administration to verify identity. Dienst2 is also the source of any additional user information.

This code is hosted for production use at [connect.ch.tudelft.nl][3]. Developers can
register their own applications by logging in there.

A client library is provided in the form of `wisvch-connect-client` which is hosted on our
[Bintray Maven repository][4]. For an example implementation, take a look at the [CH Events application][5] or the [MITREid Connect
simple-web-app][6]. 

## Development

This project is a WAR overlay for [MITREid Connect][7], which is included from Maven Central.

Configuration is done through a properties file; refer to `config/application-example.properties` and make a copy as
`config/application.properties`. An HSQLDB instance will be launched with demo data; to log in, use `admin:password`.

The servlet container can be configured with the following parameters:
```
-Dspring.config.location=$PROJECT_DIR$/config/application.properties -Dlog4j.configuration=log4j-dev.xml
-Djavax.net.ssl.trustStore=$PROJECT_DIR$/wisvch.truststore -Djavax.net.ssl.trustStorePassword=changeit
```

## Production

Configure the location of the properties file with `-Dspring.config.location=/path/to/config/application.properties` as
a JVM parameter. Spring profile `production` is used in production; to enable, add `-Dspring.profiles.active=production`
as a JVM parameter.

## Releases

Releases can be found on [our Bintray repository][4].

New releases can be made with a two-step process: `mvn release:prepare` increases the version number and creates a Git
tag, a subsequent `mvn release:perform` will build the tagged version and upload it to Bintray.

## Docker

Building:
```bash
docker build .
```

Production images are built by and available from [Quay][8].

[1]: http://openid.net/connect/ "OpenID Connect"
[2]: https://github.com/WISVCH/dienst2 "Dienst2"
[3]: https://connect.ch.tudelft.nl/ "CH Connect"
[4]: https://bintray.com/wisvch/maven/wisvch-connect/_latestVersion "WISVCH Bintray Maven repository"
[5]: https://github.com/WISVCH/events "CH Events"
[6]: https://github.com/mitreid-connect/simple-web-app "MITREid Connect simple-web-app"
[7]: https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server "MitreID Connect"
[8]: https://quay.io/repository/wisvch/connect "Docker Repository on Quay"