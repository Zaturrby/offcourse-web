(ns offcourse.adapters.aws.send
  (:require [ajax.core :refer [POST]]
            [cljs.core.async :refer [chan]]
            [shared.models.event.index :as event]
            [shared.protocols.loggable :as log]
            [shared.protocols.specced :as sp]
            [cljs.core.async :as async]
            [clojure.walk :as walk])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn handle-response [name res]
  (event/create [name :fetched (walk/keywordize-keys res)]))

(defn send [{:keys [name endpoint]} [_ query :as event]]
  (let [c (chan)
        auth-token ""]
    (POST endpoint
        {:headers {:Authorization (str "Bearer " auth-token)}
         :params (clj->js {:type :request-data
                           :payload query})
         :format :json
         :handler #(go (>! c (handle-response name %)))})
    c))
