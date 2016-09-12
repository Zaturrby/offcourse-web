(ns offcourse.views.components.user-form
  (:require [rum.core :as rum]
            [cuerdas.core :as str]
            [shared.protocols.loggable :as log]))

(defn button-title [string]
  (-> string
      str/humanize
      str/titleize))

(rum/defc user-form  [{:keys [user-name] :as user}
                      respond]
  [:.container
   [:.card
    [:.card--section {:key :user-name}
     [:input.title {:placeholder "Your Name"
                    :value (when user-name (button-title user-name))
                    :on-change nil}]]
    [:.card--section {:key :actions}
     [:.actions
      [:button.button {:key :save-course
                       :data-button-type :textbar
                       :on-click #(log/log user)
                       :disabled (not true)} "Save"]]]]])
