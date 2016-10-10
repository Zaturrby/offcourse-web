(ns offcourse.views.new-course
  (:require [offcourse.views.components.course-form :refer [course-form]]
            [plumbing.core :refer-macros [fnk]]
            [shared.protocols.loggable :as log]
            [shared.models.course.index :as co]))

(def graph
  {:empty-course   (fnk [] {:course-id "some-hash"})
   :course         (fnk [] (co/create {:goal "Test Course"}))
   :view-actions   (fnk [] #{})
   :main           (fnk [course respond] (course-form {:course course} respond))
   :dashboard      (fnk [] nil)})
