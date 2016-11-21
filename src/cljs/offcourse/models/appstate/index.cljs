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

(defn create [appstate]
  (-> {:site-title "Offcourse_"}
      (merge appstate)
      map->Appstate
      (with-meta {:spec :offcourse/appstate})))
