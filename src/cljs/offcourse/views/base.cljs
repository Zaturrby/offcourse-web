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

; It's derived state, but I might think that's not terrible in this case, because it should be standalone too.
; When the error is gone, the message should not nessecairly be gone too
(def notification {:title "Thank you for signing up!"
                   :link  "You can edit your profile here"
                   :color "yellow"})

(def graph
  {:base-actions   (fnk [] #{:go :sign-in :sign-out})
   :actions        (fnk [base-actions view-actions] (set/union base-actions view-actions))
   :container      (fnk [] app)
   :viewmodel      (fnk [appstate] (-> appstate :viewmodel))
   :app-mode       (fnk [appstate] (keyword "edit-user") #_(-> appstate :app-mode))
   :user           (fnk [appstate] (-> appstate :user))
   :user-name      (fnk [user] (when user (:user-name user)))
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
                        (when false (notifybar notification respond)))

   :base-overlays  (fnk [user respond]
                        {:auth #(overlay (auth-form user respond))
                         :new-user #(overlay (user-form user respond))
                         :edit-user #(overlay (edit-profile user respond))
                         :view-profile #(overlay (view-profile user respond))})
   :overlays       (fnk [base-overlays view-overlays]
                        (merge base-overlays view-overlays))
   :overlay        (fnk [app-mode overlays]
                        (when-let [overlay (get overlays app-mode)]
                          (overlay)))})
