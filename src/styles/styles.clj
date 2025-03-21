(ns styles.styles
  (:require [garden.arithmetic :as ga]
            [garden.core :refer [css]]
            [garden.selectors :refer [&]]
            [garden.units :refer [em percent px vh]]))

(def ib-gray "#282828")
(def ib-border-gray "#646464")
(def ib-text-gray "#6a6a6a")
(def ib-placeholder "#8d4d09")
(def ib-orange "#fba228")
(def ib-yellow "#d9d51c")

(def top-panel-height (px 40))
(def left-panel-width (px 300))
(def bottom-panel-height (px 60))
(def border-width (px 2))
(def window-padding (px 8))

(def input-card
  [:.name-input-card {:background-color ib-gray
                      :padding          (px 40)
                      :text-align       :center
                      :border-radius    0}
   [:h1 {:font-size   (px 22)
         :font-weight 400
         :margin-top  0}]
   [:.name-input {:display         :flex
                  :flex-wrap       :nowrap
                  :gap             (px 8)
                  :justify-content :center}
    [:input {:background-color ib-orange
             :color            :black
             :font-size        (px 22)
             :padding          (px 8)
             :display          :block
             :border           :none
             :border-radius    0
             :outline          :none}
     [(& "::placeholder") {:color ib-placeholder}]]
    [:button {:padding       "8px 16px"
              :border        :none
              :border-radius 0
              :background    :white
              :color         :black
              :font-size     (px 22)
              :cursor        :pointer}]]])

;; -----------------------
;; Define Styles Using Garden Syntax
(def styles
  [[:html :body :#app {:width            (percent 100)
                       :height           (percent 100)
                       :margin           0
                       :display          :flex
                       :justify-content  :center
                       :align-items      :center
                       :background-color :black
                       :color            :white
                       :font-family      "'Neue Haas Grotesk', sans-serif"
                       :font-size        (px 22)}]
   input-card
   [:.chat-window {:position :relative
                   :top      0
                   :left     0
                   :width    [["calc(" (percent 100) " - " (ga/* (ga/+ border-width window-padding) 2) ")"]]
                   :height   [["calc(" (percent 100) " - " (ga/+ border-width (ga/* window-padding 2)) ")"]]}
    [:.top-panel {:position        :absolute
                  :display         :flex
                  :justify-content :flex-start
                  :align-items     :flex-end
                  :top             0
                  :left            0
                  :width           (percent 100)
                  :height          [["calc(" top-panel-height " - " border-width ")"]]
                  :border-bottom   [[border-width "solid" ib-border-gray]]}
     [:.label {:bottom           0
               :background-color ib-border-gray
               :margin-right     :auto
               :padding          [[(px 8) (px 16) (ga/- (px 8) border-width) (px 16)]]}
      [:span {:font-size (px 22)}]]]
    [:.left-panel {:position      :absolute
                   :top           top-panel-height
                   :left          0
                   :width         [["calc(" left-panel-width " - " (ga/* border-width 2) ")"]]
                   :height        [["calc(" (percent 100) " - " (ga/+ top-panel-height border-width) ")"]]
                   :border-right  [[border-width "solid" ib-border-gray]]
                   :border-left   [[border-width "solid" ib-border-gray]]
                   :border-bottom [[border-width "solid" ib-border-gray]]}]
    [:.bottom-panel {:position         :absolute
                     :bottom           0
                     :left             left-panel-width
                     :width            [["calc(" (percent 100) " - " (ga/+ left-panel-width border-width) ")"]]
                     :border-right     [[border-width "solid" ib-border-gray]]
                     :border-bottom    [[border-width "solid" ib-border-gray]]
                     :height           [["calc(" bottom-panel-height " - " border-width ")"]]
                     :background-color ib-gray}
     [:.chat-input {:position  :relative
                    :width     [["calc(" (percent 100) " - " (px 6) ")"]]
                    :height    [["calc(" (percent 100) " - " (px 6) ")"]]
                    :display   :flex
                    :flex-wrap :nowrap
                    :padding   (px 4)
                    :gap       (px 4)
                    :border    :none}
      [:input {:background-color ib-orange
               :color            :black
               :font-size        (px 22)
               :padding          (px 8)
               :width            (percent 100)
               :display          :block
               :border           :none
               :border-radius    0
               :outline          :none}
       [(& "::placeholder") {:color ib-placeholder}]]
      [:button {:padding       "8px 16px"
                :border        :none
                :border-radius 0
                :background    :white
                :color         :black
                :font-size     (px 22)
                :cursor        :pointer}]]]
    [:.chat-wrapper {:position      :absolute
                     :top           top-panel-height
                     :left          left-panel-width
                     :width         [["calc(" (percent 100) " - " (ga/+ left-panel-width border-width) ")"]]
                     :height        [["calc(" (percent 100) " - " (ga/+ top-panel-height bottom-panel-height
                                                                        border-width) ")"]]
                     :border-right  [[border-width "solid" ib-border-gray]]
                     :border-bottom [[border-width "solid" ib-border-gray]]}
     [:.chat {:position  :relative
              :padding   (px 8)
              :font-size (px 22)}
      [:.message {}
       [:.name {:text-transform :uppercase
                :color          ib-yellow
                :margin         [[0 0 (px 2) 0]]}]
       [:.time {:color        ib-text-gray
                :margin-right (px 8)
                :font-weight  200}]]]]]])

;; -----------------------
;; Function to Generate CSS
(defn generate-css []
  (css styles))                                             ;; Generate CSS from Garden style list
