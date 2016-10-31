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

(defmethod react :granted [{:keys [state] :as as} [_ payload]]
  (let [proposal (ac/perform @state [:add payload])]
    (reset! state proposal)
    (ef/respond as [:requested [:sign-in]])))
    ; Todo:
    ; The auth profile dropped here. It's available in the payload, but doesn't
    ; get onto the proposal nor is it passed along. I'd suggest we pass it along with the :sign-in.
    ; If the sign-in fails the auth-profile gets returned to the conductor in the not-found below.
    ; Else it's dropped there, when a valid identity is retrieved from the API.

(defmethod react :signed-in [{:keys [state] :as as} [_ payload]]
  (ac/perform as [:add payload]))

(defmethod react :revoked [{:keys [state] :as as} [_ payload]]
  (let [proposal (ac/perform @state [:remove payload])]
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
    ; Todo:
    ; Add an identity and profile to the appstate here and call [requested [:switch-to :sign-up]]
    ; Identity probably will need some setup with (identity/create ...). The same goes for profile
    ; (profile/create ...), this is possible as the query (probably should be renamed) will
    ; contain the auth-token and profile because command will be able to return them.
