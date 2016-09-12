(ns offcourse.core
  (:require [offcourse.system.index :refer [system]]))

(defn app [appstate adapters]
  (system appstate
          adapters))
