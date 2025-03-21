(ns styles.core
  (:require [hawk.core :as hawk])
  (:import [java.nio.file Files Paths StandardCopyOption]))

(defn write-css []
  (require 'styles.styles :reload)
  (let [css    ((resolve 'styles.styles/generate-css))
        tmp    (Paths/get "resources/public/css/styles.css.tmp" (into-array String []))
        target (Paths/get "resources/public/css/styles.css" (into-array String []))]
    (spit (.toFile tmp) css)
    (Files/move tmp target (into-array StandardCopyOption [StandardCopyOption/REPLACE_EXISTING]))
    (println "CSS recompiled.")))

(defn -main []
  (write-css)
  (hawk/watch! [{:paths     ["src/styles"]
                 :recursive true
                 :handler   (fn [ctx evt]
                              (println "Change detected:" (:file evt))
                              (write-css)
                              ctx)}])
  @(promise))