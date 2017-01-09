(ns offcourse.views.components.actions-panel
  (:require [rum.core :as rum]
            [cuerdas.core :as str]
            [shared.protocols.loggable :as log]))

(defn handler-button [button-text respond]
  [:li.actions-panel--item {:key button-text}
   [:a.actions-panel--link {:on-click #(respond)}
                           button-text]])

(rum/defc actions-panel [{:keys [user-name credentials] :as data}
                         respond]
  [:ul.actions-panel
    [:.actions-panel--item
      [:.action-panel--search-container
        [:input.actions-panel--search {:placeholder "Enter your search"}]
        [:a.actions-panel--search-btn "Search"]]]
   (if credentials
     [(handler-button "Sign Out" (partial respond [:sign-out]))
      [:li.actions-panel--item {:key "new-course"}
       [:a.actions-panel--link {:href "/courses/new"}
                               "New Course"]]]
     (handler-button "Sign In" #(respond [:switch-to :auth])))])




     ;
    ;  #_(handler-button "view-mode" (partial respond [:switch-to :view-mode]))
    ;  #_(handler-button "auth" (partial respond [:switch-to :auth]))
    ;  #_(handler-button "new-user" (partial respond [:switch-to :new-user]))
    ;  #_(handler-button "edit-user" (partial respond [:switch-to :edit-profile]))
    ;  #_(handler-button "view-profile" (partial respond [:switch-to :view-profile])))])
