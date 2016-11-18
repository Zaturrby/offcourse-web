(ns offcourse.ui.index
  (:require [com.stuartsierra.component :refer [Lifecycle]]
            [shared.protocols.renderable :as rr :refer [Renderable]]
            [shared.protocols.eventful :as ef :refer [Eventful]]
            [offcourse.system.service :as service]
            [offcourse.ui.render :as render-impl]))

(defrecord UI []
  Lifecycle
  (start [rd] (service/listen rd))
  (stop [rd] (service/mute rd))
  Renderable
  (-render [rd query] (render-impl/render rd query))
  Eventful
  (-listen [rd] (service/listen rd))
  (-react [rd event] (rr/render rd event))
  (-mute [rd] (dissoc rd :listener))
  (-respond [rd event] (service/respond rd event)))

(defn create [name] (-> {:component-name name
                         :container "#container"}
                        map->UI))
