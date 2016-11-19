(ns offcourse.specs.appstate
  (:require [cljs.spec :as spec]
            [shared.specs.viewmodel :as viewmodel]))

(spec/def ::site-title string?)
(spec/def ::appstate (spec/keys :req-un [::site-title ::viewmodel/viewmodel]))
