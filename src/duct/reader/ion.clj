(ns duct.reader.ion
  (:require [datomic.ion :as ion]
            [integrant.core :as ig]
            [uritemplate-clj.core :as templ]))

(def get-params
  (memoize (fn
             ([s] (get-params s {}))
             ([s m]
              (let [vars {:app-name (get (ion/get-app-info) :app-name)
                          :env      (get (ion/get-env) :env)}]
                (ion/get-params {:path (templ/uritemplate s (merge vars m))}))))))

(defmethod ig/init-key ::get-params [_ {:keys [template variables]}]
  (get-params template (or variables {})))
