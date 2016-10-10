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

(defn remove-checkpoint [course-atom course checkpoint]
  (swap! course-atom #(co/remove-checkpoint course checkpoint)))

(defn create-checkpoint [course-atom course]
  (swap! course-atom #(co/create-checkpoint course)))

(rum/defcs course-form < (rum/local {} ::course) [state {:keys [course]} respond]
  (let [course-atom (::course state)
        old-course  course
        course      (merge course @course-atom)
        dirty?      (not= course old-course)
        valid?      (sp/valid? course)]
  [:.course-form
   [:.course-form--section {:key :title}
    [:.course-form--action-title "Edit the Title"]
    [:input.course-form--course-title {:key       "title"
                                       :type      :text
                                       :placeholder "Goal"
                                       :value     (:goal course)
                                       :on-change #(update-prop :goal % course-atom)}]]
   [:.course-form--section {:key :tasks}
    [:.course-form--action-title "Edit the Resources"]
    [:.course-form--list (edit-list (:checkpoints course)
                                   #(update-checkpoint course-atom course %1)
                                   #(remove-checkpoint course-atom course %1))]
    [:.course-form--cp-actions
      (when true (button "Add Checkpoint" #(create-checkpoint course-atom course)))]]
   [:.course-form--section {:key :actions}
    [:.course-form--actions
     [(when (and valid? dirty?) (button "Save Course" #(respond [:update course])))]
     [(when true (button "Cancel" #(respond [:switch-to :view-mode])))]]]]))
