(ns offcourse.command.react
  (:require [shared.protocols.eventful :as ef]
            [shared.protocols.specced :as sp]
            [shared.protocols.queryable :as qa]
            [shared.models.payload.index :as payload]
            [shared.protocols.convertible :as cv]
            [cljs.core.async :as async]
            [shared.protocols.loggable :as log]
            [shared.protocols.actionable :as ac])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defmulti react (fn [_ [_ action]] (sp/resolve action)))

(defmethod react :authenticate [])
(defmethod react :go [])
(defmethod react :sign-out [])

(defmethod react :sign-in
  [{:keys [component-name adapter] :as service} [_ action]]
  (go
    (let [credentials (some-> action meta :credentials)
          request (ac/request adapter (with-meta action credentials))
          {:keys [accepted denied] :as res} (async/<! request)]
      (when accepted (ef/respond service [:signed-in (-> accepted payload/create)]))
      (when denied (ef/respond service [:not-found (payload/create credentials)])))))

(defmethod react :sign-up
  [{:keys [component-name adapter] :as service} [_ action]]
  (go
    (let [auth-token (some-> action meta :credentials :auth-token)
          auth-profile (some-> action second :credentials :auth-profile)
          user-name (some-> action second :user-name)
          action [(first action) {:user-name user-name :auth-profile auth-profile}]
          request (ac/request adapter (with-meta action {:auth-token auth-token}))
          {:keys [accepted denied]} (async/<! request)]
      (when accepted (ef/respond service [:signed-in (-> accepted payload/create)]))
      (when denied (ef/respond service [:failed [:command :sign-up]])))))

(defmethod react :update ; perhaps add course?
  [{:keys [component-name adapter] :as service} [_ action]]
  (go
    (let [credentials (some-> action meta :credentials)
          request (ac/request adapter (with-meta action credentials))
          {:keys [accepted denied] :as res} (async/<! request)]
      (when accepted (ef/respond service [:succeded [:command :update]]))
      (when denied (ef/respond service [:failed [:command :update]])))))
