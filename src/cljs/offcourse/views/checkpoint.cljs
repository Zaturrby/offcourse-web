(ns offcourse.views.checkpoint
  (:require [offcourse.views.components.card :refer [card]]
            [offcourse.views.components.viewer :refer [viewer meta-widget]]
            [offcourse.views.components.button :refer [button]]
            [offcourse.views.components.edit-modal :refer [edit-modal]]
            [offcourse.views.containers.dashboard :refer [dashboard]]
            [offcourse.views.containers.overlay :refer [overlay]]
            [plumbing.core :refer-macros [fnk]]
            [shared.protocols.decoratable :as dc]
            [shared.protocols.queryable :as qa]
            [clojure.set :as set]
            [shared.protocols.loggable :as log]))

(def lorem "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod
  tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
  quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo
  consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
  cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non
  proident, sunt in culpa qui officia deserunt mollit anim id est laborum.

  Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod
  tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
  quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo
  consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
  cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non
  proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")

(def fake-resource
  {:content lorem
   :title "Fake Title"})

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
   :view-actions    (fnk [] #{:update :fork :switch-to})
   :main            (fnk [checkpoint
                          resource]
                         (if (and resource checkpoint)
                            (viewer {:resource resource}
                                    :checkpoint checkpoint nil nil)
                            (viewer {:resource fake-resource
                                     :checkpoint checkpoint nil nil})))
   :dashboard       (fnk [course
                          respond]
                         (when course
                           (dashboard {:main (card course respond)
                                       :edit-button (button "Edit this course"
                                                     (partial respond [:switch-to :edit-mode]))})))
   :overlay         (fnk [course
                          respond
                          app-mode]
                         (when (and course (= app-mode :edit-mode))
                           (overlay {:edit-modal (edit-modal {:course course} respond)})))})
