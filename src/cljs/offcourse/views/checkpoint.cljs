(ns offcourse.views.checkpoint
  (:require [offcourse.views.components.card :refer [card]]
            [offcourse.views.components.viewer :refer [viewer meta-widget]]
            [offcourse.views.components.button :refer [button]]
            [offcourse.views.components.edit-modal :refer [edit-modal]]
            [offcourse.views.containers.dashboard :refer [dashboard]]
            [offcourse.views.containers.checkpoint-content :refer [checkpoint-content]]
            [offcourse.views.containers.overlay :refer [overlay]]
            [plumbing.core :refer-macros [fnk]]
            [shared.protocols.decoratable :as dc]
            [shared.protocols.queryable :as qa]
            [clojure.set :as set]
            [shared.protocols.loggable :as log]))

(def graph
  {:checkpoint-data (fnk [viewmodel] (some-> viewmodel :checkpoint))
   :course-data     (fnk [viewmodel] (-> viewmodel :course))
   :course          (fnk [appstate
                          course-data
                          checkpoint-data
                          routes
                          user-name]
                         (some-> appstate
                                 (qa/get course-data)
                                 (dc/decorate appstate routes)))
   :checkpoint      (fnk [appstate
                          course
                          checkpoint-data]
                         (when course
                           (if checkpoint-data
                             (qa/get course checkpoint-data)
                             (first (:checkpoints course)))))
   :resource        (fnk [appstate
                          course
                          checkpoint]
                         (let [query {:course-id (:course-id course)
                                      :resource-url (:resource-url checkpoint)}]
                           (when checkpoint (qa/get appstate query))))
   :view-actions    (fnk [] #{:update :fork :start :stop})
   :main            (fnk [checkpoint
                          course
                          #_resource] 
                         (when checkpoint 
                           (checkpoint-content 
                            {:viewer (viewer {:resource {:title "Temporary Title" :content "Lorem"}} nil nil)
                             :meta-widget (meta-widget checkpoint course)})))
   :dashboard       (fnk [course
                          respond]
                         (when course
                           (dashboard {:main (card course respond)
                                       :edit-button (button "Edit this course" 
                                                     (partial respond [:start :edit-mode]))})))
   :overlay         (fnk [course]
                         (when (and course true)
                           (overlay {:edit-modal (edit-modal {:course course})})))})