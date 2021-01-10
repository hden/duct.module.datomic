(ns duct.migrator.ragtime.datomic
  (:require [clojure.edn :as edn]
            [duct.logger :as logger]
            [integrant.core :as ig]
            [ragtime.core :as ragtime]
            [ragtime.datomic :as ragtime-datomic]
            [ragtime.reporter :as reporter]
            [ragtime.strategy :as strategy]
            [datomic.client.api.protocols :as client-protocols]))

(defprotocol StringSource
  (get-string [source]))

(extend-protocol StringSource
  String
  (get-string [s] s)
  java.net.URL
  (get-string [s] (slurp s))
  duct.core.resource.Resource
  (get-string [s] (slurp s)))

(defn- singularize [coll]
  (if (= (count coll) 1)
    (first coll)
    coll))

(defn- clean-key [base key]
  (if (vector? key)
    (singularize (remove #{base} key))
    key))

(defn logger-reporter [logger]
  (fn [_ op id]
    (case op
      :up   (logger/log logger :report ::applying id)
      :down (logger/log logger :report ::rolling-back id))))

(defn- get-database [{:keys [connection]}]
  {:pre [(satisfies? client-protocols/Connection connection)]}
  (ragtime-datomic/create-connection connection))

(defn- get-reporter [{:keys [logger]}]
  (if logger
    (logger-reporter logger)
    reporter/print))

(defn- migrate [index {:keys [migrations database] :as options}]
  (let [db (get-database database)
        rep (get-reporter options)]
    (ragtime/migrate-all db index migrations {:reporter rep
                                              :strategy strategy/ignore-future})
    (ragtime/into-index index migrations)))

(defmethod ig/init-key :duct.migrator.ragtime/datomic [_ options]
  (migrate {} options))

(defmethod ig/resume-key :duct.migrator.ragtime/datomic [_ options _ index]
  (migrate index options))

(defmethod ig/init-key ::edn [key {:keys [data] :as options}]
  (let [id (:id options (clean-key ::edn key))
        {:keys [tx-data]} (edn/read-string (get-string data))]
    (ragtime-datomic/create-migration id tx-data)))

(defmethod ig/init-key ::migration [key {:keys [tx-data] :as options}]
  (let [id (:id options (clean-key ::migration key))]
    (ragtime-datomic/create-migration id tx-data)))
