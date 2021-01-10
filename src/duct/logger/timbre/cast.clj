(ns duct.logger.timbre.cast
  (:require [clojure.string :as str]
            [datomic.ion.cast :as cast]
            [integrant.core :as ig]))

(defn- logger-fn [{:keys [level msg_ timestamp_ error-level? context
                          ?err ?file ?line]}]
  (let [ts (force timestamp_)
        level (str/upper-case (name level))
        data (cond-> {:msg (force msg_)
                      ::level level}
               ts      (assoc ::timestamp ts)
               ?err    (assoc :ex ?err)
               ?file   (assoc ::file ?file)
               ?line   (assoc ::line ?line)
               context (assoc ::context context))]
    (if error-level?
      (cast/alert data)
      (cast/event data))))

(defmethod ig/init-key :duct.logger.timbre/cast [_ options]
  (merge {:enabled?   true
          :async?     false
          :min-level  :info
          :rate-limit nil
          :output-fn  :inherit
          :fn logger-fn}
         options))
