(ns client.ws
  (:require [re-frame.core :as rf]))

;; -----------------------
;; WebSocket State
(defonce ws-channel (atom nil)) ;; Store WebSocket instance

;; -----------------------
;; WebSocket Connection
(defn connect-websocket! []
  (let [ws (js/WebSocket. "ws://localhost:3000/ws")]
    (reset! ws-channel ws)
    (set! (.-onopen ws)
          (fn []
            (println "WebSocket connection opened.")))
    (set! (.-onmessage ws)
          (fn [event]
            (let [msg (.-data event)]
              (println "Received WebSocket message:" msg)
              (rf/dispatch [:receive-message msg]))))
    (set! (.-onerror ws)
          (fn [error]
            (println "WebSocket error:" error)))
    (set! (.-onclose ws)
          (fn [_] (println "WebSocket connection closed.")))))

(defn send-ws-message [message]
  (when (and @ws-channel (= (.-readyState @ws-channel) js/WebSocket.OPEN))
    (.send @ws-channel message)))

;; -----------------------
;; Register WebSocket Effects for Re-frame
(rf/reg-fx
  :ws/send
  (fn [message] (send-ws-message message)))