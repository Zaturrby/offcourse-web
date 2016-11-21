(ns offcourse.specs.appstate
  (:require [cljs.spec :as spec]))

(spec/def :appstate/site-title string?)

(spec/def :appstate/valid (spec/keys :req-un [:appstate/site-title
                                              :offcourse/viewmodel]))
