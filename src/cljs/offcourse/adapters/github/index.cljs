(ns offcourse.adapters.github.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.adapters.github.fetch :as fetch-impl]
            [shared.protocols.queryable :as qa :refer [Queryable]]))

(defrecord Github [name repository resources base-url]
  Lifecycle
  (start [db] db)
  (stop  [db] db)
  Queryable
  (-fetch [db query] (fetch-impl/fetch db query)))

(defn create [config]
  (map->Github config))
