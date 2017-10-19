Ops4j base
----------

Utility classes and extensions common to several ops4j projects.
PLease find the documentation in the [ops4j base wiki](https://ops4j1.jira.com/wiki/spaces/base).

Issues
------

[Jira BASE](https://ops4j1.jira.com/projects/BASE)

Build
-----

    mvn clean install

Modules
-------

In order to minimize dependencies and improve modularity, the Base project is split into very fine-grained modules.

* exec — Running a Java class in a separate process
* io — Handling streams and files
* lang — java.lang extensions
* monitors — monitoring
* net — network access/usage
* spi Utilities for obtaining services via the Java SE 6 ServiceLoader
* store — Utilities for storing and retrieving data from an InputStream.
* util — Utilities for environment, i18n and mime types
* util-collections — Utilities for collections
* util-property — Resolving properties from different sources
* util-xml — XML parsing and access
* ops4j-base — the über-bundle with all from OPS4J Base
