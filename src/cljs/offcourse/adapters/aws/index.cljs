(ns offcourse.adapters.aws.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.adapters.aws.fetch :refer [fetch]]
            [offcourse.adapters.aws.request :refer [request]]
            [shared.protocols.queryable :as ef :refer [Queryable]]
            [shared.protocols.actionable :refer [Actionable]]
            [shared.protocols.loggable :as log]))

(defrecord AWS [name resources endpoint]
  Lifecycle
  (start [adapter] adapter)
  (stop  [adapter] adapter)
  Actionable
  (-request [adapter action] (request adapter action))
  Queryable
  (-fetch [adapter query] (fetch adapter query)))

(defn create [config]
  (map->AWS config))
