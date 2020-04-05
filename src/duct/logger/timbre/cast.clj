(ns duct.logger.timbre.cast
  (:require [datomic.ion.cast :as cast]
            [integrant.core :as ig]))

(defn- logger-fn [{:keys [output_ ?err error-level? context]}]
  (when error-level?
    (let [output (force output_)
          data (cond-> {:msg output}
                 ?err (assoc :ex ?err)
                 context (assoc ::context context))]
      (cast/alert data))))

(defmethod ig/init-key :duct.logger.timbre/cast [_ options]
  (merge {:enabled?   true
          :async?     false
          :min-level  :error
          :rate-limit nil
          :output-fn  :inherit
          :fn logger-fn}
         options))
