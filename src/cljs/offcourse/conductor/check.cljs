(ns offcourse.conductor.check
  (:require [shared.protocols.specced :as sp]
            [shared.protocols.loggable :as log]))

(defn viewmodel-type [{:keys [viewmodel] :as state}]
  (when viewmodel (sp/resolve viewmodel)))

(defn check [{:keys [state]} proposal]
  (let [old-type (viewmodel-type @state)
        new-type(viewmodel-type proposal)
        user-name (some-> proposal :user :user-name)
        auth-token (some-> proposal :user :auth-token)]
    (cond
      (and (= old-type :signup-view) (= new-type :signup-view)) true
      (and (= old-type :signup-view) (and auth-token (not user-name))) false
      :default true)))
