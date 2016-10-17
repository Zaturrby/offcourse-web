(ns offcourse.views.components.card-meta
  (:require [rum.core :as rum]))

(rum/defc card-meta [course]
  [:.card--profile
    [:.card--profile-section
      [:img.card--profile-image {:src (str "/images/profilepics/" (:curator course) ".jpg")}]]
    [:.card--profile-section
      [:.card--profile-label "Curated by"]
      [:.card--profile-username (clojure.string/capitalize (:curator course))]]])
