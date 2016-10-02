(ns offcourse.adapters.aws.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.adapters.aws.fetch :refer [fetch]]
            [shared.protocols.queryable :as ef :refer [Queryable]]))

(defrecord AWS [name resources endpoint]
  Lifecycle
  (start [db] db)
  (stop  [db] db)
  Queryable
  (-fetch [db query] (fetch db query)))

(defn create [config]
  (map->AWS config))
