(ns offcourse.views.new-course
  (:require [offcourse.views.components.course-form :refer [course-form]]
            [plumbing.core :refer-macros [fnk]]
            [shared.protocols.loggable :as log]))

(def graph
  {:empty-course   (fnk [] {:course-id "some-hash"})
   :course         (fnk [] {:goal "Test Course"
                            :checkpoints [{:checkpoint-id 1233234
                                           :task "First task"
                                           :resource-url "some url"}
                                          {:checkpoint-id 1233234231242
                                           :task "Second task"
                                           :resource-url "some second url"}]})
   :view-actions   (fnk [] #{})
   :main           (fnk [course respond] (course-form {:course course} respond))
   :dashboard      (fnk [] nil)})
