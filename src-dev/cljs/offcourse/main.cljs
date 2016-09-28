(ns offcourse.main
  (:require [com.stuartsierra.component :as component]
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
  {:domain   "yeehaa.eu.auth0.com"
   :clientID "Z1J0CyMzZfIbOfBSVaMWJakoIrxm4Tfs"})

(def query-adapters
  [{:adapter    aws/create
    :name      "query"
    :resources #{:user :resources :resource}
    :endpoint  "https://ohtxqsb2a4.execute-api.us-east-1.amazonaws.com/dev/query"}
   {:adapter    github/create
    :name       "html-course"
    :repository {:name         "html-css-javascript"
                 :organization "offcourse"
                 :curator      "charlotte"
                 :sha          "3a8000ac257d6d2d9fad3e889c44c6db232a167d"}
    :resources  #{:course :collection}
    :base-url   "https://api.github.com"}
   #_{:adapter    github/create
    :name       "clojurescript-course"
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
