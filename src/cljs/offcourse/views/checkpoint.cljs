(ns offcourse.views.checkpoint
  (:require [offcourse.views.components.button :refer [button]]
            [offcourse.views.components.card :refer [card]]
            [offcourse.views.components.course-form :refer [course-form]]
            [offcourse.views.components.viewer :refer [viewer]]
            [offcourse.views.containers.dashboard :refer [dashboard]]
            [offcourse.views.containers.overlay :refer [overlay]]
            [plumbing.core :refer-macros [fnk]]
            [shared.protocols.decoratable :as dc]
            [shared.protocols.queryable :as qa]
            [shared.protocols.loggable :as log]))

(def graph
  {:view-actions    (fnk [] #{:update :fork :switch-to})
   :checkpoint-data (fnk [viewmodel] (some-> viewmodel :checkpoint))
   :course-data     (fnk [viewmodel] (-> viewmodel :course))
   :course          (fnk [appstate course-data routes]
                         (some-> appstate
                                 (qa/get course-data)
                                 (dc/decorate appstate routes)))
   :checkpoint      (fnk [appstate course checkpoint-data]
                         (when course
                           (if checkpoint-data
                             (qa/get course checkpoint-data)
                             (first (:checkpoints course)))))
   :resource        (fnk [appstate course checkpoint]
                         (let [query {:course-id    (:course-id course)
                                      :resource-url (:resource-url checkpoint)}]
                           (when checkpoint (qa/get appstate query))))
   :main            (fnk [course checkpoint resource]
                         (when (and checkpoint course)
                           (viewer {:resource   resource
                                    :checkpoint checkpoint})))
   :dashboard       (fnk [course respond]
                         (let [editable? (-> course meta :affordances :editable?)]
                           (when course
                             (dashboard {:main     (card course respond)
                                         :controls (when editable? (button {:button-text "Edit"
                                                                            :button-width "full"}
                                                                           #(respond [:switch-to :edit-mode])))}))))
   :view-overlays   (fnk [course respond]
                         {:edit-mode (overlay (course-form {:course course} respond) respond)})})
