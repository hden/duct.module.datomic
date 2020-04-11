(ns duct.server
  (:require [integrant.core :as ig]))

(defmethod ig/init-key ::nop [_ options])
