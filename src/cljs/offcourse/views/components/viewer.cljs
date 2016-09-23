(ns offcourse.views.components.viewer
  (:require [markdown.core :refer [md->html]]
            [shared.protocols.loggable :as log]
            [rum.core :as rum]))

(def lorem "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod
tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo
consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non
proident, sunt in culpa qui officia deserunt mollit anim id est laborum.

Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod
tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo
consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non
proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")

(rum/defc viewer [{:keys [resource]} _ _]
  [:.viewer
   (if-let [{:keys [title content]} resource]
     [:.viewer--content
      [:h1.title {:key :title} title]
      [:article {:key :content
                 :dangerouslySetInnerHTML {:__html (md->html #_content lorem)}}]]
     [:.loading "This resource couldn't be found... yet..."])])

(rum/defc meta-widget [checkpoint course]
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
        [:p.meta-widget--field "(link)"]]]
      [:li.meta-widget--list-item 
       [:h6.meta-widget--title "Author: "]
       [:p.meta-widget--field (clojure.string/capitalize (:curator course))]]]]]])
