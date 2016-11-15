(ns offcourse.main
  (:require [com.stuartsierra.component :as component]
            cljsjs.auth0-lock
            [offcourse.adapters.aws.index :as aws]
            [offcourse.adapters.github.index :as github]
            [offcourse.core :as core]
            [shared.models.appstate.index :as model]
            [shared.specs.core :as specs]))

(defonce app (atom nil))

(defonce appstate-data {:site-title "Offcourse_"
                        :app-mode :viewing
                        :actions specs/action-types})

(defonce appstate (atom (model/create appstate-data)))

(def auth-adapter
  (let [domain   "yeehaa.eu.auth0.com"
        client-id "Z1J0CyMzZfIbOfBSVaMWJakoIrxm4Tfs"]
    (js/Auth0Lock. client-id domain)))

(def command-adapter
  {:adapter    aws/create
   :name      "aws-command"
   :endpoint  "https://akd5yk8kih.execute-api.us-east-1.amazonaws.com/dev/command"})


(def query-adapters
  [{:adapter    aws/create
    :name      "aws-query"
    :resources #{:collection :course :resource}
    :endpoint  "https://akd5yk8kih.execute-api.us-east-1.amazonaws.com/dev/query"}])

(def adapters
  {:auth auth-adapter
   :command command-adapter
   :query query-adapters})

(defn get-appstate [] (clj->js @appstate))

(defn init []
  (do
    (aset js/window "appstate" get-appstate)
    (enable-console-print!)
    (reset! app (core/app appstate adapters))
    (reset! app (component/start @app))))

(defn reload []
  (do
    (enable-console-print!)
    (reset! app (core/app appstate adapters))
    (reset! app (component/start @app))))

(defn stop []
  (component/stop @app))
