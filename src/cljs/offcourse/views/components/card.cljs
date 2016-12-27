(ns offcourse.views.components.card
  (:require [offcourse.views.components.button :refer [button]]
            [offcourse.views.components.card-meta :refer [card-meta]]
            [offcourse.views.components.item-list :refer [item-list]]
            [offcourse.views.components.card-social :refer [card-social]]
            [cljsjs.react-grid-layout]
            [rum.core :as rum]))

(rum/defc card [{:keys [course-id goal course-slug checkpoints curator] :as course}
                respond]
 (let [{:keys [affordances course-url]} (meta course)
       {:keys [browsable? forkable? trackable? editable?]} affordances]
  [:.container
   [:.card {:on-click #()}
    [:.card--section
     [:a.card--title {:href (-> course meta :course-url)} goal]]
    [:.card--wrapper
     [:.card--section (card-meta course)]
     [:.card--section (item-list :todo checkpoints trackable? respond)]
     (when (or forkable? editable?)
      [:.card--section
       [:ul.card--row
        (when forkable? (button {:button-text "Fork"} #(respond [:fork course])))
        (when editable? (button {:button-text "Edit"} #(respond [:switch-to :edit-mode])))]])
     (when false
      [:.card--section (card-social)])]]]))

(def Grid (js/React.createFactory js/ReactGridLayout))

(defn div [props subcomp] (js/React.createElement "div" (clj->js props) subcomp))

(rum/defc cards [{:keys [courses]} respond]
  (Grid #js {:className="cards"
             :cols 12
             :rowHeight 30
             :width 1200}
        [(div {:key 1
               :data-grid {:i "a" :x 0 :y 0 :w 1 :h 2 :static true}}
              (map #(rum/with-key (card % respond) (:course-id %)) courses))]))

