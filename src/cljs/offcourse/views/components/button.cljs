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
  [:.button {:key (:button-text content)
               :data-button-color (or (:button-color content) "gray")
               :data-button-type "textbar"}
   [:a {:href url} (:button-text content)]])

(defmethod button :action [content action]
  (log/log content)
  [:.button {:key (:button-text content)
               :data-button-color (or (:button-color content) "gray")
               :data-button-type "textbar"}
   [:a {:on-click action} (:button-text content)]])
