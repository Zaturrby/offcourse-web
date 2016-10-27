(ns offcourse.conductor.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.conductor.react :as react-impl]
            [offcourse.conductor.perform :as perform-impl]
            [shared.protocols.actionable :refer [Actionable]]
            [shared.protocols.eventful :as ef :refer [Eventful]]))

(defrecord Conductor []
  Lifecycle
  (start   [as] (ef/listen as))
  (stop    [as] (ef/mute as))
  Actionable
  (-perform [as action] (perform-impl/perform as action))
  Eventful
  (-respond [as event] (ef/respond as event))
  (-mute [as] (ef/mute as))
  (-react [as event] (react-impl/react as event))
  (-listen [as] (ef/listen as)))

(defn create [name] (-> {:component-name name}
                        map->Conductor))
