(ns offcourse.views.components.card-social
  (:require [rum.core :as rum]))

(rum/defc card-social []
  [:.card--social
    [:.card--social-icons
      [:img.card--social-img {:src "/images/social/t.png"}]
      [:img.card--social-img {:src "/images/social/f.png"}]
      [:img.card--social-img {:src "/images/social/gplus.png"}]
      [:img.card--social-img {:src "/images/social/gh.png"}]]
    [:.card--social-url "Get URL"]])
