(ns offcourse.command.index
  (:require [com.stuartsierra.component :as lc :refer [Lifecycle]]
            [offcourse.command.react :as react]
            [shared.protocols.eventful :refer [Eventful]]
            [offcourse.system.service :as service]))

(defrecord Command []
  Lifecycle
  (start [service] (service/listen service))
  (stop [service] (service/mute service))
  Eventful
  (-respond [service event] (service/respond service event))
  (-react [service event] (react/react service event))
  (-mute [service] (service/mute service))
  (-listen [service] (service/listen service)))

(defn create [name] (-> {:component-name name}
                        map->Command))
