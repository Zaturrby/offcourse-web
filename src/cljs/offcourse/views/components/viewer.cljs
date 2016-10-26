(ns offcourse.views.components.viewer
  (:require [markdown.core :refer [md->html]]
            [offcourse.views.components.meta-widget :refer [meta-widget]]
            [shared.protocols.loggable :as log]
            [rum.core :as rum]))

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
