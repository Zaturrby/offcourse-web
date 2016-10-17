(ns offcourse.views.components.auth-form
  (:require [rum.core :as rum]
            [cuerdas.core :as str]
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
    [:.auth
      [:.auth--section
        [:h1.auth--title "Sign up"]]
      [:.auth--section
        [:p.auth--text "Step 1 of 2 - Authenticate"]
        [:.auth--button-container
          [:.auth--button "Github"]
          [:.auth--button "Twitter"]]]]))
