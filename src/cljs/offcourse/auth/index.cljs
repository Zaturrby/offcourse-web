(ns offcourse.auth.index
  (:require cljsjs.auth0-lock
            [com.stuartsierra.component :refer [Lifecycle]]
            [offcourse.auth.authenticate :as ac]
            [offcourse.auth.get :as get]
            [shared.protocols.eventful :as ef :refer [Eventful]]
            [shared.models.credentials.index :as credentials]))

(defn init [{:keys [config] :as auth}]
  (assoc auth :provider (js/Auth0Lock. (:clientID config) (:domain config))))


(defrecord Auth []
  Lifecycle
  (start [auth]
    (let [auth-token (get/get-local-token auth {:auth-token nil})]
      (when auth-token
        (ef/respond auth [:granted (credentials/create auth-token)]))
      (-> auth
          init
          ef/listen)))
  (stop [auth] (ef/mute auth))
  Eventful
  (-respond [auth event] (ef/respond auth event))
  (-react [auth event] (ac/react auth event))
  (-mute [auth] (ef/mute auth))
  (-listen [auth] (ef/listen auth)))


(defn create [name] (-> {:component-name name}
                        map->Auth))
