(ns offcourse.conductor.react
  (:require [offcourse.conductor.check :as ck]
            [shared.protocols.actionable :as ac]
            [shared.protocols.loggable :as log]
            [shared.protocols.queryable :as qa]
            [shared.protocols.eventful :as ef]
            [shared.protocols.specced :as sp]
            [shared.models.query.index :as query]
            [shared.protocols.convertible :as cv]
            [offcourse.models.viewmodel.index :as viewmodel]))

(defmulti react (fn [_ [event-type _ :as event]] event-type))

(defmethod react :granted [{:keys [state] :as conductor} [_ payload]]
  (let [proposal (ac/perform @state [:add payload])]
    (reset! state proposal)
    (ef/respond conductor [:requested [:sign-in]])))

(defmethod react :signed-in [{:keys [state] :as conductor} [_ payload]]
  (ac/perform conductor [:add payload]))

(defmethod react :revoked [{:keys [state] :as conductor} [_ payload]]
  (let [proposal (ac/perform @state [:remove payload])]
    (when (sp/valid? proposal)
      (reset! state proposal)
      (ef/respond conductor [:requested [:go :home]]))))

(defmethod react :requested [conductor [_ action]]
  (ac/perform conductor action))

(defmethod react :refreshed [conductor [_ payload]]
  (ac/perform conductor [:update (viewmodel/from-route payload)]))

(defmethod react :found [conductor [_ payload]]
  (ac/perform conductor [:add payload]))

(defmethod react :not-found [{:keys [state] :as conductor} [_ payload]]
  (when (= :credentials (sp/resolve payload))
    (ac/perform conductor [:switch-to :new-user])))
