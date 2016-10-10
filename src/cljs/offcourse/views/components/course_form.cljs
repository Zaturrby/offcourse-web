(ns offcourse.views.components.course-form
  (:require [rum.core :as rum]
            [offcourse.views.components.button :refer [button]]
            [offcourse.views.components.item-list :refer [edit-list]]
            [shared.protocols.specced :as sp]
            [shared.protocols.loggable :as log]))

(defn update-prop [prop-name event atom]
  (let [prop-value (.. event -target -value)]
    (swap! atom #(assoc % prop-name prop-value)))

(defn update-course [course-atom course checkpoint]
  (swap! course-atom #(co/update-checkpoint course checkpoint)))

(def new-checkpoint {:checkpoint-id 4375864
                     :task "New task"
                     :resource-url "New url"})

(defn add-resource [atom course]
  (let [checkpoints (:checkpoints course)]
    (swap! atom #(assoc % :checkpoints (conj checkpoints new-checkpoint)))))

(rum/defcs course-form < (rum/local {} ::course) [state {:keys [course]} respond]
 (let [course-atom (::course state)
       course (merge course @course-atom)
       valid? (sp/valid? course)]
  (log/log (:checkpoints course))
  [:.edit-modal
   [:.edit-modal--section {:key :title}
    [:.edit-modal--action-title "Edit the Title"]
    [:input.edit-modal--course-title {:type        :text
                                      :value      (:goal course)
                                      :on-change   #(update-prop :goal % course-atom)}]]
   [:.edit-modal--section {:key :tasks}
    [:.edit-modal--action-title "Edit the Resources"]
    [:.edit-modal--list (edit-list :edit
                                  (:checkpoints course)
                                  #(update-course course-atom course %1))]
    [:.edit-modal--action
      (when true (button "Add resource" #(add-resource course-atom course)))]]
   [:.edit-modal--section {:key :actions}
    [:.edit-modal--actions
     [(when true (button "Save Course" (partial log/log "Saving Course... or not")))]
     [(when true (button "Publish Course" (partial log/log "Publishing Course... or not")))]
     [(when true (button "Cancel" #(respond [:switch-to :view-mode])))]]]]))
