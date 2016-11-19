(ns offcourse.specs.event
  (:require [cljs.spec :as spec]
            [shared.specs.event :refer [event-spec]]
            [shared.specs.query :as query]
            [shared.specs.action :as action]
            [offcourse.specs.appstate :as appstate]
            [offcourse.specs.route :as route]
            [shared.specs.course :as course]
            [shared.specs.resource :as resource]
            [shared.specs.identity :as identity]
            [shared.specs.auth :as auth]))

(spec/def ::event-type keyword?)
(spec/def ::action    ::action/action)
(spec/def ::query      ::query/query)

(spec/def ::refreshed-payload (spec/or :appstate    ::appstate/appstate
                                       :route       ::route/route))
(spec/def ::found-payload     (spec/or :course      ::course/course
                                       :courses     (spec/coll-of ::course/course)
                                       :resource    ::resource/resource))
(spec/def ::auth-payload      (spec/or :credentials ::auth/credentials))
(spec/def ::signed-in-payload (spec/or :identity    ::identity/identity))

(defmethod event-spec :requested [_] (spec/tuple ::event-type ::action))
(defmethod event-spec :not-found [_] (spec/tuple ::event-type ::query))
(defmethod event-spec :refreshed [_] (spec/tuple ::event-type ::refreshed-payload))
(defmethod event-spec :found     [_] (spec/tuple ::event-type ::found-payload))
(defmethod event-spec :granted   [_] (spec/tuple ::event-type ::auth-payload))
(defmethod event-spec :revoked   [_] (spec/tuple ::event-type ::auth-payload))
(defmethod event-spec :signed-in [_] (spec/tuple ::event-type ::signed-in-payload))
(defmethod event-spec :rendered  [_] (spec/tuple ::event-type nil?))
(defmethod event-spec :failed    [_] (spec/tuple ::event-type any?))

(spec/def ::event (spec/multi-spec event-spec ::event-type))
