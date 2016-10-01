(ns offcourse.router.react
  (:require [pushy.core :as pushy]
            [shared.protocols.convertible :as cv]
            [shared.protocols.specced :as sp]
            [shared.protocols.loggable :as log]
            [shared.protocols.actionable :as ac]))

(defmulti react (fn [_ [event-type _]] event-type))

(defmethod react :refreshed [{:keys [history routes url-helpers responses]} [_ payload]]
  (let [{:keys [type] :as viewmodel} (-> payload :viewmodel)
        old-url (pushy/get-token history)
        new-url (-> viewmodel cv/to-route (cv/to-url routes))]
    (when-not (= old-url new-url)
      (pushy/replace-token! history new-url))))

(defmethod react :requested [rt [_ action]]
  (ac/perform rt action))
