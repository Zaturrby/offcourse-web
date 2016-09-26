(ns offcourse.views.components.edit-modal
  (:require [rum.core :as rum]
            [shared.protocols.loggable :as log]))
 
(rum/defc edit-modal [checkpoint]
  (log/log "edit modal checkpoint!!")
  (log/log checkpoint)
  [:div.edit-modal (:task checkpoint)])