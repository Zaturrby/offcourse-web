(ns offcourse.views.components.card
  (:require [offcourse.views.components.item-list :refer [item-list]]
            [rum.core :as rum]
            [shared.protocols.loggable :as log]
            [cljs.test :as test]
            [cljs.spec :as spec]))

(spec/def ::button-type (spec/or :link string?
                                 :action any?))

(defmulti button (fn [button-text payload] (first (spec/conform ::button-type payload))))

(defmethod button :link [button-text url]
  [:li.button {:data-button-type "textbar"}
   [:a {:href url} button-text]])

(defmethod button :action [button-text action]
  [:li.button {:data-button-type "textbar"}
   [:a {:on-click action} button-text]])

(rum/defc card [{:keys [course-id goal course-slug checkpoints curator] :as course}
                respond]
  (let [{:keys [affordances course-url]} (meta course)
        {:keys [browsable? forkable? trackable? editable?]} affordances]
   [:.container
    [:.card
     [:.card--section
      [:a.card--title {:href (-> course meta :course-url)} goal]]
     [:.card--section
       [:.card--profile
        [:.card--profile-section
          (log/log course)
          [:img.card--profile-image {:src (str "/images/profilepics/" (:curator course) ".jpg")}]]
        [:.card--profile-section
         [:.card--profile-label "Curated by"]
         [:.card--profile-username (clojure.string/capitalize (:curator course))]]]]
     [:.card--section (item-list :todo checkpoints trackable? respond)]
     [:.card--section
      [:ul.card--actions
       (when browsable? (button "Browse" course-url))
       (when forkable? (button "Fork" #(respond [:fork course])))]]
     [:.card--section 
      [:.card--social
       [:.card--social-icons
        [:img.card--social-img {:src "/images/social/t.png"}]
        [:img.card--social-img {:src "/images/social/f.png"}]
        [:img.card--social-img {:src "/images/social/gplus.png"}]
        [:img.card--social-img {:src "/images/social/gh.png"}]]
       [:.card--social-url "Get URL"]]]]]))

(rum/defc cards [{:keys [courses]} respond]
  [:.cards (map #(rum/with-key (card % respond) (:course-id %)) courses)])
