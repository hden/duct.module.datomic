(ns duct.module.datomic
  (:require [duct.core :as core]
            [integrant.core :as ig]))

(defmethod ig/init-key :duct.module/datomic [_ options]
  (fn [config]
    (core/merge-configs
     config
     {:duct.database/datomic options
      :duct.logger/cast options
      :duct.migrator.ragtime/datomic
      ^:demote {:database   (ig/ref :duct.database/datomic)
                :logger     (ig/ref :duct/logger)
                :migrations []}})))
