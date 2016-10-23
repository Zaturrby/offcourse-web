(ns offcourse.views.components.view-profile
  (:require [rum.core :as rum]
            [cuerdas.core :as str]
            [offcourse.views.components.button :refer [button]]
            [shared.protocols.loggable :as log]
            [shared.protocols.specced :as sp]))

(rum/defc view-profile []
  [:.card {:data-card-type :wide}
    [:.card--section
      [:.card--row {:data-row-spaced true}
          [:h1.card--title {:data-title-indent true} "Charlotte"]
          [:.card--link "Edit Profile"]]]
    [:.card--section
      [:p.card--text {:data-text-indent true
                      :data-text-padded :large}
                     "#Python #Database #HTML #CSS #Clojure"]
      [:p.card--text {:data-text-indent true
                      :data-text-padded :large}
                     "I'm an Information Architect in many ways"]
      [:p.card--text {:data-text-indent true}
        [:a.card--link "charlottevanoostrum.com"]]]
    [:.card--section
      (button {:button-text "Follow Charlotte"} #(log/log "Follow Charlotte"))]])
