(ns offcourse.models.viewmodel.missing-data
  (:require [shared.protocols.specced :as sp]
            [shared.protocols.queryable :as qa]
            [shared.models.query.index :as query]
            [shared.protocols.loggable :as log]
            [shared.models.bookmark.index :as bookmark]))

(defmulti missing-data (fn [viewmodel _] (sp/resolve viewmodel)))

(defmethod missing-data :signup-view [viewmodel state]
  false)

(defmethod missing-data :new-course-view [viewmodel state]
  false)

(defmethod missing-data :collection-view [viewmodel state]
  (query/create (:collection viewmodel)))

(defmethod missing-data :course-view [viewmodel state]
  (let [course-query (-> viewmodel :course)
        course       (qa/get state course-query)
        checkpoint   (when course (first (:checkpoints course)))
        bookmark     (bookmark/create {:resource-url  (:resource-url checkpoint)
                                       :curator       (:curator course)
                                       :course-id     (:course-id course)
                                       :checkpoint-id (:checkpoint-id checkpoint)})]
    (if course
      (qa/missing-data state bookmark)
      (query/create course-query))))

(defmethod missing-data :checkpoint-view [viewmodel state]
  (let [course-query (-> viewmodel :course)
        cp-query     (-> viewmodel :checkpoint)
        course       (qa/get state course-query)
        checkpoint   (when course (qa/get course cp-query))
        bookmark     (bookmark/create {:resource-url  (:resource-url checkpoint)
                                       :curator       (:curator course)
                                       :course-id     (:course-id course)
                                       :checkpoint-id (:checkpoint-id checkpoint)})]
    (if course
      (qa/missing-data state bookmark)
      (query/create course-query))))
