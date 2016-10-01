(ns offcourse.api.react
  (:require [shared.protocols.eventful :as ef]))

(defn react [{:keys [component-name] :as api} event]
  (ef/send api (into [component-name] event)))
