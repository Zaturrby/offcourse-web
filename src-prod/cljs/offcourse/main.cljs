(ns offcourse.main
  (:require [com.stuartsierra.component :as component]
            [offcourse.adapters.aws.index :as aws]
            [offcourse.adapters.github.index :as github]
            [offcourse.core :as core]
            [shared.models.appstate.index :as model]))

(set! cljs.core/*print-fn* identity)

(defonce appstate (atom (model/new {:site-title "Offcourse_"})))

(defonce app (atom nil))

(defonce auth-config {:domain "yeehaa.eu.auth0.com"
                      :clientID "Z1J0CyMzZfIbOfBSVaMWJakoIrxm4Tfs"})

(def adapters
  [{:adapter           github/new-db
    :name              "bootstrap"
    :repository        {:name "clojurescript-course"
                        :organization      "offcourse"
                        :curator           "charlotte"
                        :sha               "31510c353ec5d24a79b0b8d1a68d4d373d1f2d3f"}
    :resources         #{:course :collection}
    :endpoint          "https://api.github.com"}])

(defn init []
  (do
    (enable-console-print!)
    (reset! app (core/app appstate adapters auth-config))
    (reset! app (component/start @app))))
