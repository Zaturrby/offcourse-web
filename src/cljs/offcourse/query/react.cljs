(ns offcourse.query.react
  (:require [shared.protocols.eventful :as ef]
            [shared.protocols.specced :as sp]
            [shared.protocols.queryable :as qa]
            [shared.models.payload.index :as payload]
            [shared.protocols.convertible :as cv]
            [shared.protocols.loggable :as log]
            [cljs.core.async :as async])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defmulti react (fn [_ [event-type query]] (sp/resolve query)))

(defmethod react :default
  [{:keys [component-name repositories] :as service} [_ query :as event]]
  (doseq [{:keys [resources] :as repository} repositories]
    (when (contains? resources (sp/resolve query))
      (go
        (let [response (async/<! (qa/fetch repository query))]
          (if (not (:not-found response))
            (ef/respond service [:found (-> response payload/create cv/to-model)])
            (ef/respond service [:not-found query])))))))
