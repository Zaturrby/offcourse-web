(ns offcourse.views.checkpoint
  (:require [offcourse.views.components.card :refer [card]]
            [offcourse.views.components.viewer :refer [viewer]]
            [offcourse.views.containers.dashboard :refer [dashboard]]
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
                                 (dc/decorate user-name checkpoint-data routes)))
   :checkpoint      (fnk [appstate
                          course
                          checkpoint-data]
                         (when course
                           (if checkpoint-data
                             (qa/get course checkpoint-data)
                             (first (:checkpoints course)))))
   :resource        (fnk [appstate
                          checkpoint]
                         (when checkpoint (qa/get appstate checkpoint)))
   :view-actions         (fnk [] #{})
   :main            (fnk [checkpoint
                          resource]
                         (viewer {:resource resource} nil nil))
   :dashboard       (fnk [user-name
                          course
                          actions]
                         (when course
                           (dashboard {:main (card course)})))})
