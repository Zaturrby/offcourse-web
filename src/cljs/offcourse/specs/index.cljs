(ns offcourse.specs.index
  (:require [cljs.spec :as spec]
            [shared.specs.index]
            [offcourse.specs.appstate]
            [offcourse.specs.viewmodel]
            [offcourse.specs.route]
            [offcourse.specs.payload]
            [offcourse.specs.query]
            [offcourse.specs.event]
            [offcourse.specs.action]))

(spec/def :offcourse/app-modes #{:view-mode :edit-mode :auth :new-user
                                 :edit-profile :view-profile})

(spec/def ::tags #{:all})

(spec/def :offcourse/viewmodel :viewmodel/valid)
(spec/def :offcourse/route     :route/valid)
(spec/def :offcourse/appstate  :appstate/valid)
(spec/def :offcourse/query     :query/frontend)
(spec/def :offcourse/payload   :payload/frontend)
(spec/def :offcourse/action    :action/valid)
(spec/def :offcourse/event     :event/valid)
(spec/def :offcourse/user      map?)

(spec/def :query/viewmodel     :viewmodel/query)
(spec/def :query/tags          (spec/keys :req-un [::tags]))
