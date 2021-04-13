(ns duct.reader.ion
  (:require [datomic.ion :as ion]
            [duct.logger :refer [log]]
            [integrant.core :as ig]
            [uritemplate-clj.core :as templ]))

(def get-params
  (memoize (fn [{:keys [template variables default logger]
                 :or {variables {}
                      default {}}}]
             (try
               (let [vars {:app-name (get (ion/get-app-info) :app-name)
                           :env      (get (ion/get-env) :env)}
                     params (ion/get-params {:path (templ/uritemplate template (merge vars variables))})]
                 ;; `ion/get-params` returns an empty map when the path does not exist.
                 (if (= {} params)
                   default
                   params))
               ;; Replace errors with default value
               (catch Throwable ex
                 (when logger
                   (log logger :warn ::failed-to-get-params ex))
                 default)))))

(defmethod ig/init-key ::get-params [_ opts]
  (get-params opts))
