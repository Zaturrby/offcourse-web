(ns offcourse.views.components.viewer
  (:require [markdown.core :refer [md->html]]
            [shared.protocols.loggable :as log]
            [rum.core :as rum]))

(rum/defc viewer [{:keys [resource]} _ _]
  (log/log resource)
  [:.viewer
   (if-let [{:keys [title description content]} resource]
     [:.viewer--content
      [:h1.title {:key :title} title]
      [:article {:key :content
                 :dangerouslySetInnerHTML {:__html (md->html (or content description))}}]]
     [:.loading "This resource couldn't be found... yet..."])])

(rum/defc meta-widget [{:keys [resource checkpoint course]}]
  [:.meta-widget--container
   [:.meta-widget
    [:.meta-widget--section
     [:ul.meta-widget--list
      [:li.meta-widget--list-item {:data-item-type :todo
                                   :key            :task}
       [:h6.meta-widget--title "Task: "]
       [:p.meta-widget--field (:task checkpoint)]]
      [:li.meta-widget--list-item
       [:h6.meta-widget--title "Source: "]
       [:a {:key   :resource-url
            :href  (:resource-url checkpoint)}
        [:p.meta-widget--field (or (:provider-name resource) "(Only link)")]]]
      [:li.meta-widget--list-item
       [:h6.meta-widget--title "Publishing date: "]
       [:a {:key   :resource-url
            :href  (:resource-url checkpoint)}
        [:p.meta-widget--field (or (:published resource) "Unknown")]]]
      [:li.meta-widget--list-item 
       [:h6.meta-widget--title "Author: "]
       [:p.meta-widget--field (or (:author resource) "Unknown")]]]]]])