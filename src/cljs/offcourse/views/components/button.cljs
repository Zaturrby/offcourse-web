(ns offcourse.views.components.button
  (:require [rum.core :as rum]
            [clojure.string :as str]
            [cljs.test :as test]
            [cljs.spec :as spec]))

(spec/def ::button-type (spec/or :link string?
                                 :action any?))

(defmulti button (fn [button-text payload] (first (spec/conform ::button-type payload))))

(defmethod button :link [button-text url]
  [:li.button {:key button-text
               :data-button-type "textbar"}
   [:a {:href url} button-text]])

(defmethod button :action [button-text action]
  [:li.button {:key button-text
               :data-button-type "textbar"}
   [:a {:on-click action} button-text]])


