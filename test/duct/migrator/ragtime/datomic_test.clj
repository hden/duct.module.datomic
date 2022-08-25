(ns duct.migrator.ragtime.datomic-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [datomic.client.api.protocols :as client-protocols]
            [datomic.client.api.impl :as client-impl]
            [shrubbery.core :as shrubbery]
            [duct.core :as duct]
            [duct.logger :as logger]
            [duct.database.datomic :as database-datomic]
            [duct.migrator.ragtime.datomic :as migrator]
            [integrant.core :as ig]))

(duct/load-hierarchy)

(defrecord TestLogger [logs]
  logger/Logger
  (-log [_ level ns-str file line id event data]
    (swap! logs conj [event data])))

(def logs (atom []))

(defn- reset-test-state [f]
  (reset! logs [])
  (f))

(use-fixtures :each reset-test-state)

(defn create-mocks []
  (let [db (shrubbery/mock client-protocols/Db client-impl/Queryable)
        connection (shrubbery/spy (reify client-protocols/Connection
                                    (db [_] db)
                                    (transact [_ arg-map])))
        client (shrubbery/mock client-protocols/Client)]
    {:db db
     :connection connection
     :client client}))

(deftest migration-test
  (testing "default migrations attribute"
    (let [{:keys [connection client]} (create-mocks)]
      (ig/init {:duct.migrator.ragtime/datomic
                {:database   (database-datomic/map->Boundary {:client client :connection connection})
                 :logger     (->TestLogger logs)
                 :migrations [(ig/ref ::create-inventory)]}

                [::migrator/migration ::create-inventory]
                {:tx-data [{:db/ident :inv/sku
                            :db/valueType :db.type/string
                            :db/unique :db.unique/identity
                            :db/cardinality :db.cardinality/one}]}})
      (is (shrubbery/received? connection client-protocols/transact))
      (is (= @logs
             [[::migrator/applying ::create-inventory]])))))

(deftest change-and-resume-test
  (let [{:keys [connection client]} (create-mocks)
        config {:duct.migrator.ragtime/datomic
                {:database   (database-datomic/map->Boundary {:client client :connection connection})
                 :logger     (->TestLogger logs)
                 :migrations [(ig/ref ::create-inventory)]}

                [::migrator/migration ::create-inventory]
                {:tx-data [{:db/ident :inv/sku
                            :db/valueType :db.type/string
                            :db/unique :db.unique/identity
                            :db/cardinality :db.cardinality/one}]}}
        system (ig/init config)
        config' (assoc (assoc-in config
                                 [:duct.migrator.ragtime/datomic :migrations]
                                 [(ig/ref ::create-inventory)
                                  (ig/ref ::create-inventory2)])
                       [::migrator/migration ::create-inventory2]
                       {:tx-data [{:db/ident :inv/color
                                   :db/valueType :db.type/keyword
                                   :db/cardinality :db.cardinality/one}]})]
   (ig/resume config' system)
   (is (shrubbery/received? connection client-protocols/transact))
   (is (= (into #{} @logs)
          #{[::migrator/applying ::create-inventory]
            [::migrator/applying ::create-inventory2]}))))
