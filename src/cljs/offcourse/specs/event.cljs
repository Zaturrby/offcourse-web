(ns offcourse.specs.event
  (:require [cljs.spec :as spec]
            [shared.specs.event :refer [event-spec]]))

(spec/def ::event-type keyword?)
(spec/def event (spec/multi-spec event-spec ::event-type))

(spec/def :event-payload/refreshed   (spec/or :appstate    :offcourse/appstate
                                              :route       :offcourse/route))
(spec/def :event-payload/found       (spec/or :course      :offcourse/course
                                              :courses     (spec/coll-of :offcourse/course)
                                              :resource    :offcourse/resource))
(spec/def :event-payload/auth        (spec/or :credentials :offcourse/credentials))
(spec/def :event-payload/signed-in   (spec/or :identity    :offcourse/identity))

(defmethod event-spec :requested [_] (spec/tuple ::event-type :offcourse/action))
(defmethod event-spec :not-found [_] (spec/tuple ::event-type :offcourse/query))
(defmethod event-spec :refreshed [_] (spec/tuple ::event-type :event-payload/refreshed))
(defmethod event-spec :found     [_] (spec/tuple ::event-type :event-payload/found))
(defmethod event-spec :granted   [_] (spec/tuple ::event-type :event-payload/auth))
(defmethod event-spec :revoked   [_] (spec/tuple ::event-type :event-payload/auth))
(defmethod event-spec :signed-in [_] (spec/tuple ::event-type :event-payload/signed-in))
(defmethod event-spec :rendered  [_] (spec/tuple ::event-type nil?))
(defmethod event-spec :failed    [_] (spec/tuple ::event-type any?))
