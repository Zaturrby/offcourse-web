(ns offcourse.system.views
  (:require [offcourse.views.base :as bv]
            [offcourse.views.checkpoint :as cpv]
            [offcourse.views.signup :as su]
            [offcourse.views.collection :as clv]
            [offcourse.views.new-course :as ncv]))

(def views
  {:signup-view     (merge bv/graph su/graph)
   :collection-view (merge bv/graph clv/graph)
   :new-course-view (merge bv/graph {:view-actions #{}})
   :course-view     (merge bv/graph cpv/graph)
   :checkpoint-view (merge bv/graph cpv/graph)
   :new-course-view (merge bv/graph ncv/graph)})
