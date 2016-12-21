(ns offcourse.conductor.perform
  (:require [shared.protocols.specced :as sp]
            [offcourse.conductor.check :as ck]
            [shared.protocols.eventful :as ef]
            [shared.protocols.loggable :as log]
            [shared.protocols.queryable :as qa]
            [shared.protocols.actionable :as ac]
            [shared.protocols.convertible :as cv]
            [cuerdas.core :as str]
            [shared.models.query.index :as query]))

(defmulti perform (fn [conductor action] (sp/resolve action)))

(defmethod perform [:authenticate :provider] [{:keys [state] :as conductor} [_ provider]]
  (ef/respond conductor [:requested [:authenticate provider]])
  (ac/perform conductor [:switch-to :view-mode]))

(defmethod perform [:sign-up :user] [{:keys [state] :as conductor} [_ user]]
  (ef/respond conductor [:requested [:sign-up user]])
  (ac/perform conductor [:switch-to :view-mode]))

(defmethod perform [:sign-out nil] [{:keys [state] :as conductor} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (ef/respond conductor [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform [:go :home] [{:keys [state] :as conductor} action]
  (ef/respond conductor [:requested action]))

(defmethod perform [:switch-to :app-mode] [{:keys [state] :as conductor} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (ef/respond conductor [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform [:add :identity] [{:keys [state] :as conductor} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (ef/respond conductor [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform [:add :resource] [{:keys [state] :as conductor} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (ef/respond conductor [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform [:add :resources] [{:keys [state] :as conductor} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (ef/respond conductor [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform [:add :courses] [{:keys [state] :as conductor} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (ef/respond conductor [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform [:add :course] [{:keys [state] :as conductor} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (when-let [missing-data (qa/missing-data @state viewmodel)]
      (ef/respond conductor [:not-found (query/create missing-data)]))
    (if (sp/valid? proposal)
      (ef/respond conductor [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform [:update :course] [{:keys [state] :as conductor} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (when-let [missing-data (qa/missing-data @state viewmodel)]
      (ef/respond conductor [:not-found (query/create missing-data)]))
    (if (sp/valid? proposal)
      (do
        (ac/perform conductor [:switch-to :view-mode]) ; no side effect, can be done on appstate lvl
        (ef/respond conductor [:requested [:add (second action)]]))
      (log/error @state (sp/errors @state)))))

(defmethod perform [:update :viewmodel] [{:keys [state] :as conductor} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (when (ck/check conductor proposal)
      (reset! state proposal)
      (when-let [missing-data (qa/missing-data @state viewmodel)]
        (ef/respond conductor [:not-found (query/create missing-data)]))
      (if (sp/valid? proposal)
        (ef/respond conductor [:refreshed @state])
        (log/error @state (sp/errors @state))))))

(defmethod perform [:update :checkpoint] [{:keys [state] :as conductor} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (let [new-state     @state
            course-id     (:course-id (meta (second action)))
            course-query  (query/create {:course-id course-id})
            course        (qa/get new-state course-query)]
          (ef/respond conductor [:requested [:add course]])
          (ef/respond conductor [:refreshed @state]))
      (log/error @state (sp/errors @state)))))

(defmethod perform [:create :profile] [{:keys [state] :as conductor} [_ profile]]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state [:add profile])]
    (when (ck/check conductor proposal)
      (reset! state proposal)
      (if (sp/valid? @state)
        (ef/respond conductor [:requested [:save (:user @state)]])
        (log/error @state (sp/errors @state))))))

(defmethod perform [:create :new-course] [{:keys [state] :as conductor} [_ new-course]]
  (let [proposal (ac/perform @state [:create new-course])]
    (reset! state proposal)
    (if (sp/valid? @state)
      (let [new-state     @state
            course-slug   (str/slugify (:goal new-course))
            curator       (str/slugify (:curator new-course))
            course-query  (query/create {:course-slug course-slug
                                         :curator curator})
            course        (qa/get new-state course-query)]
          (ef/respond conductor [:requested [:add course]])
          (ef/respond conductor [:requested [:go :home]]))
      (log/error @state (sp/errors @state)))))

(defmethod perform [:create :course] [{:keys [state] :as conductor} [_ course]]
  (ac/perform conductor [:update course]))

(defmethod perform [:fork :course] [{:keys [state] :as conductor} action]
  (let [course (second action)
        {:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (let [new-state     @state
            user-name     (-> new-state :user :user-name)
            course-query  (cv/to-query course)
            fork-query    (dissoc (assoc course-query :curator user-name) :course-id)
            fork          (qa/get new-state fork-query)]
        (ef/respond conductor [:requested [:add fork]])
        (ef/respond conductor [:refreshed @state]))
      (log/error @state (sp/errors @state)))))

(defmethod perform :default [conductor action]
  (log/error (sp/resolve action) "Conductor: Jan Hein hasn't implemented this action yet! Shame on him!"))
