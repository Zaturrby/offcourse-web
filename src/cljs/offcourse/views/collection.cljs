(ns offcourse.views.collection
  (:require [clojure.set :as set]
            [offcourse.views.components.card :refer [cards]]
            [plumbing.core :refer-macros [fnk]]
            [shared.protocols.decoratable :as dc]
            [shared.protocols.loggable :as log]))

(defn filter-courses [{:keys [collection-name collection-type]} courses]
  courses
  #_(case collection-type
     "curators" (filter (fn [course]
                          (= collection-name (:curator course))) courses)
     "flags" (filter (fn [course]
                       (set/superset? (into #{} (:flags course)) #{collection-name})) courses)
     "tags" (filter (fn [course]
                      (set/superset? (-> course meta :tags) #{collection-name})) courses)))

(def graph
  {:view-actions   (fnk [] #{:toggle :fork :update :switch-to})
   :collection     (fnk [viewmodel] (get-in viewmodel [:collection]))
   :courses        (fnk [appstate viewmodel collection routes]
                        (->> (:courses appstate)
                             (map #(dc/decorate %1 appstate routes))
                             (filter-courses collection)))
   :main            (fnk [courses respond]
                        (when (not= '() courses)
                          (log/log "running cards!")
                          (cards {:courses courses} respond)))
   :view-overlays   (fnk [] {})})
