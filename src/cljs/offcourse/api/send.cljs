(ns offcourse.api.send
  (:require [cljs.core.async :refer [<!]]
            [cljs.core.match :refer-macros [match]]
            [shared.protocols.convertible :as cv]
            [shared.protocols.eventful :as ef]
            [shared.protocols.specced :as sp]
            [shared.protocols.loggable :as log])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defmulti send (fn [_ event] (sp/resolve event)))

(defmethod send [:not-found :query]
  [{:keys [component-name repositories] :as api} [_ query :as event]]
  (doseq [{:keys [resources] :as repository} repositories]
    (when (contains? resources (sp/resolve query))
      (go
        (let [response (<! (ef/send repository (into [component-name] event)))]
          (match response
                 [:fetched _]  (ef/respond api [:found (-> response cv/to-models)])
                 [:error _]    (ef/respond api [:not-found query])
                 _ (ef/respond api [:failed query])))))))
