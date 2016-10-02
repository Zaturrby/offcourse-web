(ns offcourse.api.send
  (:require [cljs.core.async :refer [<!]]
            [cljs.core.match :refer-macros [match]]
            [shared.protocols.convertible :as cv]
            [shared.protocols.eventful :as ef]
            [shared.protocols.specced :as sp]
            [shared.protocols.loggable :as log]
            [shared.protocols.queryable :as qa]
            [shared.models.payload.index :as payload])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defmulti send (fn [_ [event-type _]] event-type))

(defmethod send :not-found
  [{:keys [component-name repositories] :as api} [_ query :as event]]
  (doseq [{:keys [resources] :as repository} repositories]
    (when (contains? resources (sp/resolve query))
      (go
        (if-let [response (<! (qa/fetch repository query))]
          (ef/respond api [:found (-> response payload/create cv/to-model)])
          (ef/respond api [:failed query]))))))
