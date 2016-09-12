(ns offcourse.router.eventful
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [shared.models.viewmodel.index :as viewmodel]
            [shared.protocols.eventful :as ef]
            [shared.models.action.index :as action]))

(defn handle-request [rt {:keys [handler route-params]}]
  (ef/respond rt [:requested (action/create [:update (viewmodel/create handler route-params)])]))

(defn restart [{:keys [history] :as rt}]
  (pushy/replace-token! history "/"))

(defn listen [{:keys [routes responses] :as rt}]
  (let [history (pushy/pushy (partial handle-request rt)
                             (partial bidi/match-route routes))
        rt (assoc rt :history history)]
    (pushy/start! history)
    (ef/listen rt)))

(defn mute [{:keys [history listeners] :as rt}]
  (pushy/stop! history)
  (dissoc rt :listeners))
