(ns offcourse.specs.viewmodel
  (:require [cljs.spec :as spec]))

(spec/def :new-course/course    nil?)

(spec/def :viewmodel/collection (spec/keys :req-un [:query/collection]))
(spec/def :viewmodel/checkpoint (spec/keys :req-un [:query/course :query/checkpoint]))
(spec/def :viewmodel/course     (spec/keys :req-un [:query/course]))
(spec/def :viewmodel/new-course (spec/keys :req-un [:new-course/course]))

(spec/def :viewmodel/valid (spec/or :collection-view :viewmodel/collection
                                    :checkpoint-view :viewmodel/checkpoint
                                    :course-view     :viewmodel/course
                                    :new-course-view :viewmodel/new-course))

(spec/def :viewmodel/query :viewmodel/valid)
