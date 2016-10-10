(ns offcourse.views.components.course-form
  (:require [rum.core :as rum]
            [offcourse.views.components.button :refer [button]]
            [offcourse.views.components.item-list :refer [edit-list]]
            [shared.protocols.specced :as sp]
            [shared.protocols.loggable :as log]
            [shared.models.course.index :as co]))

(defn update-prop [prop-name event atom]
  (let [prop-value (.. event -target -value)]
    (swap! atom #(assoc % prop-name prop-value))))

(defn update-checkpoint [course-atom course checkpoint]
  (swap! course-atom #(co/update-checkpoint course checkpoint)))

(defn create-checkpoint [course-atom course]
  (swap! course-atom #(co/create-checkpoint course)))

(rum/defcs course-form < (rum/local {} ::course) [state {:keys [course]} respond]
 (let [course-atom (::course state)
       course (merge course @course-atom)
       valid? (sp/valid? course)]
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
                                  #(update-checkpoint course-atom course %1))]
    [:.edit-modal--action
      (when true (button "Add Checkpoint" #(create-checkpoint course-atom course)))]]
   [:.edit-modal--section {:key :actions}
    [:.edit-modal--actions
     [(when true (button "Save Course" (partial log/log "Saving Course... or not")))]
     [(when true (button "Cancel" #(respond [:switch-to :view-mode])))]]]]))
