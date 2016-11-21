(ns offcourse.models.appstate.index
  (:require [shared.specs.action :as specs]
            [shared.protocols.actionable :refer [Actionable]]
            [shared.protocols.queryable :refer [Queryable]]
            [offcourse.models.appstate.missing-data :as md]
            [offcourse.models.appstate.get :as get]
            [offcourse.models.appstate.perform :as perform]))

(defrecord Appstate []
  Actionable
  (-perform [as action] (perform/perform as action))
  Queryable
  (-get [as query] (get/get as query))
  (-missing-data [as query] (md/missing-data as query)))

(def defaults {:site-title "APP_"
               :app-mode   :viewing
               :user       nil
               :courses    []
               :resources  []
               :profiles   []})

(defn create [appstate]
  (-> defaults
      (merge appstate)
      map->Appstate
      (with-meta {:spec :offcourse/appstate})))
