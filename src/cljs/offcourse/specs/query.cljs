(ns offcourse.specs.query
  (:require [cljs.spec :as spec]))

(spec/def :query/frontend (spec/or :collection          :query/collection
                                   :tags                :query/tags
                                   :resource            :query/resource
                                   :viewmodel           :query/viewmodel
                                   :checkpoint          :query/checkpoint
                                   :course              :query/course))
