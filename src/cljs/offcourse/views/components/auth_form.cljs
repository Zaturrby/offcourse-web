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
    [:.card {:data-card-type :medium}
      [:.card--section
        [:h1.card--title {:data-title-indent true} "Sign up"]]
      [:.card--section
        [:p.card--text {:data-text-indent true
                        :data-text-padded :large}
                       "Step 1 of 2 - Authenticate"]
        [:.card--row
          (button {:button-text "Github"
                   :button-color "github"
                   :button-width "full"}
                  #())
          (button {:button-text "Twitter"
                   :button-color "twitter"
                   :button-width "full"}
                  #())]]]))
