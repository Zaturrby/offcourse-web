(ns offcourse.command.index
  (:require [com.stuartsierra.component :as lc :refer [Lifecycle]]
            [offcourse.command.react :as react]
            [shared.protocols.eventful :as ef :refer [Eventful]]))

(defrecord Command []
  Lifecycle
  (start [service] (ef/listen service))
  (stop [service] (ef/mute service))
  Eventful
  (-respond [service event] (ef/respond service event))
  (-react [service event] (react/react service event))
  (-mute [service] (ef/mute service))
  (-listen [service] (ef/listen service)))

(defn create [name] (-> {:component-name name}
                        map->Command))
