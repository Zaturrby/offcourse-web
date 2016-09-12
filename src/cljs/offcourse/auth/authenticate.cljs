(ns offcourse.auth.authenticate
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


(defmulti react (fn [_ event] (sp/resolve event)))

(defmethod react [:requested :action] [{:keys [config provider] :as auth} [_ action]]
  (if (= (first action) :sign-in)
    (go
      (let [{:keys [token]} (<! (-sign-in provider))]
        (.setItem js/localStorage "auth-token" token)
        (ef/respond auth [:granted (credentials/create token)])))
    (do
      (.removeItem js/localStorage "auth-token")
      (ef/respond auth [:revoked (credentials/create nil)]))))
