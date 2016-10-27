(ns offcourse.command.react
  (:require [shared.protocols.eventful :as ef]
            [shared.protocols.specced :as sp]
            [shared.protocols.queryable :as qa]
            [shared.models.payload.index :as payload]
            [shared.protocols.convertible :as cv]
            [cljs.core.async :as async]
            [shared.protocols.loggable :as log]
            [shared.protocols.actionable :as ac])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defmulti react (fn [_ [_ action]] (sp/resolve action)))

(defmethod react :authenticate [])

(defmethod react :sign-in
  [{:keys [component-name adapter] :as service} [_ action :as event]]
  (let [auth-token (some-> event meta :credentials :auth-token)]
    (go
      (log/log "R" (async/<! (ac/request adapter (with-meta action {:auth-token auth-token})))))))
