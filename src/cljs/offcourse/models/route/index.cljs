(ns offcourse.models.route.index
  (:require [shared.protocols.loggable :as log]
            [shared.protocols.convertible :refer [Convertible]]
            [offcourse.models.route.to-url :refer [to-url]]
            [offcourse.specs.route :as specs]
            [bidi.bidi :as bidi]))

(defn- override [params]
  (specify params
    Convertible
    (-to-url [this routes] (to-url this routes))))

(defn create [raw-route]
  (-> raw-route
      (with-meta {:spec ::specs/route})
      override))

(defmulti from-params (fn [handler params] (if params :params :handler)))

(defmethod from-params :handler [handler _]
  (create {handler nil}))

(defmethod from-params :params [_ params]
  (create params))
