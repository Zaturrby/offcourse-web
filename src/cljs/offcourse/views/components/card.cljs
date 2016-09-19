(ns offcourse.views.components.card
  (:require [offcourse.views.components.item-list :refer [item-list]]
            [rum.core :as rum]))

(rum/defc card [{:keys [course-id goal course-slug checkpoints curator] :as course}]
  [:.container
   [:.card
    [:.card--section
     [:a.card--title {:href (-> course meta :course-url)} goal]
     [:img.card--edit-sign {:src "/images/pencil.svg"}]]
    [:.card--section (item-list :todo checkpoints)]]])

(rum/defc cards [{:keys [courses]}]
  [:.cards (map #(rum/with-key (card %) (:course-id %)) courses)])
