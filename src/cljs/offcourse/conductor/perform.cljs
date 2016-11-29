(ns offcourse.conductor.perform
  (:require [shared.protocols.specced :as sp]
            [offcourse.conductor.check :as ck]
            [shared.protocols.eventful :as ef]
            [shared.protocols.loggable :as log]
            [shared.protocols.queryable :as qa]
            [shared.protocols.actionable :as ac]
            [shared.models.query.index :as query]))

(defmulti perform (fn [conductor action] (sp/resolve action)))

(defmethod perform [:create :profile] [{:keys [state] :as conductor} [_ profile]]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state [:add profile])]
    (when (ck/check conductor proposal)
      (reset! state proposal)
      (if (sp/valid? @state)
        (ef/respond conductor [:requested [:save (:user @state)]])
        (log/error @state (sp/errors @state))))))

(defmethod perform [:authenticate :provider] [{:keys [state] :as conductor} action]
  (ef/respond conductor [:requested [:authenticate (second action)]])
  (ac/perform conductor [:switch-to :view-mode]))

(defmethod perform [:sign-up :user] [{:keys [state] :as conductor} action]
  (ef/respond conductor [:requested [:sign-up (second action)]])
  (ac/perform conductor [:switch-to :view-mode]))

(defmethod perform [:sign-out nil] [{:keys [state] :as conductor} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (ef/respond conductor [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform [:go :home] [{:keys [state] :as conductor} action]
  (ef/respond conductor [:requested action]))

(defmethod perform [:add :identity] [{:keys [state] :as conductor} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (ef/respond conductor [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform [:fork :course] [{:keys [state] :as conductor} action]
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
      (ac/perform conductor [:switch-to :view-mode])
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
      (ef/respond conductor [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform [:switch-to :app-mode] [{:keys [state] :as conductor} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (ef/respond conductor [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform :default [conductor action]
  (log/error (sp/resolve action) "Conductor: Jan Hein hasn't implemented this action yet! Shame on him!"))
