(ns offcourse.appstate.perform
  (:require [shared.protocols.specced :as sp]
            [offcourse.appstate.check :as ck]
            [shared.protocols.eventful :as ef]
            [shared.protocols.loggable :as log]
            [shared.protocols.queryable :as qa]
            [shared.protocols.actionable :as ac]
            [shared.models.query.index :as query]))

(defmulti perform (fn [as action]
                    (sp/resolve action)))

(defmethod perform [:create :profile] [{:keys [state] :as as} [_ profile]]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state [:add profile])]
    (when (ck/check as proposal)
      (reset! state proposal)
      (if (sp/valid? @state)
        (ef/respond as [:requested [:save (:user @state)]])
        (log/error @state (sp/errors @state))))))

(defmethod perform [:sign-in nil] [{:keys [state] :as as} [_ viewmodel :as action]]
  (ef/respond as [:requested action]))

(defmethod perform [:sign-out nil] [{:keys [state] :as as} [_ viewmodel :as action]]
  (ef/respond as [:requested action]))

(defmethod perform [:go :home] [{:keys [state] :as as} [_ viewmodel :as action]]
  (ef/respond as [:requested action]))

(defmethod perform [:add :profile] [{:keys [state] :as as} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (ef/respond as [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform [:fork :course] [{:keys [state] :as as} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (ef/respond as [:refreshed @state])
      ((ef/respond as [:refreshed @state])log/error @state (sp/errors @state)))))

(defmethod perform [:add :resources] [{:keys [state] :as as} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (ef/respond as [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform [:add :courses] [{:keys [state] :as as} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (ef/respond as [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform [:add :course] [{:keys [state] :as as} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (when-let [missing-data (qa/missing-data @state viewmodel)]
      (ef/respond as [:not-found (query/create missing-data)]))
    (if (sp/valid? proposal)
      (ef/respond as [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform [:update :viewmodel] [{:keys [state] :as as} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (when (ck/check as proposal)
      (reset! state proposal)
      (when-let [missing-data (qa/missing-data @state viewmodel)]
        (ef/respond as [:not-found (query/create missing-data)]))
      (if (sp/valid? proposal)
        (ef/respond as [:refreshed @state])
        (log/error @state (sp/errors @state))))))

(defmethod perform [:update :checkpoint] [{:keys [state] :as as} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (ef/respond as [:refreshed @state])
      (log/error @state (sp/errors @state)))))


(defmethod perform [:switch-to :app-mode] [{:keys [state] :as as} action]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (reset! state proposal)
    (if (sp/valid? proposal)
      (ef/respond as [:refreshed @state])
      (log/error @state (sp/errors @state)))))

(defmethod perform :default [as action]
  (log/error (sp/resolve action) "Jan Hein hasn't implemented this action yet! Shame on him!"))
