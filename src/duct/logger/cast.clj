(ns duct.logger.cast
  (:require [datomic.ion.cast :as cast]
            [duct.logger :as logger]
            [integrant.core :as ig]))

(defrecord CastLogger []
  logger/Logger
  (-log [_ level ns-str file line id event data]
    (let [f (case level
              :fatal cast/alert
              :error cast/alert
              :debug cast/dev
              :trace cast/dev
              cast/event)
          m {::level  level
             ::ns-str ns-str
             ::file   file
             ::line   line
             ::id     id}]
      (cond
        (instance? Throwable event)
        (f (merge m {:msg "Throwable" :ex event}))
        (instance? Throwable data)
        (f (merge m {:msg (pr-str event) :ex data}))
        (nil? data)
        (f (merge m {:msg (pr-str event)}))
        :else
        (f (merge m {:msg (pr-str event) ::data data}))))))

(defmethod ig/init-key :duct.logger/cast [_ {:keys [redirect]}]
  (when redirect
    (cast/initialize-redirect redirect))
  (->CastLogger))
