(ns offcourse.views.containers.checkpoint-content
  (:require [rum.core :as rum]
            [shared.protocols.loggable :as log]))
  

(rum/defc checkpoint-content [{:keys [viewer meta-widget]}]
  [:.checkpoint
   [:.checkpoint--section viewer]
   [:.checkpoint--section meta-widget]])