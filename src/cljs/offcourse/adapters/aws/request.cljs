(ns offcourse.adapters.aws.request
  (:require [ajax.core :refer [POST]]
            [cljs.core.async :refer [chan]]
            [clojure.walk :as walk]
            [shared.models.event.index :as event]
            [shared.protocols.specced :as sp]
            [shared.specs.helpers :as sh]
            [shared.protocols.loggable :as log])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn handle-response [{:keys [statusCode body] :as payload}]
  (condp = statusCode
    202 {:accepted body}
    :else {:denied nil}))

(defn handle-error [{:keys [status response] :as payload}]
  (log/log "x" payload)
  {:denied nil})

(defn request [{:keys [name endpoint]} [action-type payload :as action]]
  (let [c (chan)
        auth-token (some-> action meta :auth-token)]
    (POST endpoint
          {:headers (when auth-token {:Authorization auth-token})
           :params {:action-type action-type}
           :format :json
           :handler #(go (>! c (handle-response (walk/keywordize-keys %))))
           :error-handler #(go (>! c (handle-error (walk/keywordize-keys %))))})
    c))
