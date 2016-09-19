(ns offcourse.views.collection
  (:require [clojure.set :as set]
            [offcourse.views.components.card :refer [cards]]
            [plumbing.core :refer-macros [fnk]]
            [shared.protocols.decoratable :as dc]
            [shared.protocols.loggable :as log]))

(defn filter-courses [{:keys [collection-name collection-type]} courses]
  (case collection-type
    "curators" (filter (fn [course]
                         (= collection-name (:curator course))) courses)
    "flags" (filter (fn [course]
                      (set/superset? (into #{} (:flags course)) #{collection-name})) courses)
    "tags" (filter (fn [course]
                     (set/superset? (-> course meta :tags) #{collection-name})) courses)))

(def graph
  {:collection   (fnk [viewmodel] (get-in viewmodel [:collection]))
   :courses      (fnk [appstate user-name viewmodel collection routes]
                      (->> (:courses appstate)
                           (map #(dc/decorate %1 appstate routes))
                           (filter-courses collection)))
   :view-actions (fnk [] #{:toggle :fork :mark-complete :mark-incomplete})
   :main         (fnk [courses respond]
                      (cards {:courses courses} respond))})
