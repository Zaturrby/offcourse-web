(ns offcourse.appstate.react
  (:require [offcourse.appstate.check :as ck]
            [shared.protocols.actionable :as ac]
            [shared.protocols.loggable :as log]
            [shared.protocols.queryable :as qa]
            [shared.protocols.eventful :as ef]
            [shared.protocols.specced :as sp]
            [shared.models.query.index :as query]))

(defmulti react (fn [_ event]
                  (sp/resolve event)))

(defmethod react [:granted :data] [{:keys [state] :as as} [_ payload]]
  (let [proposal (ac/perform @state [:add payload])]
    (when (ck/check as proposal)
      (reset! state proposal)
      (ef/respond as [:not-found (query/create (get-in proposal [:user]))]))))

(defmethod react [:revoked :data] [{:keys [state] :as as} [_ payload]]
  (let [proposal (ac/perform @state [:add payload])]
    (when (sp/valid? proposal)
      (reset! state proposal)
      (ef/respond as [:refreshed @state]))))

(defmethod react [:requested :action] [{:keys [state] :as as} [_ action]]
  (ac/perform as action))

(defmethod react [:found :data] [{:keys [state] :as as} [_ payload]]
  (let [proposal (ac/perform @state [:add payload])]
    (when (sp/valid? proposal)
      (reset! state proposal)
      (ef/respond as [:refreshed @state]))))

(defmethod react [:not-found :data] [{:keys [state] :as as} [_ payload]]
  (log/error payload "missing-data")
  #_(when-not (-> @state :user :user-name)
      (rd/redirect as :signup)))
