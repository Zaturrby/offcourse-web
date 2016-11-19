(ns offcourse.models.viewmodel.index
  (:require [shared.protocols.convertible :refer [Convertible]]
            [shared.protocols.queryable :refer [Queryable]]
            [offcourse.models.viewmodel.missing-data :as md-impl]
            [offcourse.models.viewmodel.to-route :refer [to-route]]
            [shared.specs.viewmodel :as specs]
            [shared.protocols.specced :as sp]
            [shared.protocols.loggable :as log]
            [cljs.spec :as spec]))

(defrecord Viewmodel []
  Queryable
  (-missing-data [this query] (md-impl/missing-data this query))
  Convertible
  (-to-route [this] (to-route this)))

(defn create [vm]
  (with-meta (map->Viewmodel vm) {:spec ::specs/viewmodel}))

(defmulti from-route (fn [params] (sp/resolve params)))

(defmethod from-route :signup-view []
  (create {:user {}}))

(defmethod from-route :checkpoint-view [params]
  (create {:course     (select-keys params [:curator :organization :course-slug])
           :checkpoint (select-keys params [:checkpoint-slug :checkpoint-id])}))

(defmethod from-route :new-course-view [params]
  (create {:course nil}))

(defmethod from-route :course-view [params]
  (create {:course (select-keys params [:curator :organization :course-slug])}))

(defmethod from-route :collection-view [params]
  (create {:collection (select-keys params [:collection-type :collection-name])}))

(defmethod from-route :home-view [params]
  (create {:collection {:collection-type "flags"
                        :collection-name "all"}}))
