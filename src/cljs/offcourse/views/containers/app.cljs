(ns offcourse.views.containers.app
  (:require[rum.core :as rum]))

(rum/defc app [{:keys [main menubar notifybar dashboard overlay]}]
  [:.layout
    [:.layout--header
      [:.header
        [:.header--section menubar]
        (when notifybar [:.header--section notifybar])]]
    [:.layout--main
      [:.main
        (when dashboard [:.main--section dashboard])
        [:.main--section main]
        (when overlay [:.main--overlay overlay])]]])
