(ns offcourse.adapters.aws.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.adapters.aws.send :as send-impl]
            [shared.protocols.eventful :as ef :refer [Eventful]]))

(defrecord AWS [name resources endpoint]
  Lifecycle
  (start [db] db)
  (stop  [db] db)
  Eventful
  (-send [db event] (send-impl/send db event)))

(defn create [config]
  (map->AWS config))
