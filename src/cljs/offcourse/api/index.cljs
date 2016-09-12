(ns offcourse.api.index
  (:require [com.stuartsierra.component :as lc :refer [Lifecycle]]
            [offcourse.api.react :as react]
            [offcourse.api.send :as send]
            [shared.protocols.eventful :as ef :refer [Eventful]]
            [shared.protocols.loggable :as log]))

(defrecord API []
  Lifecycle
  (start [api] (ef/listen api))
  (stop [api] (ef/mute api))
  Eventful
  (-respond [api event] (ef/respond api event))
  (-react [api event] (react/react api event))
  (-send [api event] (send/send api event))
  (-mute [api] (ef/mute api))
  (-listen [api] (ef/listen api)))

(defn create [name] (-> {:component-name name}
                        map->API))
