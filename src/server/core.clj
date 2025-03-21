(ns server.core
  (:require [org.httpkit.server :as http]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response response]]
            [server.ws :refer [ws-handler]]                 ;; Import WebSocket handler
            [nrepl.server :refer [start-server stop-server]])
  (:gen-class))

;; -----------------------
;; nREPL Setup
(defonce nrepl-server (atom nil))

(defn start-repl! []
  (reset! nrepl-server (start-server :port 7888))           ;; Start nREPL on port 7888
  (println "nREPL running on port 7888"))

;; -----------------------
;; Routes
(defroutes app-routes
  (GET "/" [] (resource-response "index.html" {:root "public"})) ;; Serve index.html
  (GET "/ws" [] ws-handler)                                 ;; WebSocket endpoint
  (route/resources "/")                                     ;; Serve static files
  (route/not-found "Not Found"))

;; -----------------------
;; Start Server
(defn start! []
  (start-repl!)                                             ;; Ensure nREPL starts first
  (http/run-server app-routes {:port 3000})
  (println "Server running on http://localhost:3000"))

(defn -main [& args]
  (start!))
