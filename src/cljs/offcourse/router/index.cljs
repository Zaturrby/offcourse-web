(ns offcourse.router.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [shared.protocols.eventful :as ef :refer [Eventful]]
            [offcourse.router.react :as react-impl]
            [offcourse.router.eventful :as ef-impl]))

(defrecord Router []
  Lifecycle
  (start [rt] (ef/-listen rt))
  (stop [rt] (ef/mute rt))
  Eventful
  (-listen [rt] (ef-impl/listen rt))
  (-react [rt event] (react-impl/react rt event))
  (-mute [rt] (ef-impl/mute rt))
  (-respond [rt event] nil))

(defn create [name] (-> {:component-name name}
                        map->Router))
