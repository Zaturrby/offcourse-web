(ns offcourse.views.new-course
  (:require [offcourse.views.components.new-course :refer [new-course]]
            [plumbing.core :refer-macros [fnk]]
            [shared.protocols.loggable :as log]))

(def graph
  {:empty-course   (fnk [] {:course-id "some-hash"})
   :view-actions (fnk [] #{})
   :main         (fnk [empty-course respond] (new-course empty-course respond))
   :dashboard    (fnk [] nil)})
