(ns offcourse.views.base
  (:require [plumbing.core :refer-macros [fnk]]
            [offcourse.views.containers.app :refer [app]]
            [offcourse.views.components.logo :refer [logo]]
            [offcourse.views.components.actions-panel :refer [actions-panel]]
            [offcourse.views.containers.menubar :refer [menubar]]
            [shared.protocols.specced :as sp]
            [shared.protocols.loggable :as log]
            [clojure.set :as set]))

(def graph
  {:container      (fnk [] app)
   :viewmodel      (fnk [[:appstate viewmodel]] viewmodel)
   :viewmodel-name (fnk [viewmodel] (sp/resolve viewmodel))
   :user           (fnk [appstate] (:user appstate))
   :user-name      (fnk [user] (when user (:user-name user)))
   :base-actions   (fnk [] #{:sign-in :go :sign-out})
   :actions        (fnk [base-actions view-actions] (set/union base-actions view-actions))
   :respond        (fnk [responder actions]
                        (fn [[action-type :as action]] action
                          (if (contains? actions action-type)
                            (responder [:requested action])
                            (log/error action-type (str "invalid action")))))
   :logo           (fnk [[:appstate site-title]
                         respond]
                        (logo {:site-title site-title} respond))
   :actions-panel  (fnk [user
                         respond]
                        (actions-panel user
                                       respond))
   :menubar        (fnk [logo
                         actions-panel
                         viewmodel-name]
                        (menubar logo actions-panel))})
