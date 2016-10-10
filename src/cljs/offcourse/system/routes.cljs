(ns offcourse.system.routes)

(def curator-routes    ["users/" :curator])
(def org-routes        ["org/" :organization])
(def new-course-route  "courses/new")
(def signup-route      "users/new")
(def course-routes     (conj curator-routes "/courses/" :course-slug))
(def course-org-routes (conj org-routes "/users/" :curator "/courses/" :course-slug))
(def collection-routes [:collection-type "/" :collection-name])
(def checkpoint-routes (conj course-routes "/checkpoints/" :checkpoint-slug))
(def checkpoint-org-routes (conj course-org-routes "/checkpoints/" :checkpoint-slug))

(def table ["/" {signup-route          :signup-view
                 new-course-route      :new-course-view
                 course-org-routes     :course-org-view
                 checkpoint-org-routes :checkpoint-org-view
                 checkpoint-routes     :checkpoint-view
                 collection-routes     :collection-view
                 course-routes         :course-view
                 true                  :home-view}])
