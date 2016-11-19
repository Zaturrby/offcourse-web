(ns offcourse.models.appstate.perform
  (:require [shared.models.query.index :as query]
            [shared.paths.index :as paths]
            [shared.models.course.index :as course]
            [com.rpl.specter :refer [ALL]]
            [shared.protocols.queryable :as qa]
            [shared.protocols.actionable :as ac]
            [shared.protocols.specced :as sp]
            [shared.protocols.loggable :as log]
            [cuerdas.core :as str]
            [shared.protocols.convertible :as cv])
  (:require-macros [com.rpl.specter.macros :refer [select-first setval transform]]))

(defn- add [store item]
  (if-not (qa/get store (cv/to-query item))
    (update-in store [(sp/resolve item)] #(conj % item))
    store))

(defmulti perform (fn [as action] (sp/resolve action)))

(defmethod perform [:update :viewmodel] [store [_ viewmodel]]
  (-> store (assoc :viewmodel viewmodel)))

(defmethod perform [:sign-out nil] [store [_ payload]]
  (dissoc store :user))

(defmethod perform [:add :credentials] [store [_ payload]]
  (assoc-in store [:user :credentials] (select-keys payload [:auth-token])))


(defmethod perform [:add :identity] [store [_ payload]]
  (update-in store [:user] #(merge %1 payload)))

(defmethod perform [:add :courses] [store [_ courses]]
  (reduce add store courses))

(defmethod perform [:update :course] [store [_ {:keys [revision] :as course}]]
  (let [course (assoc course :revision (inc revision))]
    (setval [:courses (paths/course course)] course store)))

(defmethod perform [:add :resource] [store [_ resource]]
  (add store resource))

(defmethod perform [:add :resources] [store [_ resources]]
  (reduce add store resources))

;; replace charlotte with real user here

(defmethod perform [:fork :course] [{:keys [courses user] :as store} [_ course]]
  (let [{:keys [original fork]} (ac/perform course [:fork {:user-name "charlotte"}])
        store                   (setval [:courses (paths/course course)] original store)]
    (-> store
        (add fork))))

(defmethod perform [:switch-to :app-mode] [{:keys [app-mode] :as store} [_ new-mode]]
  (assoc-in store [:app-mode] new-mode))

(defmethod perform [:update :checkpoint] [store [_ checkpoint]]
  (let [course-id (:course-id (meta checkpoint))
        checkpoint-id (:checkpoint-id checkpoint)
        checkpoint (assoc checkpoint :course-id course-id)]
    (setval [:courses ALL #(= (:course-id %) course-id)
             :checkpoints ALL #(= (:checkpoint-id %) checkpoint-id)]  checkpoint store)))

(defmethod perform [:add :course] [store [_ course]]
  (add store course))
