(ns offcourse.views.components.edit-modal
  (:require [rum.core :as rum]
            [offcourse.views.components.button :refer [button]]
            [offcourse.views.components.item-list :refer [item-list]]
            [shared.protocols.loggable :as log]))
 
(rum/defc edit-modal [{:keys [checkpoint course]}]
  (log/log "edit modal checkpoint!!")
  (log/log checkpoint)
  ; (log/log course)
  [:.edit-modal 
   [:.edit-modal--section 
    [:.edit-modal--action-title "Edit the Title"]
    [:.edit-modal--course-title (:task checkpoint)]]
   [:.edit-modal--section
    [:.edit-modal--action-title "Edit the Resources"]
    [:.edit-modal--list (item-list :todo (:checkpoints course) nil nil)]]
   [:.edit-modal--section
    [:.edit-modal--actions 
     [(when true (button "Save Course" (partial log/log "Saving Course... or not")))]
     [(when true (button "Publish Course" (partial log/log "Publishing Course... or not")))]
     [(when true (button "Cancel" (partial log/log "Canceling... or not")))]]]])
    