(ns ethlance.server.contract.ethlance
  (:require [ethlance.server.contract :refer [call]]
            [district.server.smart-contracts :as smart-contracts]))

(defn create-job [creator offered-value job-type arbiters ipfs-data]
  ; When there's only one contract the 1st param to contract-send can be just name of the contract (symbol)
  ; For contracts that have multiple copies on different addresses use [:ethlance contract-addr])
  (let [prepared-args [creator offered-value job-type arbiters ipfs-data]
        opts {}]
    (smart-contracts/contract-call :ethlance :create-job prepared-args (merge {:gas 6000000} opts))))

(defn ze-simple [& [amount opts]]
  (smart-contracts/contract-call :ethlance :ze-simple [amount] (merge {:gas 6000000} opts)))
