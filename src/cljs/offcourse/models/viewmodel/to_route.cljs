(ns offcourse.models.viewmodel.to-route
  (:require [shared.protocols.loggable :as log]
            [shared.protocols.specced :as sp]
            [offcourse.models.route.index :as route]))

(defmulti to-route (fn [vm] (sp/resolve vm)))

(defmethod to-route :signup-view [_]
  (route/create {:signup-view nil}))

(defmethod to-route :new-course-view [_]
  (route/create {:new-course-view nil}))

(defmethod to-route :course-view [{:keys [course]}]
  (route/create {:curator         (:curator course)
                 :course-slug     (:course-slug course)
                 :organization    (:organization course)}))

(defmethod to-route :checkpoint-view [{:keys [course checkpoint]}]
  (route/create {:curator         (:curator course)
                 :course-slug     (:course-slug course)
                 :checkpoint-slug (:checkpoint-slug checkpoint)
                 :organization    (:organization course)}))

(defmethod to-route :collection-view [{:keys [collection]}]
  (route/create collection))
