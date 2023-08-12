# CH Connect

[OpenID Connect][oidc] for W.I.S.V. 'Christiaan Huygens'.

OpenID Connect (OIDC) lets you log into a remote site or application using your identity without exposing your
credentials. In addition, OIDC can provide this application with additional user information like full name or phone
number. For W.I.S.V. 'Christiaan Huygens', this OIDC implementation is configured to allow login through TU Delft NetID
(SAML) and CH Google Accounts. Both login methods are linked to the [Dienst2][dienst2] member administration to verify
identity. Dienst2 is also the source of any additional user information.

This code is hosted for production use at [connect.ch.tudelft.nl][prod]. Developers can register their own applications
by logging in there.

## Development

This project is a WAR overlay for [MITREid Connect][upstream], which is included from Maven Central.


### IntelliJ IDEA
1. Install JDK 1.8: `File` > `Project Structure` > `Project SDK` > `+` > `Download` > `1.8` (`Cornetto` works)
2. Set up the project: `File` > `Project Structure` > `Project` > `Project SDK` > `1.8` > `Apply`
3. Copy `config/application-example.properties` to `config/application.properties` and make any necessary changes. An HSQLDB instance will be 
   launched with demo data; to log in, use `admin:password`.
4. To make sure the config is loaded we need to configure the servlet container with the following parameters:
    ```
    -Dspring.config.location=$PROJECT_DIR$/config/application.properties
    -Dlog4j.configurationFile=$PROJECT_DIR$/config/log4j2-dev.xml
    -Djavax.net.ssl.trustStore=$PROJECT_DIR$/wisvch.truststore
    -Djavax.net.ssl.trustStorePassword=changeit
    ```
    where `$PROJECT_DIR$` is the path to the project.
    These values can be places in `.mvn/jvm.config`. Or you can add them to the `VM options` in the `Run/Debug Configurations` dialog.
5. Run the project with `mvn jetty:run` or via the Maven toolbar in IntelliJ IDEA.

#### Setup HTTPS (Advanced)
Some SAML clients require HTTPS. To enable HTTPS, you can follow the instructions below.
1. Create a keystore with a self-signed certificate:
    ```bash
    keytool -genkey -alias jetty -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore jetty.p12 -validity 3650
    ```
2. Convert the keystore to a JKS keystore:
    ```bash
    keytool -importkeystore -srckeystore jetty.p12 -destkeystore jetty.jks -deststoretype JKS
    ```
3. Configure the keystore in `application.properties` by setting:
    ```properties
   server.ssl.key-keystore=jetty.jks
   server.ssl.key-store-password=[YOUR KEYSTORE PASSWORD]
   server.ssl.key-password=[YOUR KEY PASSWORD]
    ```
   and set the issuer URI to `https://localhost:8443/`:
   ```properties
   server.issuer=https://localhost:8443
   server.port=8443
   server.force_https=true
    ```
4. Edit `pom.xml` to configure the Jetty plugin:
   ```xml
   <plugin>
       <groupId>org.eclipse.jetty</groupId>
       <artifactId>jetty-maven-plugin</artifactId>
       <version>9.3.5.v20151012</version>
       <configuration>
           <systemPropertiesFile>${project.basedir}/config/application.properties</systemPropertiesFile>
           <jettyXml>jetty.xml</jettyXml>
       </configuration>
   </plugin>
   ```
   make sure that you do not commit this change.
5. If you want to use Google SAML, you are not allowed to redirect traffic to localhost. You can use [ngrok](https://ngrok.com/) to
   create a tunnel to your local machine. You can then use the ngrok URL as the issuer URI:
    ```shell script
    ngrok http https://localhost:8443 
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
