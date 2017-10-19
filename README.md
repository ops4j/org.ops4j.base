Ops4j base
----------

Utility classes and extensions common to several ops4j projects.
PLease find the documentation in the [ops4j base wiki](https://ops4j1.jira.com/wiki/spaces/base).

Issues
------

[Jira BASE](https://ops4j1.jira.com/wiki/spaces/base/overview)

Build
-----

    mvn clean install

Modules
-------

In order to minimize dependencies and improve modularity, the Base project is split into very fine-grained modules.

* [exec](ops4j-base-exec) — Running a Java class in a separate process
* [io](ops4j-base-io) — Handling streams and files
* [lang](ops4j-base-lang) — java.lang extensions
* [monitors](ops4j-base-monitors) — monitoring
* [net](ops4j-base-net) — network access/usage
* [spi](ops4j-base-spi) Utilities for obtaining services via the Java SE 6 ServiceLoader
* [store](ops4j-base-store) — Utilities for storing and retrieving data from an InputStream.
* [util](ops4j-base-util) — Utilities for environment, i18n and mime types
* [util-collections](ops4j-base-util-collections) — Utilities for collections
* [util-property](ops4j-base-util-property) — Resolving properties from different sources
* [util-xml](ops4j-base-util-xml) — XML parsing and access
* [ops4j-base](ops4j-base) — the über-bundle with all from OPS4J Base
