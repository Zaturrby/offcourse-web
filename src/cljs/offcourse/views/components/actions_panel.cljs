(ns offcourse.views.components.actions-panel
  (:require [rum.core :as rum]
            [cuerdas.core :as str]
            [shared.protocols.loggable :as log]))

(defn handler-button [button-text respond]
  [:li.actions-panel--link {:on-click #(respond)}
   button-text])

(rum/defc actions-panel [{:keys [user-name credentials] :as data}
                         respond]
  [:ul.actions-panel
   (handler-button "view-mode" (partial respond [:switch-to :view-mode]))
   (handler-button "auth" (partial respond [:switch-to :auth]))
   (handler-button "new-user" (partial respond [:switch-to :new-user]))
   (handler-button "edit-user" (partial respond [:switch-to :edit-profile]))
   (handler-button "view-profile" (partial respond [:switch-to :view-profile]))
   (if credentials
     (handler-button "Sign Out" (partial respond [:sign-out]))
     (handler-button "Sign In" (partial respond [:sign-in])))])
