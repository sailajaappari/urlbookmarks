(ns urlbookmarks.dev
  (:require [urlbookmarks.core :as core]))

(enable-console-print!)

(defn on-jsload []
  (core/main))
