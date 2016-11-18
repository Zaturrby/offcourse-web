(ns offcourse.query.index
  (:require [com.stuartsierra.component :as lc :refer [Lifecycle]]
            [offcourse.query.react :as react]
            [shared.protocols.eventful :refer [Eventful]]
            [offcourse.system.service :as service]
            [shared.protocols.loggable :as log]))

(defrecord Query []
  Lifecycle
  (start [query] (service/listen query))
  (stop [query] (service/mute query))
  Eventful
  (-respond [query event] (service/respond query event))
  (-react [query event] (react/react query event))
  (-mute [query] (service/mute query))
  (-listen [query] (service/listen query)))

(defn create [name] (-> {:component-name name}
                        map->Query))
