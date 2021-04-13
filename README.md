# duct.module.datomic [![CircleCI](https://circleci.com/gh/hden/duct.module.datomic/tree/master.svg?style=svg)](https://circleci.com/gh/hden/duct.module.datomic/tree/master)

A Duct module that adds Integrant keys for a Datomic database connection and Ragtime migrations to a configuration.

## Installation

To install, add the following to your project `:dependencies`:

`[hden/duct.module.datomic "0.5.1"]`

## Usage

To add this module to your configuration, add the :duct.module/sql key to your config.edn file:

```clojure
:duct.module/datomic {:server-type :ion
                      :region      "us-east-1"
                      :system      "mbrainz-stu"
                      :endpoint    "http://entry.mbrainz-stu.us-east-1.datomic.net:8182"
                      :proxy-port  8182
                      :database    "datomic-docs-tutorial"}

;; Read configurations from the AWS SSM
;; See https://docs.datomic.com/cloud/ions/ions-reference.html#parameters-example
[:duct.reader.ion/get-params :my-app.params/my-param]
{:template "/datomic-shared{/env}{/app-name}/my-param"
 :logger   #ig/ref :duct/logger
 :default  "foobar"}
```

### dev-local

You can develop and test applications with minimal connectivity and setup by using the dev-local tool. Override the connection by setting the `:duct.database/datomic` key in the `dev.edn`.

See https://docs.datomic.com/cloud/dev-local.html

```clojure
:duct.database/datomic
{:server-type :dev-local
 :storage-dir :mem
 :system      "mbrainz-stu"
 :database    "datomic-docs-tutorial"}
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
