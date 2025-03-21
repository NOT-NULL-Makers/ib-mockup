(ns client.core
  (:require ["react-dom/client" :as react-dom]
            [reagent.core :as r]
            [client.ui :refer [app-ui]]
            [client.ws :as ws]))

(defonce root (atom nil)) ;; Store React root instance

(defn mount-root []
  (let [container (.getElementById js/document "app")
        root-instance (.createRoot react-dom container)]
    (.render root-instance (r/as-element [app-ui]))
    (reset! root root-instance)))

(defn init []
  (ws/connect-websocket!) ;; Initialize WebSocket connection
  (mount-root))