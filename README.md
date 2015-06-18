# WISVCH Connect [![Build Status](https://travis-ci.org/WISVCH/connect.svg)](https://travis-ci.org/WISVCH/connect)
OpenID Connect for W.I.S.V. 'Christiaan Huygens'

## Development

This project is a WAR overlay for MITREid Connect version 1.2, which is still in development. Therefore we use a Git submodule instead of a Maven Central dependency to include the MITREid Connect project.

After cloning issue a `git submodule update --init --recursive` command to set up the git submodules,
then you should be able to run `mvn package`.
