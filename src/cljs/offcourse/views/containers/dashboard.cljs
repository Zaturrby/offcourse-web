(ns offcourse.views.containers.dashboard
  (:require [rum.core :as rum]
            [shared.protocols.loggable :as log]))

(rum/defc dashboard [{:keys [main edit-button]}]
  [:.dashboard
   [:.dashboard--main main]
   [:.dashboard--edit edit-button]])
