(ns offcourse.ui.render
  (:require [medley.core :as medley]
            [shared.models.view.index :as view]
            [shared.protocols.renderable :as rr]
            [shared.protocols.eventful :as ef]
            [rum.core :as rum]
            [shared.protocols.specced :as sp]
            [cljs.spec :as spec]
            [shared.specs.core :as specs]
            [shared.protocols.loggable :as log]))

(defn -render [view-graph element]
  (let [{:keys [container] :as composition} view-graph
        rendered-view (container composition)]
    (rum/mount rendered-view
               (. js/document (querySelector element)))))

(defmulti render (fn [_ [_ payload :as event]] (sp/resolve event)))

(defmethod render [:refreshed :data] [{:keys [views container routes] :as rd}
                             [_ payload]]
  (let [view       (view/create {:appstate    payload
                                 :responder   (partial ef/respond rd)
                                 :routes      routes})
        view-graph (rr/render view views)
        viewmodel  (:viewmodel view-graph)
        actions    (:actions view-graph)]
    (-render view-graph container)
    (ef/respond rd [:rendered nil])))

(defmethod render :default [_ _] nil)
