(ns duct.reader.ion
  (:require [datomic.ion :as ion]
            [integrant.core :as ig]
            [uritemplate-clj.core :as templ]))

(def get-params
  (memoize (fn [{:keys [template variables default]
                 :or {variables {}
                      default {}}}]
             (let [vars {:app-name (get (ion/get-app-info) :app-name)
                         :env      (get (ion/get-env) :env)}
                   params (ion/get-params {:path (templ/uritemplate template (merge vars variables))})]
               (if (= {} params)
                 default
                 params)))))

(defmethod ig/init-key ::get-params [_ opts]
  (get-params opts))
