# Nytta

[![Build](https://github.com/Timeular/nytta/actions/workflows/maven-build.yml/badge.svg?branch=main)](https://github.com/Timeular/nytta/actions/workflows/maven-build.yml)

This is a collection of libraries which intend to be small helpers/utils.
To read more about the nytta helpers visit the different readme files of the modules:

* [Http Client](http-client/README.md)
* [Spring Extensions](spring-ext/README.md)
* [iHop](ihop/README.md)
* [Tracking](tracking/README.md)
* [E-Mail](email/README.md)
* [Prova](prova/README.md)

## Development

All modules are written in kotlin and released together. This means that if in one module a change occurrs
then all other modules will be released with the same version (usually there are at least dependency upgrades
in all modules).

All artifacts are hosted on the maven central repository. Snapshots are not published.

### Deployment

Releases are published to Maven Central through the [Central Portal](https://central.sonatype.com)
under the `com.timeular.nytta` namespace. You need:

* a Central Portal user token with access to the namespace, in `~/.m2/settings.xml`:

```xml
<settings>
    <servers>
        <server>
            <id>central</id>
            <username>TOKEN_USERNAME</username>
            <password>TOKEN_PASSWORD</password>
        </server>
    </servers>
</settings>
```

* a GPG key, published to `keyserver.ubuntu.com` or `keys.openpgp.org`

For releases you have to proceed the following steps:

```bash
# set version to new target version and update the version in the module READMEs
./mvnw versions:set -DnewVersion=4.0.0 -DgenerateBackupPoms=false

# commit and tag version
git commit -am "Release to version 4.0.0"
git tag -a "4.0.0" -m "Release 4.0.0"

# build, sign and publish to maven central
./mvnw clean deploy -P release

# set to next version
./mvnw versions:set -DnewVersion=4.0.1-SNAPSHOT -DgenerateBackupPoms=false
git commit -am "Set version to 4.0.1-SNAPSHOT"

git push
git push --tags
```
