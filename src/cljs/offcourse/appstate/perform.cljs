(ns offcourse.appstate.perform
  (:require [shared.protocols.specced :as sp]
            [offcourse.appstate.check :as ck]
            [shared.protocols.eventful :as ef]
            [shared.protocols.loggable :as log]
            [shared.protocols.queryable :as qa]
            [shared.protocols.actionable :as ac]))

(defmulti perform (fn [as action] (sp/resolve action)))

(defmethod perform [:update :viewmodel] [{:keys [state] :as as} [_ viewmodel :as action]]
  (let [{:keys [viewmodel] :as proposal} (ac/perform @state action)]
    (when (ck/check as proposal)
      (reset! state proposal)
      (when-let [missing-data (qa/missing-data @state viewmodel)]
        (ef/respond as [:not-found missing-data]))
      (if (sp/valid? @state)
        (ef/respond as [:refreshed @state])
        (log/error @state (sp/errors @state))))))

(defmethod perform [:sign-in nil] [{:keys [state] :as as} [_ viewmodel :as action]]
  (ef/respond as [:requested action]))

(defmethod perform [:sign-out nil] [{:keys [state] :as as} [_ viewmodel :as action]]
  (ef/respond as [:requested action]))
