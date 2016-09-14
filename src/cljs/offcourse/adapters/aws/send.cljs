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
  (log/log "R" res)
  (event/create [name :not-found (walk/keywordize-keys res)]))

(defn send [{:keys [name endpoint]} [event-type query :as event]]
  (let [c (chan)
        auth-token ""]
    (POST endpoint
        {:headers {:Authorization (str "Bearer " auth-token)}
         :params {:event-type event-type
                  :payload-type (sp/resolve query)
                  :payload query}
         :format :json
         :handler #(go (>! c (handle-response name %)))})
    c))
