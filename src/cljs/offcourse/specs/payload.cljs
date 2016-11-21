(ns offcourse.specs.payload
  (:require [cljs.spec :as spec]))

(spec/def :payload/frontend (spec/or :resource        :offcourse/resource
                                     :course          :offcourse/course
                                     :courses         (spec/coll-of :offcourse/course)
                                     :resources       (spec/coll-of :offcourse/resource)))
