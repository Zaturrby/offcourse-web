(ns offcourse.core
  (:require [offcourse.system.index :refer [system]]
            [offcourse.protocol-extensions.decoratable]
            [offcourse.protocol-extensions.loggable]
            [offcourse.protocol-extensions.convertible]
            [offcourse.specs.event]
            [offcourse.specs.action]))

(defn app [appstate adapters]
  (system appstate adapters))
