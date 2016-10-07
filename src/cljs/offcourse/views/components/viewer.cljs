(ns offcourse.views.components.viewer
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
        [:a {:href (:resource-url resource)
             :target "_black"}
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

(def youtube-regex #"(?:http?s?://)?(?:www\.)?(?:youtube\.com|youtu\.be)/(?:watch\?v=)?(.+)")

(rum/defc viewer [{:keys [resource checkpoint]} _ _]
  [:.viewer
   (if-let [{:keys [title description content resource-url]} resource]
     [:.viewer--section
      [:.viewer--main
       [:.viewer--content
        [:h1.title {:key :title} (or title "--- no title ---")]
        (if content
         [:article {:key :content
                    :dangerouslySetInnerHTML {:__html (md->html content)}}]
         [[:article {:key :content
                     :dangerouslySetInnerHTML {:__html (md->html (or (str "<strong>Description:</strong> " description) "<strong> No description available </strong>"))}}]])]
       (when-let [video-id (get (re-find youtube-regex resource-url) 1)]
        [:.viewer--video-container
         [:iframe.viewer--video {:allow-full-screen true
                                 :frame-border 0
                                 :src (str "http://www.youtube.com/embed/" video-id)}]])]]

     [:.viewer--section
      [:.viewer--loading
       [:.viewer--loading-img]
       [:.viewer--main]]])
   [:.viewer--section (meta-widget {:checkpoint checkpoint
                                    :resource resource})]])
