(ns offcourse.router.eventful
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [shared.protocols.eventful :as ef]
            [shared.models.route.index :as route]
            [shared.models.action.index :as action]
            [shared.protocols.convertible :as cv]))

(defn handle-request [rt {:keys [handler route-params]}]
  (ef/respond rt [:refreshed (route/create handler route-params)]))

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
