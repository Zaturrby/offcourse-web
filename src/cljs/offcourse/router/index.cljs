(ns offcourse.router.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [shared.protocols.eventful :as ef :refer [Eventful]]
            [shared.protocols.actionable :refer [Actionable]]
            [offcourse.router.react :as react-impl]
            [offcourse.router.perform :as perform-impl]
            [offcourse.router.eventful :as ef-impl]
            [offcourse.system.service :as service]
            [shared.protocols.loggable :as log]))

(defrecord Router []
  Lifecycle
  (start [rt] (ef/-listen rt))
  (stop [rt] (service/mute rt))
  Actionable
  (-perform [rt action] (perform-impl/perform rt action))
  Eventful
  (-listen [rt] (ef-impl/listen rt))
  (-react [rt event] (react-impl/react rt event))
  (-mute [rt] (ef-impl/mute rt))
  (-respond [rt event] (service/respond rt event)))

(defn create [name] (-> {:component-name name}
                        map->Router))
