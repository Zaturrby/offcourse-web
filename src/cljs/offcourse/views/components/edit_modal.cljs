(ns offcourse.views.components.edit-modal
  (:require [rum.core :as rum]
            [offcourse.views.components.button :refer [button]]
            [shared.protocols.specced :as sp]
            [offcourse.views.components.item-list :refer [edit-list]]
            [shared.protocols.loggable :as log]
            [shared.models.course.index :as co]))

(defn update-prop [prop-name event atom]
  (let [prop-value (.. event -target -value)]
    (swap! atom #(assoc % prop-name prop-value))))

(defn update-course [course-atom course checkpoint]
  (swap! course-atom #(co/update-checkpoint course checkpoint)))

(rum/defcs edit-modal < (rum/local {} ::course) [state {:keys [course]} respond]
  (let [course-atom (::course state)
        new-course  (merge course @course-atom)
        dirty?      (not= course new-course)
        valid?      (sp/valid? new-course)]
  [:.edit-modal
   [:.edit-modal--section {:key :title}
    [:.edit-modal--action-title "Edit the Title"]
    [:input.edit-modal--course-title {:type      :text
                                      :value     (:goal new-course)
                                      :on-change #(update-prop :goal % course-atom)}]]
   [:.edit-modal--section {:key :tasks}
    [:.edit-modal--action-title "Edit the Resources"]
    [:.edit-modal--list (edit-list :edit
                                    (:checkpoints new-course)
                                  #(update-course course-atom course %1))]]
   [:.edit-modal--section {:key :actions}
    [:.edit-modal--actions
     [(when (and valid? dirty?) (button "Save Course" #(respond [:update new-course])))]
     [(when true (button "Cancel" #(respond [:switch-to :view-mode])))]]]]))
