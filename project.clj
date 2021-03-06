(defproject hden/duct.module.datomic "0.5.3"
  :description "A Duct module that adds Integrant keys for a Datomic database connection and Ragtime migrations to a configuration."
  :url "https://github.com/hden/duct.module.datomic"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :managed-dependencies [[com.datomic/client-cloud "0.8.113"]]
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [com.datomic/client-cloud]
                 [duct/core "0.8.0"]
                 [duct/logger "0.3.0"]
                 [hden/duct.database.datomic "0.2.0"]
                 [ragtime.datomic "0.2.0"]
                 [uritemplate-clj "1.3.0"]]
  :repl-options {:init-ns duct.module.datomic}
  :profiles
  {:dev {:dependencies [[com.gearswithingears/shrubbery "0.4.1"]]}})
