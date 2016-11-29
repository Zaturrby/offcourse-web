(ns offcourse.ui.render
  (:require [rum.core :as rum]
            [offcourse.models.view.index :as view]
            [shared.protocols.eventful :as ef]
            [shared.protocols.renderable :as rr]
            [shared.protocols.loggable :as log]))

(defn -render [view-graph element]
  (let [{:keys [container] :as composition} view-graph
        rendered-view (container composition)]
    (rum/mount rendered-view
               (. js/document (querySelector element)))))

(defmulti render (fn [_ [event-type _]] event-type))

(defmethod render :refreshed [{:keys [views container routes] :as rd}
                              [_ payload]]
  (let [view       (view/create {:appstate    payload
                                 :responder   (partial ef/respond rd)
                                 :routes      routes})
        view-graph (rr/render view views)]
    (log/log "UI Render")
    (log/log view-graph)
    (-render view-graph container)
    (ef/respond rd [:rendered])))

(defmethod render :default [_ _] nil)
