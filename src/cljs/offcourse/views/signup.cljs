(ns offcourse.views.signup
  (:require [shared.protocols.decoratable :as dc]
            [offcourse.views.containers.dashboard :refer [dashboard]]
            [offcourse.views.components.user-form :refer [user-form]]
            [plumbing.core :refer-macros [fnk]]
            [clojure.set :as set]
            [shared.protocols.loggable :as log]))

(def graph
  {:user         (fnk [appstate] (:user appstate))
   :auth-token   (fnk [user] (:auth-token user))
   :main         (fnk [] nil)
   :view-actions (fnk [] #{:update})
   :dashboard    (fnk [user
                       auth-token
                       respond]
                      (if auth-token (dashboard {:main (user-form user respond)})
                          "Please Sign In to start the sign up process..."))})
