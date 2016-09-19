(ns offcourse.protocol-extensions.decoratable
  (:require [cuerdas.core :as str]
            [plumbing.core :refer-macros [fnk]]
            [plumbing.graph :as graph]
            [shared.models.checkpoint.index :refer [Checkpoint]]
            [shared.models.course.index :as co :refer [Course]]
            [shared.protocols.convertible :as cv]
            [shared.protocols.decoratable :as dc :refer [Decoratable]]
            [shared.protocols.queryable :as qa]
            [shared.protocols.loggable :as log]))

(defn compute [graph graph-data]
  ((graph/compile graph) graph-data))

(def affordances-graph
  {:browsable? (fnk [] true)
   :forkable?  (fnk [current-user user-is-curator?]
                    (and current-user (not user-is-curator?)))
   :editable?  (fnk [user-is-curator?] user-is-curator?)
   :trackable? (fnk [user-is-curator?] user-is-curator?)})

(def course-meta-graph
  {:tags             (fnk [course]
                          (qa/get course {:tags :all}))
   :course-url       (fnk [course routes]
                          (cv/to-url course routes))
   :current-user     (fnk [appstate] (when-let [user (:user appstate)]
                                       (:user-name user)))
   :course-curator   (fnk [course] (:curator course))
   :user-is-forker   (fnk [current-user course] false)
   :user-is-curator? (fnk [current-user course-curator]
                          (and current-user (= course-curator current-user)))
   :affordances      (fnk [user-is-curator? current-user]
                          (compute affordances-graph {:user-is-curator? user-is-curator?
                                                      :current-user     current-user}))})

(extend-protocol Decoratable
  Checkpoint
  (-decorate [{:keys [task] :as checkpoint} {:keys [selected course]} routes]
    (let [checkpoint-url  (cv/to-url checkpoint course routes)
          checkpoint-slug (str/slugify task)]
      (if (= selected checkpoint-slug)
        (with-meta checkpoint {:selected       true
                               :checkpoint-url checkpoint-url})
        (with-meta checkpoint {:checkpoint-url checkpoint-url}))))
  Course
  (-decorate [{:keys [checkpoints curator] :as course} appstate routes]
    (let [course-meta (compute course-meta-graph {:course   course
                                                  :routes   routes
                                                  :appstate appstate})]
      (some-> course
              (assoc :checkpoints (map #(dc/decorate %1 {:selected (:checkpoint-slug %)
                                                         :course course} routes)
                                       checkpoints))
              (with-meta course-meta)))))
