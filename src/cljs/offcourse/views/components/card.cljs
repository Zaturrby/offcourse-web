(ns offcourse.views.components.card
  (:require [offcourse.views.components.item-list :refer [item-list]]
            [rum.core :as rum]
            [shared.protocols.loggable :as log]))

(rum/defc link-button [button-text url]
  [:li.button {:data-button-type "textbar"}
   [:a {:href url} button-text]])

(rum/defc action-button [button-text action respond]
  [:li.button {:data-button-type "textbar"}
   [:a {:on-click #(respond action)} button-text]])

(rum/defc card [{:keys [course-id goal course-slug checkpoints curator] :as course}
                respond]
  (let [{:keys [affordances course-url]} (meta course)
        {:keys [browsable? forkable? trackable? editable?]} affordances]
   [:.container
    [:.card
     [:.card--section
      [:a.card--title {:href (-> course meta :course-url)} goal]]
     (log/log "course from card")
     (log/log course)
     [:.card--section (item-list :todo checkpoints trackable? respond)]
     [:.card--section
      [:ul.card--actions
       (when browsable? (link-button "Browse" course-url))
       (when forkable? (action-button "Fork" [:fork course] respond))]]]]))

(rum/defc cards [{:keys [courses]} respond]
  [:.cards (map #(rum/with-key (card % respond) (:course-id %)) courses)])
