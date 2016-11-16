(ns offcourse.auth.react
  (:require [cljs.core.async :as async :refer [<! >! chan]]
            [shared.protocols.eventful :as ef]
            [shared.protocols.specced :as sp]
            [shared.models.credentials.index :as credentials]
            [shared.protocols.loggable :as log])
  (:require-macros [cljs.core.async.macros :refer [go]]))


(defn -sign-in [provider]
  (let [c (chan)]
    (.show provider (fn [error response token]
                      (async/put! c {:error error
                                     :response response
                                     :token token})))
    c))


(defn marshal [obj] (.stringify js/JSON obj))

(defmulti react (fn [_ [event-type _]] event-type))

(defmethod react :requested [{:keys [config provider] :as auth} [_ action]]
  (case (first action)
    :authenticate (go
                    (let [{:keys [token response]} (<! (-sign-in provider))
                          profile (js->clj response :keywordize-keys true)]
                      (ef/respond auth [:granted (credentials/create {:auth-token token
                                                                      :auth-profile profile})])))
    :sign-out (do
                (ef/respond auth [:revoked (credentials/create {:auth-token nil})]))
    nil))
