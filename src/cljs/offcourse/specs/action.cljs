(ns offcourse.specs.action
  (:require [cljs.spec :as spec]
            [shared.specs.action :as action :refer [action-spec]]))

(def action (spec/multi-spec action-spec :action-type))

(defmethod action-spec :authenticate [_]
  (spec/tuple :offcourse/actions (spec/or :provider #{:github})))

(defmethod action-spec :sign-in [_]
  (spec/tuple :offcourse/actions))

(defmethod action-spec :sign-up [_]
  (spec/tuple :offcourse/actions (spec/or :user :offcourse/user)))

(defmethod action-spec :sign-out [_]
  (spec/tuple :offcourse/actions))

(defmethod action-spec :create [_]
  (spec/tuple :offcourse/actions (spec/or :new-user #{:new-user}
                                          :profile :offcourse/profile)))

(defmethod action-spec :update [_]
  (spec/tuple :offcourse/actions (spec/or :viewmodel  :offcourse/viewmodel
                                          :course     :offcourse/course
                                          :revision   #{:revision}
                                          :checkpoint map?)))

(defmethod action-spec :save [_]
  (spec/tuple :offcourse/actions (spec/or :profile :offcourse/profile)))

(defmethod action-spec :go [_]
  (spec/tuple :offcourse/actions (spec/or :home        #{:home})))

(defmethod action-spec :switch-to [_]
  (spec/tuple :offcourse/actions (spec/or :app-mode    :offcourse/app-modes)))

(defmethod action-spec :fork [_]
  (spec/tuple :offcourse/actions (spec/or :course      :offcourse/course
                                          :identity    :offcourse/identity)))

(defmethod action-spec :remove [_]
  (spec/tuple :offcourse/actions (spec/or  :credentials :offcourse/credentials
                                           :checkpoint  :offcourse/checkpoint)))

(defmethod action-spec :add [_]
  (spec/tuple :offcourse/actions (spec/or  :credentials    :offcourse/credentials
                                           :profile        :offcourse/profile
                                           :identity       :offcourse/identity
                                           :course         :offcourse/course
                                           :resource       :offcourse/resource
                                           :new-checkpoint #{:new-checkpoint}
                                           :courses        (spec/coll-of :offcourse/course)
                                           :resources      (spec/coll-of :offcourse/resource))))
