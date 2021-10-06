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

;; alts!! with put operations
(let [c5 (chan)
      c6 (chan)]
  (go (<! c6))
    (let [[value channel] (alts!! [c5 [c6 "put operation!"]])]
      (println value)
      (= channel c6)))

;; queues
(defn append-to-file
  "Write a string to the end of a file"
  [filename s]
  (spit filename s :append true))

(defn format-quote
  "Delineate the beginning and end of a quote"
  [quote]
  (str "=== BEGIN QUOTE ===\n" quote "=== END QUOTE ===\n\n"))

(defn random-quote
  "Retrieve a random quote and format it"
  []
  (format-quote (slurp "http://www.braveclojure.com/random-quote")))

(defn snag-quotes
  [filename num-quotes]
  (let [c (chan)]
    (go (while true (append-to-file filename (<! c))))
    (dotimes [n num-quotes] (go (>! c (random-quote))))))

;; process pipelines
(defn upper-caser
  [in]
  (let [out (chan)]
    (go (while true (>! out (clojure.string/upper-case (<! in)))))
    out))

(defn reverser
  [in]
  (let [out (chan)]
    (go (while true (>! out (clojure.string/reverse (<! in)))))
    out))

(defn printer
  [in]
  (go (while true (println (<! in)))))

(def in-chan (chan))
(def upper-caser-out (upper-caser in-chan))
(def reverser-out (reverser upper-caser-out))
(printer reverser-out)
(>!! in-chan "hello")
