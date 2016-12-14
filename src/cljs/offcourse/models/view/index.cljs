(ns offcourse.models.view.index
  (:require [plumbing.graph :as graph]
            [rum.core :as rum]
            [shared.protocols.renderable :refer [Renderable]]
            [shared.protocols.specced :as sp]
            [shared.protocols.loggable :as log]))

(defrecord View []
  Renderable
  (-render [view views]
    (let [view-type (-> view :appstate :viewmodel sp/resolve)]
      ((graph/compile (view-type views)) view))))

(defn create [view-data]
  (map->View view-data))
