(ns offcourse.router.perform
  (:require [shared.protocols.loggable :as log]
            [shared.protocols.eventful :as ef]
            [shared.models.action.index :as action]
            [shared.models.viewmodel.index :as viewmodel]
            [shared.protocols.specced :as sp]))

(def go-home (action/create [:update (viewmodel/create :home-view {})]))
(def go-to-signup (action/create [:update (viewmodel/create :signup-view {})]))

(defmulti perform (fn [rt action] (sp/resolve action)))

(defmethod perform [:sign-in nil] [rt action] nil)

(defmethod perform [:sign-out nil] [rt action]
  (ef/respond rt [:requested go-home]))

(defmethod perform [:save :profile] [rt action]
  (ef/respond rt [:requested go-home]))

(defmethod perform [:create :new-user] [rt action]
  (ef/respond rt [:requested go-to-signup]))

(defmethod perform [:go :home] [rt action]
  (ef/respond rt [:requested go-home]))
