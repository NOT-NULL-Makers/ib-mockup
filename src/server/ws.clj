(ns server.ws
  (:require [org.httpkit.server :as http]
            [clojure.edn :as edn]))

;; -----------------------
;; WebSocket State
(defonce clients (atom #{}))  ;; Store connected clients
(defonce messages (atom []))  ;; Store chat history (temporary, no DB)

;; -----------------------
;; Get Current Timestamp
(defn current-timestamp []
  (let [now (java.time.LocalTime/now)
        fmt (java.time.format.DateTimeFormatter/ofPattern "HH:mm:ss")]
    (.format now fmt)))

;; -----------------------
;; Broadcast to All Clients
(defn broadcast! [message]
  (swap! messages conj message)  ;; Store message in history
  (doseq [client @clients]
    (http/send! client (pr-str message))))  ;; Send EDN string

;; -----------------------
;; WebSocket Handler
(defn ws-handler [req]
  (http/as-channel req
    {:on-open  (fn [channel]
                 (swap! clients conj channel)
                 (println "Client connected. Total clients:" (count @clients)))

     :on-receive (fn [channel raw-message]
                   (println "Received raw EDN message:" raw-message)
                   (try
                     (let [parsed-msg         (edn/read-string raw-message)
                           msg-with-timestamp (assoc parsed-msg :received-at (current-timestamp))]
                       (println "Parsed message with timestamp:" msg-with-timestamp)
                       (broadcast! msg-with-timestamp))
                     (catch Exception e
                       (println "Error parsing EDN message:" (.getMessage e)))))

     :on-close (fn [channel status]
                 (swap! clients disj channel)
                 (println "Client disconnected. Remaining clients:" (count @clients)))}))