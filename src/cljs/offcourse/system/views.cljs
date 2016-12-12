(ns offcourse.system.views
  (:require [offcourse.views.base :as bv]
            [offcourse.views.checkpoint :as cpv]
            [offcourse.views.collection :as clv]
            [offcourse.views.new-course :as ncv]))

(def views
  {:collection-view (merge bv/graph clv/graph)
   :new-course-view (merge bv/graph ncv/graph)
   :checkpoint-view (merge bv/graph cpv/graph)})
