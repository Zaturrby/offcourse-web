(ns offcourse.views.components.auth-form
  (:require [rum.core :as rum]
            [cuerdas.core :as str]
            [offcourse.views.components.button :refer [button]]
            [shared.protocols.loggable :as log]
            [shared.protocols.specced :as sp]))

(defn button-title [string]
  (-> string
      str/humanize
      str/titleize))

(defn update-prop [prop-name event atom]
  (let [user-name (.. event -target -value)]
    (swap! atom #(assoc % prop-name user-name))))

(rum/defcs auth-form < (rum/local {} ::user) [state user respond]
  (let [user-atom (::user state)
        user (merge user @user-atom)
        valid? (sp/valid? user)]
  ; (log/log user-atom)
    [:.card
      [:.card--section
        [:.card--indenter [:h1.card--title "Sign up"]]]
      [:.card--section
        [:.card--indenter [:p.card--text "Step 1 of 2 - Authenticate"]]
        [:.card--padder
          (button {:button-text "Github"
                   :button-color "github"} #())
          (button {:button-text "Twitter"
                   :button-color "twitter"} #())]]]))
