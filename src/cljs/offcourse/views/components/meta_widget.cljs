(ns offcourse.views.components.meta-widget
  (:require [markdown.core :refer [md->html]]
            [offcourse.views.components.button :refer [button]]
            [shared.protocols.loggable :as log]
            [rum.core :as rum]))

(rum/defc meta-widget [{:keys [resource checkpoint]}]
  [:.meta-widget--container
   [:.meta-widget
    (log/log checkpoint)
    [:.meta-widget--section
     [:ul.meta-widget--list
      [:li.meta-widget--list-item
       [:div.button {:key "source"
                     :data-button-type "textbar"}
        [:a {:href (:resource-url checkpoint)
             :target "_blank"}
            "View on Source"]]]
      [:li.meta-widget--list-item {:data-item-type :todo
                                   :key            :task}
       [:h6.meta-widget--title "Task: "]
       [:p.meta-widget--field (:task checkpoint)]]
      (when resource
       [(when-let [provider-name (:provider-name resource)]
         [:li.meta-widget--list-item
          [:h6.meta-widget--title "Source: "]
          [:a {:key :resource-url
               :href (:resource-url checkpoint)}
           [:p.meta-widget--field provider-name]]])]
       [(when-let [published (:published resource)]
         [:li.meta-widget--list-item
          [:h6.meta-widget--title "Publishing date: "]
          [:a {:key :resource-url
               :href (:resource-url checkpoint)}
           [:p.meta-widget--field published]]])]
       [(when-let [author (:author resource)]
         [:li.meta-widget--list-item
          [:h6.meta-widget--title "Author: "]
          [:p.meta-widget--field author]])])]]]])
