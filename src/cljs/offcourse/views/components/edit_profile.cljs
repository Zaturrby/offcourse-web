(ns offcourse.views.components.edit-profile
  (:require [rum.core :as rum]
            [cuerdas.core :as str]
            [offcourse.views.components.button :refer [button]]
            [offcourse.views.components.dropdown :refer [dropdown]]
            [shared.protocols.loggable :as log]
            [shared.protocols.specced :as sp]))

(defn button-title [string]
  (-> string
      str/humanize
      str/titleize))

(defn update-prop [prop-name event atom]
  (let [user-name (.. event -target -value)]
    (swap! atom #(assoc % prop-name user-name))))

(defn set-dropdown [dropdown-name dropdown-atom]
  (swap! dropdown-atom #(str dropdown-name)))

(rum/defcs edit-profile < (rum/local {} ::user)
                          (rum/local "" ::dropdown)
  [state user respond]
  (let [user-atom (::user state)
        user (merge user @user-atom)
        valid? (sp/valid? user)
        dropdown-atom (::dropdown state)
        dropdown?     @dropdown-atom]
    [:.card {:data-card-type :wide
             :on-click (when (not= dropdown? "") #(set-dropdown "" dropdown-atom))}
      [:.card--section
        [:.card--row {:data-row-spaced true}
          [:h1.card--title {:data-title-indent true} "Edit your Profile"]
          [:.card--link {:data-link-type :strong} "View Profile"]]]
      [:.card--section {:key :user-name}
        [:p.card--text {:data-text-indent true} "Username"]
        [:input.form--field {:placeholder "Username"
                             :value (:user-name user)
                             :auto-focus true
                             :on-change #(update-prop :user-name % user-atom)}]]

      [:.card--section
        [:p.card--text {:data-text-indent true} "What would you like to learn"]
        [:.card--row {:data-row-wrap true}
          ; (map #(rum/with-key 
          ;         [:input.form--field {:data-field-type :half
          ;                              :placeholder "Python"}]
          ;         ())
          ;      checkpoints)
          [:input.form--field {:data-field-type :half
                               :placeholder "Database"}]
          [:input.form--field {:data-field-type :half
                               :placeholder "HTML"}]
          [:input.form--field {:data-field-type :half
                               :placeholder "CSS"}]
          [:input.form--field {:data-field-type :half
                               :placeholder "Clojure"}]]]

      [:.card--section
        [:.card--row {:data-row-spaced true
                      :data-row-padded :large}
          [:p.card--text {:data-text-indent true}
                         "Add your accounts"]
          [:.card--link {:data-link-type :em
                         :on-click #(set-dropdown "data" dropdown-atom)}
                        "What Offcourse will do with your accounts"]
          (dropdown {:title "Datapolicy"
                     :text "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
                     :shown (= dropdown? "data")})]
        [:.card--row {:data-row-padded :large}
          (button {:button-text "Github"
                   :button-color "github"}
                  #(log/log "Login with Github"))
          [:.card--title {:data-title-type :disabled
                          :data-title-indent true}
            "Add your Github account"]]
        [:.card--row {:data-row-padded :large}
          (button {:button-text "Twitter"
                   :button-color "twitter"}
                  #(log/log "Login with Twitter"))
          [:.card--title {:data-title-type :disabled
                          :data-title-indent true}
            "Add your Twitter account"]]
        [:.card--row
          (button {:button-text "Linkedin"
                   :button-color "linkedin"}
                  #(log/log "Login with Linkedin"))
          [:.card--title {:data-title-type :disabled
                          :data-title-indent true}
            "Add your Linkedin account"]]]

      [:.card--section
        [:p.card--text {:data-text-indent true} "Some cool information about you"]
        [:input.form--field {:placeholder "Username"}]]

      [:.card--section
        [:p.card--text {:data-text-indent true} "Your URL"]
        [:input.form--field {:placeholder "charlottevanoostrum.com"}]]

      (when true ;valid?
        [:.card--section
          [:.card--row {:data-row-spaced true}
            (button {:button-text "Save Changes"}
                    #(log/log "Update user"))
            (button {:button-text "Cancel"
                     :button-color "red"}
                    #(log/log "Close"))]])]))
