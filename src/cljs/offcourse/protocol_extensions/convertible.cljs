(ns offcourse.protocol-extensions.convertible
  (:require [cuerdas.core :as str]
            [shared.models.checkpoint.index :refer [Checkpoint]]
            [shared.models.course.index :as co :refer [Course]]
            [shared.models.viewmodel.index :as viewmodel]
            [shared.protocols.convertible :as cv :refer [Convertible]]
            [shared.protocols.loggable :as log]
            [shared.models.route.index :as route]))

(extend-protocol Convertible
  Checkpoint
  (-to-url [{:keys [task] :as checkpoint} {:keys [goal] :as course} routes]
    (let [route (route/create {:course-slug (str/slugify goal)
                               :checkpoint-slug (str/slugify task)
                               :organization (:organization course)
                               :curator (:curator course)})]
      (cv/to-url route routes)))
  Course
  (-to-url [{:keys [goal] :as course} routes]
    (let [route (route/create {:course-slug (str/slugify goal)
                               :organization (:organization course)
                               :curator (:curator course)})]
      (cv/to-url route routes))))
