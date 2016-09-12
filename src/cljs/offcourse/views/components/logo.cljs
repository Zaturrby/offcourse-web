(ns offcourse.views.components.logo
  (:require [rum.core :as rum]))

(rum/defc logo [{:keys [site-title] :as data}
                respond]
  [:.logo
   [:a {on-click #(respond [:go :home])}
    site-title]])
