(ns offcourse.specs.action
  (:require [cljs.spec :as spec]
            [shared.specs.action :as action :refer [action-spec]]
            [shared.specs.viewmodel :as viewmodel]
            [shared.specs.course :as course]
            [shared.specs.resource :as resource]
            [shared.specs.helpers :as helpers]
            [shared.specs.base :as base]
            [shared.specs.bookmark :as bookmark]
            [shared.specs.checkpoint :as checkpoint]
            [shared.specs.profile :as profile]
            [shared.specs.auth :as auth]
            [shared.specs.identity :as identity]))

(spec/def ::app-modes base/valid-modes)
(spec/def ::action-types action/types)

(defmethod action-spec :create [_]
  (spec/tuple ::action-types (spec/or :new-user #{:new-user}
                                      :profile ::profile/profile )))

(defmethod action-spec :update [_]
  (spec/tuple ::action-types (spec/or :viewmodel  ::viewmodel/viewmodel
                                      :revision   #{:revision}
                                      :course     ::course/course
                                      :checkpoint map?)))

(defmethod action-spec :save [_]
  (spec/tuple ::action-types (spec/or :profile ::profile/profile)))

(defmethod action-spec :authenticate [_]
  (spec/tuple ::action-types))

(defmethod action-spec :sign-in [_]
  (spec/tuple ::action-types))

(defmethod action-spec :sign-out [_]
  (spec/tuple ::action-types))

(defmethod action-spec :go [_]
  (spec/tuple ::action-types (spec/or :home #{:home})))

(defmethod action-spec :switch-to [_]
  (spec/tuple ::action-types (spec/or :app-mode ::app-modes)))


(defmethod action-spec :fork [_]
  (spec/tuple ::action-types (spec/or :course      ::course/course
                                      :identity    ::identity/identity)))

(defmethod action-spec :remove [_]
  (spec/tuple ::action-types (spec/or  :credentials ::auth/credentials
                                       :checkpoint ::checkpoint/checkpoint)))

(defmethod action-spec :add [_]
  (spec/tuple ::action-types (spec/or  :credentials ::auth/credentials
                                       :profile     ::profile/profile
                                       :identity    ::identity/identity
                                       :course      ::course/course
                                       :courses     (spec/* ::course/course)
                                       :new-checkpoint #{:new-checkpoint}
                                       :resource    ::resource/resource
                                       :resources   (spec/* ::resource/resource))))
