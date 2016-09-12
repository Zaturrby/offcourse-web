(ns offcourse.appstate.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.appstate.react :as react-impl]
            [offcourse.appstate.perform :as perform-impl]
            [shared.protocols.actionable :refer [Actionable]]
            [shared.protocols.eventful :as ef :refer [Eventful]]))

(defrecord Appstate []
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
                        map->Appstate))
