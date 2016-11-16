(ns offcourse.views.containers.dashboard
  (:require [rum.core :as rum]))

(rum/defc dashboard [{:keys [main controls]}]
  [:.dashboard
   [:.dashboard--main main]
   [:.dashboard--buttons controls]])
