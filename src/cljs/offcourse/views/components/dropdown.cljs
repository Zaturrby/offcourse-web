(ns offcourse.views.components.dropdown
  (:require [rum.core :as rum]))

(rum/defc dropdown [message]
  [:.dropdown {:data-dropdown-shown (:shown message)}
    [:.dropdown-title (:title message)]
    [:.dropdown-text (:text message)]])
