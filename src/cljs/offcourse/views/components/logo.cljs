(ns offcourse.views.components.logo
  (:require [rum.core :as rum]))

(rum/defc logo [{:keys [site-title] :as data}
                respond]
  [:.menubar--logo
   [:a {on-click #(respond [:go :home])}
    site-title]])
