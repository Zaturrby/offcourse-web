(ns offcourse.adapters.github.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.adapters.github.send :as send-impl]
            [shared.protocols.eventful :as ef :refer [Eventful]]))

(defrecord Github [name repository resources base-url]
  Lifecycle
  (start [db] db)
  (stop  [db] db)
  Eventful
  (-send [db event] (send-impl/send db event)))

(defn create [config]
  (map->Github config))
