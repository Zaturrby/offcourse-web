(ns offcourse.views.components.notifybar
  (:require [rum.core :as rum]))

(rum/defc notifybar [notification respond]
  [:.notifybar {:data-notify-color (:color notification)}
    [:.notifybar--section
      [:.notifybar--subtitle (:title notification)]
      (when (:link notification)
        [:a.notifybar--link (:link notification)])]
    [:.notifybar--section
       [:.notifybar--link {:data-link-type :strong} "Close"]]])
