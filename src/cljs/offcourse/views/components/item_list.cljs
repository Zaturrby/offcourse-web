(ns offcourse.views.components.item-list
  (:require [rum.core :as rum]
            [shared.protocols.specced :as sp]
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

(rum/defc item-list [list-type checkpoints trackable? respond]
  [:ul.list {:data-list-type (name list-type)}
   (case list-type
     :todo (map #(rum/with-key (todo-list-item % trackable? respond) (:checkpoint-id %)) checkpoints))])

(rum/defc edit-list-item [checkpoint trackable? handler]
    [:li.list--item
      [:.list--item-section
       [:input.list--course {:type        :text 
                             :value      (:task checkpoint)
                             :on-change   #(handler :task % checkpoint)}]
       [:input.list--url    {:type        :text 
                             :value      (:resource-url checkpoint)
                             :on-change   #(handler :resource-url checkpoint %)}]]
      [:.list--item-section
       [:button.button {:key :add-button
                        :data-button-type (name :icon)
                        :on-click nil #_(remove-checkpoint checkpoint)} "^"]]])

(rum/defc edit-list [list-type checkpoints handler]
  [:ul.list {:data-list-type (name list-type)}
   (map #(rum/with-key (edit-list-item % handler) (:checkpoint-id %)) checkpoints)])