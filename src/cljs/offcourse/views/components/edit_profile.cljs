(ns offcourse.views.components.edit-profile
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

(rum/defcs edit-profile < (rum/local {} ::user) [state user respond]
  (let [user-atom (::user state)
        user (merge user @user-atom)
        valid? (sp/valid? user)]
    [:.card {:data-card-type :wide}
      [:.card--section
        [:.card--row-between
          [:.card--indenter
            [:h1.card--title "Edit your Profile"]]
          [:.card--link "View Profile"]]]
      [:.card--section {:key :user-name}
        [:.card--indenter
          [:p.card--text "Username"]]
        [:input.card--field {:placeholder "Username"
                             :value (:user-name user)
                             :auto-focus true
                             :on-change #(update-prop :user-name % user-atom)}]]

      [:.card--section
        [:.card--indenter
          [:p.card--text "What would you like to learn"]]
        [:.card--row-wrap
          [:input.card--field-small {:placeholder "Python"}]
          [:input.card--field-small {:placeholder "Database"}]
          [:input.card--field-small {:placeholder "HTML"}]
          [:input.card--field-small {:placeholder "CSS"}]
          [:input.card--field-small {:placeholder "Clojure"}]]]

      [:.card--section
        [:.card--row-between
          [:.card--indenter
            [:p.card--text "Add your accounts"]]
          [:.card--link-em "What Offcourse will do with your accounts"]]
        [:.card--padder
          [:.card--row
            (button {:button-text "Github"
                     :button-color "github"}
                    #())
            [:.card--v-center [:.card--indenter [:.card--title "Add your Github account"]]]]]
        [:.card--padder
          [:.card--row
            (button {:button-text "Twitter"
                     :button-color "twitter"}
                    #())
            [:.card--v-center [:.card--indenter [:.card--title "Add your Twitter account"]]]]]
        [:.card--padder
          [:.card--row
            (button {:button-text "Linkedin"
                     :button-color "linkedin"}
                    #())
            [:.card--v-center [:.card--indenter [:.card--title "Add your Linkedin account"]]]]]]

      [:.card--section
        [:.card--indenter
          [:p.card--text "Some cool information about you"]]
        [:input.card--field {:placeholder "Username"}]]

      [:.card--section
        [:.card--indenter
          [:p.card--text "Your URL"]]
        [:input.card--field {:placeholder "charlottevanoostrum.com"}]]

      (when true ;valid?
        [:.card--section
          [:.card--row-between
            (button {:button-text "Save Changes"} #(log/log "update user"))
            (button {:button-text "Cancel"
                     :button-color "red"}
                    #(log/log "close"))]])]))
