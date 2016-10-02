(ns offcourse.views.base
  (:require [clojure.set :as set]
            [offcourse.views.components.actions-panel :refer [actions-panel]]
            [offcourse.views.components.logo :refer [logo]]
            [offcourse.views.containers.app :refer [app]]
            [offcourse.views.containers.menubar :refer [menubar]]
            [plumbing.core :refer-macros [fnk]]
            [shared.protocols.loggable :as log]
            [shared.protocols.specced :as sp]))

(def graph
  {:container      (fnk [] app)
   :viewmodel      (fnk [[:appstate viewmodel]] viewmodel)
   :app-mode       (fnk [[:appstate app-mode]] app-mode)
   :viewmodel-name (fnk [viewmodel] (sp/resolve viewmodel))
   :user           (fnk [appstate] (:user appstate))
   :user-name      (fnk [user] (when user (:user-name user)))
   :base-actions   (fnk [] #{:go :sign-in :sign-out})
   :actions        (fnk [base-actions view-actions] (set/union base-actions view-actions))
   :respond        (fnk [responder actions]
                        (fn [[action-type :as action]] action
                          (if (contains? actions action-type)
                            (responder [:requested action])
                            (log/error action-type (str "invalid action")))))
   :logo           (fnk [[:appstate site-title]
                         respond]
                        (logo {:site-title site-title} respond))
   :actions-panel  (fnk [user respond]
                        (actions-panel user respond))
   :menubar        (fnk [logo actions-panel]
                        (menubar logo actions-panel))})
