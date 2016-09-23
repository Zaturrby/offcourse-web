(ns offcourse.main
  (:require [com.stuartsierra.component :as component]
            [offcourse.adapters.aws.index :as aws]
            [offcourse.adapters.github.index :as github]
            [offcourse.core :as core]
            [shared.models.appstate.index :as model]
            [shared.specs.action :as action]))

(defonce app (atom nil))

(defonce appstate-data {:site-title "Offcourse_"
                        :actions action/action-types})

(defonce appstate (atom (model/create appstate-data)))

(def auth-adapter
  {:domain   "yeehaa.eu.auth0.com"
   :clientID "Z1J0CyMzZfIbOfBSVaMWJakoIrxm4Tfs"})

(def query-adapters
  [{:adapter    aws/create
    :name      "query"
    :resources #{:user :resources}
    :endpoint  "https://u3b8lgtti3.execute-api.us-east-1.amazonaws.com/staging/query"}
   #_{:adapter    github/create
    :name       "html-course"
    :repository {:name         "html-css-javascript"
                 :organization "offcourse"
                 :curator      "charlotte"
                 :sha          "4f98f9cf8d521dc3bb8c4489d91f6c9468f76c69"}
    :resources  #{:course :collection}
    :base-url   "https://api.github.com"}
   {:adapter    github/create
    :name       "bootstrap-data"
    :repository {:name         "clojurescript-course"
                 :organization "offcourse"
                 :curator      "charlotte"
                 :sha          "31510c353ec5d24a79b0b8d1a68d4d373d1f2d3f"}
    :resources  #{:course :collection}
    :base-url   "https://api.github.com"}])

(def adapters
  {:auth auth-adapter
   :command nil
   :query query-adapters})

(defn get-appstate [] (clj->js @appstate))

(defn init []
  (do
    (enable-console-print!)
    (reset! app (core/app appstate adapters auth-config))
    (reset! app (component/start @app))))
