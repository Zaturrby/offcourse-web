(ns offcourse.views.components.new-course
  (:require [rum.core :as rum]
            [offcourse.views.components.button :refer [button]]
            [offcourse.views.components.item-list :refer [edit-list]]
            [shared.protocols.specced :as sp]
            [shared.protocols.loggable :as log])
  (:require-macros [com.rpl.specter.macros :refer [setval]]))

(defn update-prop [prop-name event atom]
  (let [prop-value (.. event -target -value)]
    (swap! atom #(assoc % prop-name prop-value))))

(rum/defcs new-course < (rum/local {} ::course) [state {:keys [empty-course]} respond]
 (let [course-atom (::course state)
       course @course-atom]
      ;  valid? (sp/valid? course)]
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
                                  #())]]
   [:.edit-modal--section {:key :actions}
    [:.edit-modal--actions
     [(when true (button "Save Course" (partial log/log "Saving Course... or not")))]
     [(when true (button "Publish Course" (partial log/log "Publishing Course... or not")))]
     [(when true (button "Cancel" #(respond [:switch-to :view-mode])))]]]]))
