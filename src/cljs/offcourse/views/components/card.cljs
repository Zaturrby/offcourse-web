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
  [:.container {:key 1
                :data-grid {:i course-id :x 0 :y 0 :w 1 :h 2 :static true}}
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

(defn grid [props subcomp]
           (js/console.log props subcomp)
           (js/React.createElement
             js/ReactGridLayout
             (clj->js props)
             subcomp))

(defn wrapper [props subcomp] (js/React.createElement "div" (clj->js props) subcomp))

(rum/defc cards [{:keys [courses]} respond]
  (grid #js {:className "cards"
             :cols 12
             :rowHeight 30
             :width 1200}
    ; [:div {:key "02"} "Test"] ; Old failing case
    ; [(wrapper {:key 1
    ;            :data-grid {:i "a" :x 0 :y 0 :w 1 :h 2 :static true}}
    ;           (map #(rum/with-key (card % respond) (:course-id %)) courses))] ; Undesirable due to container
    (map #(rum/with-key (wrapper #js {:key 1
                                      :data-grid {:i "a" :x 0 :y 0 :w 1 :h 2 :static true}}
                                 (card % respond))
                        (:course-id %))
         courses))) ; Not working, map returns 'something' that grid doesn't understand
