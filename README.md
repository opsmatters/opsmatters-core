![opsmatters](https://i.imgur.com/VoLABc1.png)

# OpsMatters Core
[![Build Status](https://travis-ci.org/opsmatters/opsmatters-core.svg?branch=master)](https://travis-ci.org/opsmatters/opsmatters-core)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.opsmatters/opsmatters-core/badge.svg?style=blue)](https://maven-badges.herokuapp.com/maven-central/com.opsmatters/opsmatters-core)
[![Javadocs](http://javadoc.io/badge/com.opsmatters/opsmatters-core.svg)](http://javadoc.io/doc/com.opsmatters/opsmatters-core)

Java library for the opsmatters suite including a data cache for New Relic Monitoring and Alerting, models, and reporting utilities.

## Examples

To obtain a New Relic cache instance containing all the static configuration:
```
NewRelicCache cache = NewRelicCache.builder()
    .apiKey("<YOUR_API_KEY>")
    .alerts(true)
    .apm(true)
    .mobile(true)
    .browser(true)
    .synthetics(true)
    .insights(true)
    .servers(true)
    .plugins(true)
    .infrastructure(true)
    .build();
```
Next, create a manager to synchronise the cache with New Relic by importing all resources for the enabled products:
```
ProviderManager manager = ProviderFactory.getManager(Provider.NEW_RELIC);
boolean success = manager.sync(cache);
```
Once the cache has been populated, it can be queried for the resources and collections it contains. Here are some examples:

* To query the alert channels:
```
Collection<AlertChannel> channels = cache.alertChannels().list();
```
* To query the alert policies:
```
Collection<AlertPolicy> policies = cache.alertPolicies().list();
```
* To query the APM alert conditions for a particular alert policy:
```
Collection<AlertCondition> conditions = cache.alertPolicies().alertConditions(policy.getId()).list()
```
* To query the Infrastructure alert conditions for a particular alert policy:
```
Collection<InfraAlertCondition> infraConditions = cache.alertPolicies().infraAlertConditions(policy.getId()).list()
```
* To query the dashboards:
```
Collection<Dashboard> dashboards = cache.dashboards().list();
```

## Prerequisites

A New Relic account with an Admin user.
The user needs to generate an [Admin API Key](https://docs.newrelic.com/docs/apis/rest-api-v2/getting-started/api-keys) 
to provide read-write access via the [New Relic REST APIs](https://api.newrelic.com).
The Admin API Key is referenced in the documentation as the parameter "YOUR_API_KEY".

## Installing

First clone the repository using:
```
>$ git clone https://github.com/opsmatters/opsmatters-core.git
>$ cd opsmatters-core
```

To compile the source code, run all tests, and generate all artefacts (including sources, javadoc, etc):
```
mvn package 
```

## Running the tests

To execute the unit tests:
```
mvn clean test 
```

The following tests are included:

* ProviderTest: Creates a full NewRelicCache with all the available configuration items and then creates some test reports for the objects in a variety of formats.

## Deployment

The build artefacts are hosted in The Maven Central Repository. 

Add the following dependency to include the artefact within your project:
```
<dependency>
  <groupId>com.opsmatters</groupId>
  <artifactId>opsmatters-core</artifactId>
  <version>1.0.2</version>
</dependency>
```

## Built With

* [newrelic-api](https://github.com/opsmatters/newrelic-api) - Java client library for the New Relic Monitoring and Alerting REST APIs
* [docx4j](https://www.docx4java.org/trac/docx4j) - Java library for creating and manipulating Microsoft Open XML files
* [jxl](http://jexcelapi.sourceforge.net/) - Java API enabling developers to read, write, and modify Excel spreadsheets
* [opencsv](http://opencsv.sourceforge.net/) - CSV parser library for Java
* [Maven](https://maven.apache.org/) - Dependency Management
* [JUnit](http://junit.org/) - Unit testing framework

## Contributing

Please read [CONTRIBUTING.md](https://www.contributor-covenant.org/version/1/4/code-of-conduct.html) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

This project use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/opsmatters/opsmatters-core/tags). 

## Authors

* **Gerald Curley** - *Initial work* - [opsmatters](https://github.com/opsmatters)

See also the list of [contributors](https://github.com/opsmatters/opsmatters-core/contributors) who participated in this project.

## License

This project is licensed under the terms of the [Apache license 2.0](https://www.apache.org/licenses/LICENSE-2.0.html).

<sub>Copyright (c) 2018 opsmatters</sub>