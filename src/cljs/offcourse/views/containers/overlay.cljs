(ns offcourse.views.containers.overlay
  (:require [rum.core :as rum]
            [shared.protocols.loggable :as log]))
 
(rum/defc overlay []
  [:div.overlay "overlay"])