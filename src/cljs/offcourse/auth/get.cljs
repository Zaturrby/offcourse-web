(ns offcourse.auth.get
  (:refer-clojure :exclude [get])
  (:require [cljs.core.async :refer [>! chan]]
            [shared.protocols.loggable :as log])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn unmarshal [string] (.parse js/JSON string))

(defn get-local-token [_]
  (when-let [token (.getItem js/localStorage "auth-token")]
    token))

(defn get-local-profile [_]
  (when-let [auth-profile (-> js/localStorage
                              (.getItem "auth-profile"))]
    (js->clj (unmarshal auth-profile ):keywordize-keys true)))
