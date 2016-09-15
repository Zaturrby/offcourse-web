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


(defn marshal [obj] (.stringify js/JSON obj))

(defmulti react (fn [_ event] (sp/resolve event)))

(defmethod react [:requested :action] [{:keys [config provider] :as auth} [_ action]]
  (case (first action)
    :sign-in (go
               (let [{:keys [token response]} (<! (-sign-in provider))
                     profile (js->clj response :keywordize-keys true)]
                 (when token (.setItem js/localStorage "auth-token" token))
                 (when response (.setItem js/localStorage "auth-profile" (marshal response)))
                 (ef/respond auth [:granted (credentials/create token profile)])))
    :sign-out (do
                (.removeItem js/localStorage "auth-token")
                (.removeItem js/localStorage "auth-profile")
                (ef/respond auth [:revoked (credentials/create nil {})]))
    nil))
