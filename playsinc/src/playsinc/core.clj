(ns playsinc.core
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]]))

(def echo-chan (chan))
(go (println (<! echo-chan)))
(>!! echo-chan "check check")

;; this won't work because there's no process listening for this channel.
;; (>!! (chan) "checking")

;; buffered channels
(def echo-buffer (chan 2))
(>!! echo-buffer "checking")
(>!! echo-buffer "checking again")

(def hi-chan (chan))
(doseq [n (range 100)]
  (go (>! hi-chan (str "hi " n))))

;; thread (blocking instead of parking)
(thread (println (<!! echo-chan)))
(>!! echo-chan "thread checking")

;; vending machine example project
(defn vending-machine
  []
  (let [in (chan)
        out (chan)]
    (go (<! in)
        (>! out "product"))
    [in out]))

(let [[in out] (vending-machine)]
  (>!! in "pocket lint")
  (<!! out))
