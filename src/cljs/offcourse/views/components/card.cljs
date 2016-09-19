(ns offcourse.views.components.card
  (:require [offcourse.views.components.item-list :refer [item-list]]
            [rum.core :as rum]
            [shared.protocols.loggable :as log]))

(rum/defc action [course action-type]
  [:li.button {:data-button-type "textbar"}
   [:a {:href (-> course meta :course-url)} action-type]])

(rum/defc card [{:keys [course-id goal course-slug checkpoints curator] :as course}]
  (let [{:keys [affordances]} (meta course)
        {:keys [browsable? forkable? editable?]} affordances]
   [:.container
    [:.card
     [:.card--section
      [:a.card--title {:href (-> course meta :course-url)} goal]]
     [:.card--section (item-list :todo checkpoints)]
     [:.card--section
      [:ul.card--actions
       (when browsable? (action course "Browse"))
       (when forkable? (action course "Fork"))
       (when editable? (action course "Edit"))]]]]))

(rum/defc cards [{:keys [courses]}]
  [:.cards (map #(rum/with-key (card %) (:course-id %)) courses)])
