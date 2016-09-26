(ns offcourse.views.components.edit-modal
  (:require [rum.core :as rum]
            [shared.protocols.loggable :as log]))
 
(rum/defc edit-modal
  (log/log "Edit!"))