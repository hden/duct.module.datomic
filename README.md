# duct.module.datomic

A Duct module that adds Integrant keys for a Datomic database connection and Ragtime migrations to a configuration.

## Installation

To install, add the following to your project `:dependencies`:

`[hden/duct.module.datomic "0.1.0-SNAPSHOT"]`

## Usage

To add this module to your configuration, add the :duct.module/sql key to your config.edn file:

```clojure
:duct.module/datomic {:server-type :ion
                      :region "us-east-1"
                      :system "mbrainz-stu"
                      :endpoint "http://entry.mbrainz-stu.us-east-1.datomic.net:8182"
                      :proxy-port 8182
                      :database "datomic-docs-tutorial"}
```

### Integrant Keys

When prepped, the module will compile the following Integrant keys into your config:

```clojure
:duct.database/datomic {}
:duct.migrator.ragtime/datomic {:database #ig/ref :duct.database/datomic
                                :logger #ig/ref :duct/logger
                                :migrations []}
```

These defaults can be (selectively) overridden through custom `:duct.database/datomic` and `:duct.migrator.ragtime/datomic` keys in your duct profiles.

## License

Copyright © 2019 Haokang Den

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.
