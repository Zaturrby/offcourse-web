(ns offcourse.views.components.button
  (:require [rum.core :as rum]
            [clojure.string :as str]
            [cljs.test :as test]
            [cljs.spec :as spec]
            [shared.protocols.loggable :as log]))

(spec/def ::button-type (spec/or :link string?
                                 :action any?))

(defmulti button (fn [content payload] (first (spec/conform ::button-type payload))))

(defmethod button :link [content url]
  [:a.button {:key (:button-text content)
              :data-button-color (or (:button-color content) "gray")
              :data-button-type "textbar"
              :href url
              :data-button-width (or (:button-width content) "default")}
   [:p  (:button-text content)]])

(defmethod button :action [content action]
  [:.button {:key (:button-text content)
             :data-button-color (or (:button-color content) "gray")
             :data-button-type "textbar"
             :on-click action
             :data-button-width (or (:button-width content) "default")}
   [:a  (:button-text content)]])
