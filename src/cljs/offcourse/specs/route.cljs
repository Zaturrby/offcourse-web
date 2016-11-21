(ns offcourse.specs.route
  (:require [cljs.spec :as spec]))


(spec/def ::home-view nil?)
(spec/def ::signup-view nil?)
(spec/def ::new-course-view nil?)
(spec/def :route/organization (spec/nilable :base/organization))

(spec/def :route/course     (spec/keys :req-un [:course/curator :course/course-slug]
                                       :opt-un [:route/organization]))
(spec/def :route/checkpoint (spec/keys :req-un [:checkpoint/checkpoint-slug :course/curator
                                                :course/course-slug]
                                       :opt-un [:route/organization]))
(spec/def :route/collection :offcourse/collection)
(spec/def :route/home       (spec/keys :req-un [::home-view]))
(spec/def :route/sign-up    (spec/keys :req-un [::signup-view]))
(spec/def :route/new-course (spec/keys :req-un [::new-course-view]))

(spec/def :route/valid (spec/or :home-view       :route/home
                                :signup-view     :route/sign-up
                                :new-course-view :route/new-course
                                :collection-view :route/collection
                                :checkpoint-view :route/checkpoint
                                :course-view     :route/course))
