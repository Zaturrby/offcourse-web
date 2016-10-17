(ns offcourse.views.components.user-form
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

(rum/defcs user-form < (rum/local {} ::user) [state user respond]
  (let [user-atom (::user state)
        user (merge user @user-atom)
        valid? (sp/valid? user)]
    [:.card
      [:.card--section
        [:.card--indenter
          [:h1.card--title "Sign up"]]]
      [:.card--section {:key :user-name}
        [:.card--indenter
          [:p.card--text "Step 2 of 2 - Add an Username"]]
        [:input.card--field {:placeholder "Username"
                             :value (:user-name user)
                             :auto-focus true
                             :on-change #(update-prop :user-name % user-atom)}]]
      (when true ;valid?
        [:.card--section {:key :actions}
          [:.actions
            (button {:button-text "Go!"} #(respond [:create user]))]])]))
