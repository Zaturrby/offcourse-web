(ns offcourse.adapters.aws.request
  (:require [ajax.core :refer [POST]]
            [cljs.core.async :refer [chan]]
            [clojure.walk :as walk]
            [shared.models.event.index :as event]
            [shared.protocols.specced :as sp]
            [shared.specs.helpers :as sh]
            [shared.protocols.loggable :as log])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn handle-response [name payload]
  (walk/keywordize-keys payload))

(defn request [{:keys [name endpoint]} [action-type payload :as action]]
  (let [c (chan)
        auth-token (some-> action meta :auth-token)]
    (POST endpoint
          {:headers (when auth-token {:Authorization auth-token})
           :params {:action-type action-type}
           :format :json
           :handler #(go (>! c (handle-response name %)))})
    c))
