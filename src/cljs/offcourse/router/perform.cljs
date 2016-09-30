(ns offcourse.router.perform
  (:require [shared.protocols.loggable :as log]
            [shared.protocols.eventful :as ef]
            [shared.models.action.index :as action]
            [shared.models.viewmodel.index :as viewmodel]
            [shared.protocols.specced :as sp]
            [shared.models.route.index :as route]))

(defmulti perform (fn [rt action] (sp/resolve action)))

(defmethod perform [:sign-in nil] [rt action] nil)

(defmethod perform [:sign-out nil] [rt action]
  (ef/respond rt [:refreshed (route/create :home-view)]))

(defmethod perform [:save :profile] [rt action]
  (ef/respond rt [:refreshed (route/create :home-view)]))

(defmethod perform [:create :new-user] [rt action]
  (ef/respond rt [:refreshed (route/create :user)]))

(defmethod perform [:go :home] [rt action]
  (ef/respond rt [:refreshed (route/create :home-view)]))
