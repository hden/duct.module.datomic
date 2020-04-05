(ns duct.module.datomic
  (:require [duct.core :as core]
            [integrant.core :as ig]
            [duct.core.merge :as merge]))

(defn- get-environment [config options]
  (:environment options (:duct.core/environment config :production)))

(def ^:private logger-configs
  {:production
   {:duct.logger.timbre/cast {}
    :duct.logger/timbre
    ^:demote {:appenders ^:displace {:duct.logger.timbre/cast (ig/ref :duct.logger.timbre/cast)}}}})

(defmethod ig/init-key :duct.module/datomic [_ options]
  (fn [config]
    (core/merge-configs
     config
     {:duct.database/datomic options}
     {:duct.migrator.ragtime/datomic
      ^:demote {:database   (ig/ref :duct.database/datomic)
                :logger     (ig/ref :duct/logger)
                :migrations []}}
     (logger-configs (get-environment config options)))))
