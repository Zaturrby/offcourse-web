(ns offcourse.views.components.edit-modal
  (:require [rum.core :as rum]
            [offcourse.views.components.button :refer [button]]
            [offcourse.views.components.item-list :refer [edit-list]]
            [shared.protocols.specced :as sp]
            [shared.protocols.loggable :as log])
  (:require-macros [com.rpl.specter.macros :refer [setval]]))

 
(defn update-prop [prop-name event atom]
  (let [prop-value (.. event -target -value)]
    (swap! atom #(assoc % prop-name prop-value))))

(defn update-checkpoint [atom prop-name checkpoint event]
  (let [prop-value (.. event -target -value)]
    (swap! atom #(setval [:checkpoints checkpoint prop-name] prop-value %))))

(rum/defcs edit-modal < (rum/local {} ::course) [state {:keys [course]}]
 (let [course-atom (::course state)
       course (merge course @course-atom)
       valid? (sp/valid? course)]
  [:.edit-modal
   [:.edit-modal--section 
    [:.edit-modal--action-title "Edit the Title"]
    [:input.edit-modal--course-title {:type        :text 
                                      :value      (:goal course)
                                      :on-change   #(update-prop :goal % course-atom)}]]
   [:.edit-modal--section
    [:.edit-modal--action-title "Edit the Resources"]
    [:.edit-modal--list (edit-list :edit 
                                  (:checkpoints course) 
                                  (partial update-checkpoint course-atom))]]
   [:.edit-modal--section
    [:.edit-modal--actions 
     [(when true (button "Save Course" (partial log/log "Saving Course... or not")))]
     [(when true (button "Publish Course" (partial log/log "Publishing Course... or not")))]
     [(when true (button "Cancel" (partial log/log "Canceling... or not")))]]]]))
   