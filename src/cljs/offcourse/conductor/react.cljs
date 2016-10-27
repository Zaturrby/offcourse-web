(ns offcourse.conductor.react
  (:require [offcourse.conductor.check :as ck]
            [shared.protocols.actionable :as ac]
            [shared.protocols.loggable :as log]
            [shared.protocols.queryable :as qa]
            [shared.protocols.eventful :as ef]
            [shared.protocols.specced :as sp]
            [shared.models.query.index :as query]
            [shared.protocols.convertible :as cv]
            [shared.models.viewmodel.index :as viewmodel]))

(defmulti react (fn [_ [event-type _ :as event]] event-type))

(defmethod react :granted [{:keys [state] :as as} [_ payload]]
  (let [proposal (ac/perform @state [:add payload])]
    (reset! state proposal)
    (ef/respond as [:requested [:sign-in]])))

(defmethod react :revoked [{:keys [state] :as as} [_ payload]]
  (let [proposal (ac/perform @state [:add payload])]
    (when (sp/valid? proposal)
      (reset! state proposal)
      (ef/respond as [:requested [:go :home]]))))

(defmethod react :requested [as [_ action]]
  (ac/perform as action))

(defmethod react :refreshed [as [_ payload]]
  (ac/perform as [:update (viewmodel/from-route payload)]))

(defmethod react :found [as [_ payload]]
  (ac/perform as [:add payload]))

(defmethod react :not-found [{:keys [state] :as as} [_ query]]
  (when (= :user (sp/resolve query))
    (ef/respond as [:requested [:create :new-user]])))
