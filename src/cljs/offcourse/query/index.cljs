(ns offcourse.query.index
  (:require [com.stuartsierra.component :as lc :refer [Lifecycle]]
            [offcourse.query.react :as react]
            [shared.protocols.eventful :as ef :refer [Eventful]]
            [shared.protocols.loggable :as log]))

(defrecord Query []
  Lifecycle
  (start [query] (ef/listen query))
  (stop [query] (ef/mute query))
  Eventful
  (-respond [query event] (ef/respond query event))
  (-react [query event] (react/react query event))
  (-mute [query] (ef/mute query))
  (-listen [query] (ef/listen query)))

(defn create [name] (-> {:component-name name}
                        map->Query))
