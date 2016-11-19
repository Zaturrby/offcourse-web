(ns offcourse.models.route.to-url
  (:require [bidi.bidi :as bidi]
            [shared.protocols.loggable :as log]
            [shared.protocols.specced :as sp]))

(defn needs-organization? [{:keys [organization] :as this}]
  (and organization (not (= organization "offcourse"))))

(defmulti to-url (fn [this routes]
                   (sp/resolve this)))

(defmethod to-url :signup-view [this routes]
  (bidi/path-for routes :signup-view))

(defmethod to-url :new-course-view [this routes]
  (bidi/path-for routes :new-course-view))

(defmethod to-url :course-view [this routes]
  (if (needs-organization? this)
    (bidi/path-for routes
                   :course-org-view
                   :curator (:curator this)
                   :course-slug (:course-slug this)
                   :organization (:organization this))
    (bidi/path-for routes
                   :course-view
                   :curator (:curator this)
                   :course-slug (:course-slug this))))

(defmethod to-url :checkpoint-view [this routes]
  (if (needs-organization? this)
    (bidi/path-for routes
                   :checkpoint-org-view
                   :curator (:curator this)
                   :course-slug (:course-slug this)
                   :checkpoint-slug (:checkpoint-slug this)
                   :organization (:organization this))
    (bidi/path-for routes
                   :checkpoint-view
                   :curator (:curator this)
                   :course-slug (:course-slug this)
                   :checkpoint-slug (:checkpoint-slug this))))

(defmethod to-url :collection-view [this routes]
  (bidi/path-for routes :collection-view
                 :collection-type (:collection-type this)
                 :collection-name (:collection-name this)))
