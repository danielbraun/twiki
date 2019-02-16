(ns twiki
  (:require [bidi.bidi :as bidi]))

(def ^:dynamic *routes*)

(defn path-for
  ([handler]
   (path-for handler {}))
  ([handler params]
   (bidi/unmatch-pair *routes* {:handler handler :params params})))

(defmulti handle-route :handler)

(defn wrap-routing [handler routes]
  (fn [request]
    (binding [*routes* routes]
      (let [match (bidi.bidi/match-route* routes (:uri request) request)]
        (handler
         (or (-> match
                 (update :params merge (:route-params match)))
             request))))))
