(ns duct.logger.timbre.cast
  (:require [datomic.ion.cast :as cast]
            [integrant.core :as ig]))

(defn- logger-fn [{:keys [level output_ ?err error-level? context]}]
  (let [output (force output_)
        data (cond-> {:msg output
                      ::level level}
               ?err (assoc :ex ?err)
               context (assoc ::context context))]
    (if error-level?
      (cast/alert data)
      (cast/event data))))

(defmethod ig/init-key :duct.logger.timbre/cast [_ options]
  (merge {:enabled?   true
          :async?     false
          :min-level  :error
          :rate-limit nil
          :output-fn  :inherit
          :fn logger-fn}
         options))
