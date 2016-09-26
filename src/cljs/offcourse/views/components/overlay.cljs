(ns offcourse.views.components.overlay
  (:require [rum.core :as rum]
            [shared.protocols.loggable :as log]))
 
(rum/defc overlay
  (log/log "Overlay!"))