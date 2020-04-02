(defproject hden/duct.module.datomic "0.1.0-SNAPSHOT"
  :description "A Duct module that adds Integrant keys for a Datomic database connection and Ragtime migrations to a configuration."
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [duct/core "0.8.0"]
                 [duct/logger "0.3.0"]
                 [ragtime.datomic "0.1.0"]]
  :repl-options {:init-ns duct.module.datomic}
  :profiles
  {:dev {:dependencies [[com.gearswithingears/shrubbery "0.4.1"]
                        [com.datomic/client-cloud "0.8.83"]]}})
