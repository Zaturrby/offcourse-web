(ns offcourse.views.components.card-meta
  (:require [rum.core :as rum]))

(rum/defc card-meta [course]
  [:.card-meta
    [:.card-meta--section
      [:img.card-meta--image {:src (str "/images/profilepics/" (:curator course) ".jpg")}]]
    [:.card-meta--section
      [:.card-meta--label "Curated by"]
      [:.card-meta--username (clojure.string/capitalize (:curator course))]]])
