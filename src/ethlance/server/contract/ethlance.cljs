(ns ethlance.server.contract.ethlance
  (:require [ethlance.server.contract :refer [call]]
            [cljs-web3-next.eth :as web3-eth]
            [district.server.smart-contracts :as smart-contracts]))

(defn initialize [job-proxy-address]
  (smart-contracts/contract-send :ethlance :initialize [job-proxy-address] {:gas 6000000}))

(defn create-job [creator offered-values job-type arbiters ipfs-data]
  ; When there's only one contract the 1st param to contract-send can be just name of the contract (symbol)
  ; For contracts that have multiple copies on different addresses use [:ethlance contract-addr])
  (let [prepared-args [creator [] job-type arbiters ipfs-data]
        opts {}]
    (println "Calling with: " prepared-args)
    (smart-contracts/contract-send :ethlance :create-job prepared-args (merge {:gas 6000000} opts))))

(defn ze-simple [& [amount opts]]
  (smart-contracts/contract-call :ethlance :ze-simple [amount] (merge {:gas 6000000} opts)))
