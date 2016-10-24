(ns offcourse.views.containers.overlay
  (:require [rum.core :as rum]
            [shared.protocols.loggable :as log]))

(rum/defc overlay [modal respond]
  [:div.overlay #_{:on-click  #(respond [:switch-to :view-mode])}
    modal])
