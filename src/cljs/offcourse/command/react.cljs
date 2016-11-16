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
(defmethod react :go [])
(defmethod react :sign-out [])

(defmethod react :sign-in
  [{:keys [component-name adapter] :as service} [_ action :as event]]
  (go
    (let [auth-token (some-> event meta :credentials :auth-token)
          request (ac/request adapter (with-meta action {:auth-token auth-token}))
          {:keys [accepted denied]} (async/<! request)]
      (when accepted (ef/respond service [:signed-in (-> {:user-name "charlotte"} payload/create)])))))
