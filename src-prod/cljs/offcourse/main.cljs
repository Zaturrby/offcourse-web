(ns offcourse.main
  (:require [com.stuartsierra.component :as component]
            [offcourse.adapters.aws.index :as aws]
            [offcourse.adapters.github.index :as github]
            [offcourse.core :as core]
            [shared.models.appstate.index :as model]
            [shared.specs.core :as specs]))

(defonce app (atom nil))

(defonce appstate-data {:site-title "Offcourse_"
                        :app-mode :view-mode
                        :actions specs/action-types})

(defonce appstate (atom (model/create appstate-data)))

(def auth-adapter
  {:domain   "yeehaa.eu.auth0.com"
   :clientID "Z1J0CyMzZfIbOfBSVaMWJakoIrxm4Tfs"})

(def command-adapter
 {:adapter    aws/create
  :name      "aws-command"
  :endpoint  "https://c5ut0y5m28.execute-api.us-east-1.amazonaws.com/dev/command"})

(def query-adapters
  [{:adapter    aws/create
    :name      "query"
    :resources #{:resources :resource}
    :endpoint  "https://2lia48xse2.execute-api.us-east-1.amazonaws.com/production/query"}
   {:adapter    github/create
    :name       "html-course"
    :repository {:name         "html-css-javascript"
                 :organization "offcourse"
                 :curator      "charlotte"
                 :sha          "e438278961f4f83b587b6fc8413f714ae34682a9"}
    :resources  #{:course :collection}
    :base-url   "https://api.github.com"}])

(def adapters
  {:auth auth-adapter
   :command command-adapter
   :query query-adapters})

(defn get-appstate [] (clj->js @appstate))

(defn init []
  (do
    (enable-console-print!)
    (reset! app (core/app appstate adapters auth-config))
    (reset! app (component/start @app))))
