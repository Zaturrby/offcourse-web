(ns offcourse.auth.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.auth.react :as react]
            [offcourse.auth.get :as get]
            [shared.protocols.eventful :as ef :refer [Eventful]]
            [offcourse.system.service :as service]
            [shared.models.credentials.index :as credentials]))

(defrecord Auth []
  Lifecycle
  (start [auth] (service/listen auth))
  (stop [auth] (service/mute auth))
  Eventful
  (-respond [auth event] (service/respond auth event))
  (-react [auth event] (react/react auth event))
  (-mute [auth] (service/mute auth))
  (-listen [auth] (service/listen auth)))

(defn create [name] (-> {:component-name name}
                        map->Auth))
