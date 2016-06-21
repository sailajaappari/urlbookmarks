(ns urlbookmarks.core
  (:require [urlbookmarks.server :as server])
  (:gen-class))


(defn start! []
  (server/start!))

(defn -main []
  (start!))
