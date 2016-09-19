(ns offcourse.views.components.item-list
  (:require [rum.core :as rum]
            [shared.protocols.loggable :as log]))

(defn toggle-checkpoint [checkpoint respond]
  (if (:completed? checkpoint)
    (respond [:mark-incomplete checkpoint])
    (respond [:mark-complete checkpoint])))

(rum/defc todo-list-item [{:keys [task completed? checkpoint-slug order] :as checkpoint} trackable? respond]
  (let [{:keys [selected checkpoint-url]} (meta checkpoint)]
    [:li.list--item {:data-selected selected
                     :data-item-type :todo}
     (when trackable? [:button.button
                       {:key :checkbox
                        :data-button-type :checkbox
                        :on-click #(toggle-checkpoint checkpoint respond)
                        :data-selected (boolean completed?)}])
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
