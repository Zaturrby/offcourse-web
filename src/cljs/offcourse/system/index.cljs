(ns offcourse.system.index
  (:require [com.stuartsierra.component :as component]
            [offcourse.query.index :as query]
            [offcourse.command.index :as command]
            [offcourse.conductor.index :as conductor]
            [offcourse.auth.index :as auth]
            [offcourse.router.index :as router]
            [offcourse.protocol-extensions.decoratable]
            [offcourse.protocol-extensions.loggable]
            [offcourse.protocol-extensions.convertible]
            [offcourse.system.plumbing :as plumbing]
            [offcourse.system.routes :as routes]
            [offcourse.system.views :refer [views]]
            [offcourse.ui.index :as ui]))

(defn connect-to-repository [{:keys [adapter] :as config}]
  (component/start (adapter config)))

(defn system [appstate adapters]
  (let [channels plumbing/channels]
    (component/system-map
     :repositories               (map connect-to-repository (:query adapters))
     :adapter                    (connect-to-repository (:command adapters))
     :command-channels           (:command channels)
     :command-triggers           [:requested]
     :command-responses          [:signed-in]
     :command                    (component/using (command/create :command)
                                                {:channels     :command-channels
                                                 :triggers     :command-triggers
                                                 :responses    :command-responses
                                                 :adapter      :adapter})
     :query-channels           (:query channels)
     :query-triggers           [:not-found]
     :query-responses          [:found :not-found :failed]
     :query                    (component/using (query/create :query)
                                              {:channels     :query-channels
                                               :triggers     :query-triggers
                                               :responses    :query-responses
                                               :repositories :repositories})
     :auth-channels           (:auth channels)
     :auth-triggers           [:requested]
     :auth-responses          [:granted :revoked]
     :auth-provider           (:auth adapters)
     :auth                    (component/using (auth/create :auth)
                                               {:channels  :auth-channels
                                                :triggers  :auth-triggers
                                                :responses :auth-responses
                                                :provider  :auth-provider})

     :routes                 routes/table
     :router-triggers        [:refreshed :requested]
     :router-responses       [:requested]
     :router-channels        (:router channels)
     :router                 (component/using (router/create :router)
                                              {:channels           :router-channels
                                               :triggers           :router-triggers
                                               :responses          :router-responses
                                               :routes             :routes})
     :appstate-atom          appstate
     :conductor-triggers      [:granted  :revoked :refreshed :requested :found :not-found :signed-in]
     :conductor-responses     [:refreshed :updated :requested :not-found]
     :conductor-channels      (:conductor channels)
     :conductor               (component/using (conductor/create :conductor)
                                              {:channels  :conductor-channels
                                               :triggers  :conductor-triggers
                                               :responses :conductor-responses
                                               :state     :appstate-atom})
     :views                  views
     :ui-triggers            [:refreshed]
     :ui-responses           [:rendered :requested]
     :ui-channels            (:ui channels)
     :ui                     (component/using (ui/create :ui)
                                              {:channels    :ui-channels
                                               :triggers    :ui-triggers
                                               :responses   :ui-responses
                                               :routes      :routes
                                               :views       :views}))))
