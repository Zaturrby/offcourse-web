(ns offcourse.core
  (:require [offcourse.system.index :refer [system]]
            [offcourse.protocol-extensions.decoratable]
            [offcourse.protocol-extensions.loggable]
            [offcourse.protocol-extensions.convertible]
            [offcourse.specs.index]
            [cljs.spec :as spec]
            [shared.protocols.loggable :as log]))

(defn app [appstate adapters]
  (system appstate adapters))
