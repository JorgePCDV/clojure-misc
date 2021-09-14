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
;; better version
(defn vending-machine-v2
  [product-count]
  (let [in (chan)
        out (chan)]
    (go (loop [hc product-count]
          (if (> hc 0)
            (let [input (<! in)]
              (if (= 3 input)
                (do (>! out "product")
                    (recur (dec hc)))
                (do (>! out "wrong product")
                    (recur hc))))
            (do (close! in)
                (close! out)))))
    [in out]))
(let [[in out] (vending-machine-v2 2)]
  (>!! in "pocket lint")
  (println (<!! out))

  (>!! in 3)
  (println (<!! out))

  (>!! in 3)
  (println (<!! out))

  (>!! in 3)
  (<!! out))

;; process pipeline
(let [c1 (chan)
      c2 (chan)
      c3 (chan)]
  (go (>! c2 (clojure.string/upper-case (<! c1))))
  (go (>! c3 (clojure.string/reverse (<! c2))))
  (go (println (<! c3)))
  (>!! c1 "hello"))

;; alts!!
(defn upload
  [screenshot c]
  (go (Thread/sleep (rand 100))
      (>! c screenshot)))

(let [c1 (chan)
      c2 (chan)
      c3 (chan)]
  (upload "screenshot1.jpg" c1)
  (upload "screenshot2.jpg" c2)
  (upload "screenshot3.jpg" c3)
    (let [[screenshot channel] (alts!! [c1 c2 c3])]
      (println "Sending screenshot notification for" screenshot)))

;; alts!! timeouts
(let [c4 (chan)]
  (upload "screenshot_timeout.jpg" c4)
  (let [[screenshot channel] (alts!! [c4 (timeout 5)])]
    (if screenshot
      (println "sending screenshot notification for" screenshot)
      (println "timed out!"))))
