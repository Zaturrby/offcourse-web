(ns offcourse.views.components.item-list
  (:require [rum.core :as rum]
            [shared.protocols.loggable :as log]))

(defn toggle-checkpoint [{:keys [complete?] :as checkpoint} respond]
  (respond [:update (assoc checkpoint :complete? (not complete?))]))

(rum/defc todo-list-item [{:keys [task complete? checkpoint-slug order] :as checkpoint} trackable? respond]
  (let [{:keys [selected checkpoint-url]} (meta checkpoint)]
    [:li.list--item {:data-selected selected
                     :data-item-type :todo}
     (when trackable? [:button.button
                       {:key :checkbox
                        :data-button-type :checkbox
                        :on-click #(toggle-checkpoint checkpoint respond)
                        :data-selected (boolean complete?)}])
     [:a {:key :title
          :href checkpoint-url} [:span task]]]))

(rum/defc list-item [{:keys [task] :as checkpoint}]
  [:li.list--item
   [:span {:key :title} task]
   [:button.button {:key :add-button
                    :data-button-type (name :icon)
                    :on-click nil #_(remove-checkpoint checkpoint)} "X"]])

(rum/defc item-list [list-type checkpoints trackable? respond]
  [:ul.list {:data-list-type (name list-type)}
   (case list-type
     :todo (map #(rum/with-key (todo-list-item % trackable? respond) (:checkpoint-id %)) checkpoints)
     :edit (map #(rum/with-key (list-item %) (:checkpoint-id %)) checkpoints))])
