(ns offcourse.views.signup
  (:require [shared.protocols.decoratable :as dc]
            [offcourse.views.containers.dashboard :refer [dashboard]]
            [offcourse.views.components.user-form :refer [user-form]]
            [plumbing.core :refer-macros [fnk]]))

(def graph
  {:profile     (fnk [appstate] nil)
   :main        (fnk [] nil)
   :actions    (fnk [base-actions]
                    (->> base-actions
                         (into #{})))
   :dashboard   (fnk [profile
                      respond]
                     (dashboard {:main (user-form profile respond)}))})
