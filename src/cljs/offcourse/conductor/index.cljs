(ns offcourse.conductor.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.conductor.perform :as perform-impl]
            [offcourse.conductor.react :as react-impl]
            [offcourse.system.service :as service]
            [shared.protocols.actionable :refer [Actionable]]
            [shared.protocols.eventful :refer [Eventful]]))

(defrecord Conductor []
  Lifecycle
  (start   [as] (service/listen as))
  (stop    [as] (service/mute as))
  Actionable
  (-perform [as action] (perform-impl/perform as action))
  Eventful
  (-respond [as event] (service/respond as event))
  (-react [as event] (react-impl/react as event))
  (-mute [as] (service/mute as))
  (-listen [as] (service/listen as)))

(defn create [name] (-> {:component-name name}
                        map->Conductor))
