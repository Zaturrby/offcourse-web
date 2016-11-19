(ns offcourse.specs.route
  (:require [cljs.spec :as spec]
            [shared.specs.base :as base]
            [shared.specs.collection :as collection]
            [shared.specs.course :as course]
            [shared.specs.checkpoint :as checkpoint]))

(spec/def ::organization (spec/nilable ::base/organization))

(spec/def ::course     (spec/keys :req-un [::base/curator ::course/course-slug]
                                  :opt-un [::organization]))

(spec/def ::checkpoint (spec/keys :req-un [::checkpoint/checkpoint-slug ::base/curator
                                           ::course/course-slug]
                                  :opt-un [::organization]))

(spec/def ::home-view nil?)
(spec/def ::signup-view nil?)
(spec/def ::new-course-view nil?)

(spec/def ::home       (spec/keys :req-un [::home-view]))
(spec/def ::sign-up    (spec/keys :req-un [::signup-view]))
(spec/def ::new-course (spec/keys :req-un [::new-course-view]))

(spec/def ::route      (spec/or :home-view       ::home
                                :signup-view     ::sign-up
                                :new-course-view ::new-course
                                :collection-view ::collection/collection
                                :checkpoint-view ::checkpoint
                                :course-view     ::course))
