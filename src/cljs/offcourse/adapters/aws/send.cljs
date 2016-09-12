(ns offcourse.adapters.aws.send
  (:require [ajax.core :refer [POST]]
            [cljs.core.async :refer [chan]]
            [shared.models.event.index :as event]
            [shared.protocols.loggable :as log]
            [shared.protocols.specced :as sp]
            [cljs.core.async :as async])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn handle-response [name res]
  (let [{:keys [type payload]} (js->clj (.parse js/JSON res) :keywordize-keys true)]
    (event/create [name (keyword type) payload])))


(defn send [{:keys [name endpoint]} [_ query :as event]]
  (let [c (chan)
        auth-token ""]
    (async/put! c (event/create [name :fetched {:user-name "Yeehaa"}]))
    #_(POST endpoint
        {:headers {:Authorization (str "Bearer " auth-token)}
         :params (clj->js {:type :request-data
                           :payload query})
         :format :json
         :handler #(go (>! c (handle-response name %)))})
    c))
