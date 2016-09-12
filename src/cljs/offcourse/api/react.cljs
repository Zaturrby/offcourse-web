(ns offcourse.api.react
  (:require [cljs.spec :as spec]
            [cljs.spec.test :as stest]
            [shared.protocols.eventful :as ef]
            [shared.specs.core :as specs]
            [shared.protocols.loggable :as log]))

(spec/fdef react :args (spec/cat :component any? :event ::specs/event))

(defn react [{:keys [component-name] :as api} event]
  (ef/send api (into [component-name] event)))

(stest/instrument `react)
