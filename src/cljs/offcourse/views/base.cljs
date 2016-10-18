(ns offcourse.views.base
  (:require [clojure.set :as set]
            [offcourse.views.containers.app :refer [app]]
            [offcourse.views.containers.menubar :refer [menubar]]
            [offcourse.views.components.notifybar :refer [notifybar]]
            [offcourse.views.containers.overlay :refer [overlay]]
            [offcourse.views.components.actions-panel :refer [actions-panel]]
            [offcourse.views.components.logo :refer [logo]]
            [offcourse.views.components.auth-form :refer [auth-form]]
            [offcourse.views.components.user-form :refer [user-form]]
            [offcourse.views.components.edit-profile :refer [edit-profile]]
            [offcourse.views.components.view-profile :refer [view-profile]]
            [plumbing.core :refer-macros [fnk]]
            [shared.protocols.loggable :as log]
            [shared.protocols.specced :as sp]))

(def notification {:title "Thank you for signing up!"
                   :link  "You can edit your profile here"
                   :color "gray"})

(def graph
  {:base-actions   (fnk [] #{:go :sign-in :sign-out})
   :container      (fnk [] app)
   :viewmodel      (fnk [appstate] (-> appstate :viewmodel))
   :app-mode       (fnk [appstate] (-> appstate :app-mode))
   :user           (fnk [appstate] (-> appstate :user))
   :user-name      (fnk [user] (when user (:user-name user)))
   :actions        (fnk [base-actions view-actions] (set/union base-actions view-actions))
   :respond        (fnk [responder actions]
                        (fn [[action-type :as action]] action
                          (if (contains? actions action-type)
                            (responder [:requested action])
                            (log/error action-type (str "invalid action")))))
   :logo           (fnk [[:appstate site-title] respond]
                        (logo {:site-title site-title} respond))
   :actions-panel  (fnk [user respond] (actions-panel user respond))
   :menubar        (fnk [logo actions-panel]
                        (menubar logo actions-panel))
   :notification   (fnk [appstate] notification)
   :notifybar      (fnk [notification respond]
                        (when true (notifybar notification respond)))
   :overlay        (fnk [app-mode]
                        (when true))})
                          ; (overlay (auth-form))))})
                          ; (overlay (user-form))))})
                          ; (overlay (edit-profile))))})
                          ; (overlay (view-profile))))})
