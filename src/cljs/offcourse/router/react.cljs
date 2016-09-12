(ns offcourse.router.react
  (:require [pushy.core :as pushy]
            [shared.protocols.convertible :as cv]
            [shared.protocols.specced :as sp]
            [shared.protocols.loggable :as log]
            [shared.protocols.actionable :as ac]))

(defmulti react (fn [_ [_ payload :as event]] (sp/resolve event)))

(defmethod react [:refreshed :data] [{:keys [history routes url-helpers responses]} [_ payload]]
  (let [{:keys [type] :as viewmodel} (-> payload :viewmodel)
        old-url (pushy/get-token history)
        new-url (cv/to-url viewmodel routes)]
    (when-not (= old-url new-url)
      (pushy/replace-token! history new-url))))

(defmethod react [:requested :action] [rt [_ action]]
  (ac/perform rt action))
