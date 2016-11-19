(ns offcourse.router.eventful
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [shared.protocols.eventful :as ef]
            [offcourse.models.route.index :as route]
            [shared.models.action.index :as action]
            [shared.protocols.convertible :as cv]
            [offcourse.system.service :as service]
            [shared.protocols.loggable :as log]))

(defn handle-request [rt {:keys [handler route-params]}]
  (service/respond rt [:refreshed (route/from-params handler route-params)]))

(defn restart [{:keys [history] :as rt}]
  (pushy/replace-token! history "/"))

(defn listen [{:keys [routes responses] :as rt}]
  (let [history (pushy/pushy (partial handle-request rt)
                             (partial bidi/match-route routes))
        rt (assoc rt :history history)]
    (pushy/start! history)
    (service/listen rt)))

(defn mute [{:keys [history listeners] :as rt}]
  (pushy/stop! history)
  (dissoc rt :listeners))
