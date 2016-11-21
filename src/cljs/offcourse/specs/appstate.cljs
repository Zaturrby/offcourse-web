(ns offcourse.specs.appstate
  (:require [cljs.spec :as spec]))

(spec/def :appstate/site-title string?)

(spec/def :appstate/user       (spec/nilable :offcourse/user))
(spec/def :appstate/courses    (spec/coll-of :offcourse/course))
(spec/def :appstate/resources  (spec/coll-of :offcourse/resource))
(spec/def :appstate/profiles   (spec/coll-of :offcourse/profile))

(spec/def :appstate/valid (spec/keys :req-un [:appstate/site-title
                                              :offcourse/viewmodel
                                              :appstate/user
                                              :appstate/courses
                                              :appstate/resources
                                              :appstate/profiles]))
