(ns offcourse.views.components.viewer
  (:require [markdown.core :refer [md->html]]
            [rum.core :as rum]))

(rum/defc viewer [{:keys [resource]} _ _]
  [:.viewer
   (if-let [{:keys [title content]} resource]
     [:.viewer--content
      [:h1.title {:key :title} title]
      [:article {:key :content
                 :dangerouslySetInnerHTML {:__html (md->html content)}}]]
     [:.loading "This resource couldn't be found... yet..."])])

(rum/defc meta-widget []
  [:.meta-widget--container
   [:.meta-widget
    [:.meta-widget--section
     [:ul.meta-widget--list
      [:li.meta-widget--list-item {:data-item-type :todo
                                   :key            :task}
       [:a {:key   :title
            :href "some href"}
        [:h6.meta-widget--title "Task: "]
        [:p.meta-widget--field "Some task"]]]
      [:li.meta-widget--list-item
       [:h6.meta-widget--title "Source: "]
       [:p.meta-widget--field "Smashing Magazine"]]
      [:li.meta-widget--list-item 
       [:h6.meta-widget--title "Author: "]
       [:p.meta-widget--field "John Doehingy"]]
      [:li.meta-widget--list-item
       [:h6.meta-widget--title "Date: "]
       [:p.meta-widget--field "05-08-2019"]]]]]])
