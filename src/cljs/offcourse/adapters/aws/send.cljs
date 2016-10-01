(ns offcourse.adapters.aws.send
  (:require [ajax.core :refer [POST]]
            [cljs.core.async :refer [chan]]
            [clojure.walk :as walk]
            [shared.models.event.index :as event]
            [shared.protocols.specced :as sp]
            [shared.specs.helpers :as sh])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn handle-response [name [event-type payload]]
  (event/create [name (keyword event-type) (walk/keywordize-keys payload)]))

(defn send [{:keys [name endpoint]} [event-type query :as event]]
  (let [c (chan)
        auth-token ""]
    (POST endpoint
        {:headers {:Authorization (str "Bearer " auth-token)}
         :params {:event-type event-type
                  :query-type (sp/resolve query)
                  :query (if (sh/one? query)
                           query
                           (map clj->js query))}
         :format :json
         :handler #(go (>! c (handle-response name %)))})
    c))
