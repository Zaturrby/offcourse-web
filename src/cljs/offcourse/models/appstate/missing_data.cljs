(ns offcourse.models.appstate.missing-data
  (:refer-clojure :exclude [get -reset remove])
  (:require [clojure.set :as set]
            [shared.models.query.index :as query]
            [shared.protocols.queryable :as qa]
            [cljs.spec :as spec]
            [shared.specs.core :as specs]
            [shared.protocols.loggable :as log]
            [shared.protocols.specced :as sp]
            [shared.models.bookmark.index :as bookmark]
            [offcourse.models.viewmodel.index :as viewmodel]))

(defmulti missing-data (fn [state query] (sp/resolve query)))

(defmethod missing-data :viewmodel [state viewmodel]
  (qa/missing-data (viewmodel/create viewmodel) state))

(defmethod missing-data :resource [{:keys [resources]} {:keys [resource-url] :as bookmark}]
  (let [resource-urls (into #{} (map :resource-url resources))]
    (when-not (contains? resource-urls resource-url)
      bookmark)))
