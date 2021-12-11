# CH Connect

[OpenID Connect][oidc] for W.I.S.V. 'Christiaan Huygens'.

OpenID Connect (OIDC) lets you log into a remote site or application using your identity without exposing your
credentials. In addition, OIDC can provide this application with additional user information like full name or phone
number. For W.I.S.V. 'Christiaan Huygens', this OIDC implementation is configured to allow login through TU Delft NetID
(SAML) and CH Accounts (LDAP). Both login methods are linked to the [Dienst2][dienst2] member administration to verify
identity. Dienst2 is also the source of any additional user information.

This code is hosted for production use at [connect.ch.tudelft.nl][prod]. Developers can register their own applications
by logging in there.

## Development

This project is a WAR overlay for [MITREid Connect][upstream], which is included from Maven Central.

Configuration is done through a properties file; refer to `config/application-example.properties` and make a copy as
`config/application.properties`. An HSQLDB instance will be launched with demo data; to log in, use `admin:password`.

The servlet container can be configured with the following parameters:
```
-Dspring.config.location=$PROJECT_DIR$/config/application.properties
-Dlog4j.configurationFile=$PROJECT_DIR$/config/log4j2-dev.xml
-Djavax.net.ssl.trustStore=$PROJECT_DIR$/wisvch.truststore
-Djavax.net.ssl.trustStorePassword=changeit
```
## Production

Configure the location of the properties file with `-Dspring.config.location=/path/to/config/application.properties` as
a JVM parameter. Spring profile `production` is used in production; to enable, add `-Dspring.profiles.active=production`
as a JVM parameter.

## Docker

Building:
```bash
docker build .
```

Production images are built using a [GitHub Actions workflow][gh-actions] and can be downloaded from the
[GitHub Container Registry][ghcr].

[ghcr]: https://github.com/WISVCH/connect/pkgs/container/connect "GitHub Container Registry"

[gh-actions]: https://github.com/WISVCH/connect/actions "GitHub Actions workflow"
[oidc]: http://openid.net/connect/ "OpenID Connect"
[dienst2]: https://github.com/WISVCH/dienst2 "Dienst2"
[prod]: https://connect.ch.tudelft.nl/ "CH Connect"
[upstream]: https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server "MitreID Connect"
