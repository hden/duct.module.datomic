(ns duct.module.datomic-test
  (:require [clojure.test :refer :all]
            [duct.core :as core]
            [duct.module.datomic :as module-datomic]
            [integrant.core :as ig]))

(core/load-hierarchy)

(def base-config {:duct.module/datomic {:server-type :ion}})

(deftest module-test
  (testing "blank config"
    (is (= {:duct.database/datomic
            {:server-type :ion
             :duct.core/requires #{}}

            :duct.migrator.ragtime/datomic
            {:database   (ig/ref :duct.database/datomic)
             :logger     (ig/ref :duct/logger)
             :migrations []}}
           (core/build-config base-config))))

  (testing "config with existing data"
    (let [config (assoc base-config
                        :duct.profile/base
                        {:duct.migrator.ragtime/datomic
                         {:migrations [::foo]}})]
      (is (= ::foo
             (get-in (core/build-config config)
                     [:duct.migrator.ragtime/datomic
                      :migrations
                      0]))))))
