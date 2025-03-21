(ns client.ui
  (:require [cljs.reader :as reader]
            [re-frame.core :as rf]))

;; -----------------------
;; Event Handlers

(rf/reg-event-db
  :set-tmp-name
  (fn [db [_ new-name]]
    (assoc db :tmp-name new-name)))

(rf/reg-event-db
  :set-name
  (fn [db [_ new-name]]
    (assoc db :name new-name)))

(rf/reg-event-db
  :set-message
  (fn [db [_ new-message]]
    (assoc db :message new-message)))

(rf/reg-event-fx
  :send-message
  (fn [{:keys [db]} _]
    (let [{:keys [name message]} db]
      (when (and name message)
        (let [msg {:name    name
                   :message message}]
          {:ws/send msg
           :db      (assoc db :message nil)})))))

;; -----------------------
;; Handle Incoming Messages (WebSocket)

(rf/reg-event-db
  :receive-message
  (fn [db [_ raw-msg]]
    (let [msg (reader/read-string raw-msg)]
      (update db :messages conj msg))))

;; -----------------------
;; Subscriptions

(rf/reg-sub
  :tmp-name
  (fn [db _]
    (:tmp-name db "")))

(rf/reg-sub
  :name
  (fn [db _] (:name db)))

(rf/reg-sub
  :message
  (fn [db _] (:message db)))

(rf/reg-sub
  :messages
  (fn [db _] (reverse (:messages db []))))

;; -----------------------
;; UI Components

(defn name-input []
  (let [tmp-name (rf/subscribe [:tmp-name])]
    (let [name @tmp-name]
      [:div.name-input-card
       [:h1 "Welcome to Instant Bloomberg"]
       [:div.name-input
        [:input {:type        "text"
                 :value       name
                 :placeholder "Enter your name"
                 :on-change   #(rf/dispatch [:set-tmp-name (-> % .-target .-value)])}]
        [:button {:on-click #(rf/dispatch [:set-name name])} "Chat"]]])))

(defn chat-messages []
  (let [messages @(rf/subscribe [:messages])]
    [:div.chat
     (for [[idx {:keys [name message received-at]}] (map-indexed vector messages)]
       (do
         ^{:key idx}
         [:div.message
          [:p.name name]
          [:span.time received-at]
          [:span.message message]]))]))

(defn chat-input []
  (let [message @(rf/subscribe [:message])]
    [:div.chat-input
     [:input {:type        "text"
              :value       message
              :placeholder "Type a message..."
              :on-change   #(rf/dispatch [:set-message (-> % .-target .-value)])}]
     [:button {:on-click #(rf/dispatch [:send-message])} "Send"]]))

(defn app-ui []
  (let [name @(rf/subscribe [:name])]
    (if (not (empty? name))
      [:div.chat-window
       [:div.top-panel
        [:div.label
         [:span "IB Manager"]]]
       [:div.left-panel]
       [:div.chat-wrapper
        [chat-messages]]
       [:div.bottom-panel
        [chat-input]]]
      [name-input])))
