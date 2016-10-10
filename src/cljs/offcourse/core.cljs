(ns offcourse.core
  (:require [offcourse.system.index :refer [system]]
            [offcourse.specs.action]))

(defn app [appstate adapters]
  (system appstate
          adapters))
