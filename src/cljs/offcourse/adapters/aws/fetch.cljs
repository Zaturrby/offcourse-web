(ns offcourse.adapters.aws.fetch
  (:require [ajax.core :refer [POST]]
            [cljs.core.async :refer [chan]]
            [clojure.walk :as walk]
            [shared.models.event.index :as event]
            [shared.protocols.specced :as sp]
            [shared.specs.helpers :as sh]
            [shared.protocols.loggable :as log])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn handle-response [name [event-type payload]]
  (if (= event-type "found")
    (walk/keywordize-keys payload)
    false))

(defn fetch [{:keys [name endpoint]} query]
  (let [c (chan)
        auth-token ""]
    (POST endpoint
        {:headers {:Authorization (str "Bearer " auth-token)}
         :params {:event-type :not-found
                  :query-type (sp/resolve query)
                  :query (if (sh/one? query)
                           query
                           (map clj->js query))}
         :format :json
         :handler #(go (>! c (handle-response name %)))})
    c))
