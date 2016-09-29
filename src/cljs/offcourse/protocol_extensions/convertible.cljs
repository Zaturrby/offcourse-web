(ns offcourse.protocol-extensions.convertible
  (:require [cuerdas.core :as str]
            [shared.models.checkpoint.index :refer [Checkpoint]]
            [shared.models.course.index :as co :refer [Course]]
            [shared.models.viewmodel.index :as viewmodel]
            [shared.protocols.convertible :as cv :refer [Convertible]]
            [shared.protocols.loggable :as log]
            [shared.models.route-params.index :as route-params]))

(extend-protocol Convertible
  Checkpoint
  (-to-url [{:keys [task] :as checkpoint} {:keys [goal] :as course} routes]
    (let [viewmodel (viewmodel/create {:course (assoc course :course-slug (str/slugify goal))
                                       :checkpoint (assoc checkpoint :checkpoint-slug (str/slugify task))})]
      (cv/to-url viewmodel routes)))
  Course
  (-to-url [{:keys [goal] :as course} routes]
    (let [viewmodel (viewmodel/create {:course (assoc course :course-slug (str/slugify goal))})]
      (cv/to-url viewmodel routes))))
