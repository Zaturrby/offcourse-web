(ns offcourse.system.plumbing
  (:require [cljs.core.async :refer [chan merge mult tap]]))

(def channels
  (let [router-output    (chan)
        conductor-output (chan)
        query-output     (chan)
        command-output   (chan)
        ui-output        (chan)
        auth-output      (chan)
        ui-mult          (mult ui-output)
        query-mult       (mult query-output)
        command-mult     (mult command-output)
        auth-mult        (mult auth-output)
        conductor-mult   (mult conductor-output)
        conductor-input  (merge [router-output
                                 (tap auth-mult (chan))
                                 (tap command-mult (chan))
                                 (tap ui-mult (chan))
                                 (tap query-mult (chan))])
        query-input      (tap conductor-mult (chan))
        command-input    (tap conductor-mult (chan))
        router-input     (tap conductor-mult (chan))
        auth-input       (tap conductor-mult (chan))
        ui-input         (tap conductor-mult (chan))]
    {:query     {:input  query-input
                 :output query-output}
     :command   {:input command-input
                 :output command-output}
     :router    {:output router-output
                 :input  router-input}
     :auth      {:input  auth-input
                 :output auth-output}
     :conductor {:input  conductor-input
                 :output conductor-output}
     :ui        {:input  ui-input
                 :output ui-output}}))
