(ns offcourse.views.components.view-profile
  (:require [rum.core :as rum]
            [cuerdas.core :as str]
            [offcourse.views.components.button :refer [button]]
            [shared.protocols.loggable :as log]
            [shared.protocols.specced :as sp]))

(rum/defc view-profile [user respond]
  [:.card {:data-card-type :wide}
    [:.card--section
      [:.card--row {:data-row-spaced true}
          [:h1.card--title {:data-title-indent true} (:full-name user)]
          [:.card--link "Edit Profile"]]]
    [:.card--section
      [:p.card--text {:data-text-indent true
                      :data-text-padded :large}
                     (map #(str %) (:interest user))]
      [:p.card--text {:data-text-indent true
                      :data-text-padded :large}
                     (:description user)]
      [:p.card--text {:data-text-indent true}
        [:a.card--link (:website user)]]]
    [:.card--section
      (button {:button-text (str "Follow " (:user-name user))} #(log/log "Follow"))]])
