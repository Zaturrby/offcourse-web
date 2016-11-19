(ns offcourse.system.service
  (:require [cljs.core.async :as async]
            [shared.models.event.index :as event]
            [shared.protocols.eventful :as ef]
            [shared.protocols.loggable :as log]
            [shared.protocols.specced :as sp])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(defn- listener [{:keys [channels component-name triggers] :as this}]
  (go-loop []
    (let [[type :as event] (<! (:input channels))]
      (when (contains? (into #{} triggers) type) (ef/react this event))
      (recur))))

(defn respond [{:keys [channels component-name state] :as this} [status payload]]
  (let [credentials (when state (-> @state :user :credentials))
        payload     (with-meta payload (merge (meta payload) {:credentials credentials}))
        response    (event/create [component-name status payload])]
    (if (sp/valid? response)
      (async/put! (:output channels) #_response (log/pipe response))
      (log/error response (sp/errors response)))))

(defn listen [this]
  (assoc this :listener (listener this)))

(defn mute [{:keys [channels] :as this}]
  (do
    (async/close! (:input channels))
    (dissoc :listener this)))
