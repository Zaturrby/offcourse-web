(ns offcourse.auth.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.auth.react :as react]
            [offcourse.auth.get :as get]
            [shared.protocols.eventful :as ef :refer [Eventful]]
            [shared.models.credentials.index :as credentials]))

(defrecord Auth []
  Lifecycle
  (start [auth] (ef/listen auth))
  (stop [auth] (ef/mute auth))
  Eventful
  (-respond [auth event] (ef/respond auth event))
  (-react [auth event] (react/react auth event))
  (-mute [auth] (ef/mute auth))
  (-listen [auth] (ef/listen auth)))

(defn create [name] (-> {:component-name name}
                        map->Auth))
