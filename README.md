# Nytta

[![Build Status](https://travis-ci.org/Timeular/nytta.svg?branch=master)](https://travis-ci.org/Timeular/nytta)

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

All artifacts are hosted on the maven central repository but if you want to include snapshot releases you have
to directly include the  sonatype snapshot repository:

```gradle
maven {
    url "https://oss.sonatype.org/content/repositories/snapshots/"
}
```

### Deployment

If you want to deploy the artifact to the sonatype repository you need appropriate permissions and you have to execute
the following command in the root folder.

```bash
mvn clean deploy
```

For releases the maven release plugin is used. Therefore you have to execute two separate command:

```bash
mvn release:prepare

mvn release:perform
```
