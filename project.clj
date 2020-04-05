(defproject hden/duct.module.datomic "0.1.0-SNAPSHOT"
  :description "A Duct module that adds Integrant keys for a Datomic database connection and Ragtime migrations to a configuration."
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :managed-dependencies [[com.datomic/client-cloud "0.8.80"]
                         [com.datomic/ion "0.9.35"]]
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.datomic/client-cloud]
                 [com.datomic/ion]
                 [duct/core "0.8.0"]
                 [duct/logger "0.3.0"]
                 [hden/duct.database.datomic "0.1.0"]
                 [ragtime.datomic "0.1.0"]]
  :repl-options {:init-ns duct.module.datomic}
  :repositories [["datomic-cloud" "s3://datomic-releases-1fc2183a/maven/releases"]]
  :profiles
  {:dev {:dependencies [[com.gearswithingears/shrubbery "0.4.1"]]}})
