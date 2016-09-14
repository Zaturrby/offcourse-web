(ns offcourse.views.components.user-form
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

(rum/defcs user-form < (rum/local {} ::user) [state user respond]
  (let [user-atom (::user state)
        user (merge user @user-atom)
        valid? (sp/valid? user)]
  [:.container
   [:.card
    [:.card--section {:key :user-name}
     [:input.title {:placeholder "Your Name"
                    :value (:user-name user)
                    :auto-focus true
                    :on-change #(update-prop :user-name % user-atom)}]]
    (when valid?
      [:.card--section {:key :actions}
       [:.actions
        [:button.button {:key :save-course
                         :data-button-type :textbar
                         :on-click #(respond [:create @user-atom])
                         :disabled (not true)} "Save"]]])]]))
