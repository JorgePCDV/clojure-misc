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
