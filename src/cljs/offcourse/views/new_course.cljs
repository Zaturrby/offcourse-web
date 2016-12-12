(ns offcourse.views.new-course
  (:require [offcourse.views.components.course-form :refer [course-form]]
            [offcourse.views.containers.overlay :refer [overlay]]
            [plumbing.core :refer-macros [fnk]]
            [shared.protocols.loggable :as log]
            [shared.models.course.index :as course-model]
            [shared.models.checkpoint.index :as cp-model]))

(def new-course {:checkpoints [{:checkpoint-id 0
                                :task "do something"
                                :resource-url "http://offcourse.io"}]})

(def graph
  {:view-actions    (fnk [] #{:update})
   :view-overlays   (fnk [] {})
   :course          (fnk []
                         (-> {}
                             course-model/map->Course
                             (with-meta {:spec :offcourse/course})))
   :main            (fnk [course respond]
                         (overlay (course-form {:course course} respond)))})
