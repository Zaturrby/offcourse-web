(ns offcourse.views.containers.overlay
  (:require [rum.core :as rum]
            [shared.protocols.loggable :as log]))
 
(rum/defc overlay [{:keys [edit-modal]}]
  [:div.overlay edit-modal])