(ns twiki.i18n
  (:require [taoensso.tower :as tower]
            [clojure.edn]))

(defn- resources [n]
  (->> n
       (.getResources (.getContextClassLoader (Thread/currentThread)))
       enumeration-seq))

(defn translate [& args]
  (let [t (tower/make-t
           {:dictionary (->> "i18n.edn"
                             resources
                             (map (comp clojure.edn/read-string slurp))
                             (apply merge-with merge))})]
    (apply t (or tower/*locale*
                 (java.util.Locale/getDefault))
           args)))

(def t translate)
