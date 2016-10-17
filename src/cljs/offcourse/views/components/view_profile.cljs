(ns offcourse.views.components.view-profile
  (:require [rum.core :as rum]
            [cuerdas.core :as str]
            [offcourse.views.components.button :refer [button]]
            [shared.protocols.loggable :as log]
            [shared.protocols.specced :as sp]))

(rum/defc view-profile []
  [:.card {:data-card-type :wide}
    [:.card--section
      [:.card--indenter
        [:.card--row-between
          [:h1.card--title "Charlotte"]
          [:.card--link "Edit Profile"]]]]
    [:.card--section
      [:.card--indenter
        [:p.card--text "#Python #Database #HTML #CSS #Clojure"]
        [:p.card--text "I'm an Information Architect in many ways"]
        [:p.card--link "charlottevanoostrum.com"]]]
    [:.card--section
      (button {:button-text "Follow Charlotte"} #(log/log "Follow"))]])
