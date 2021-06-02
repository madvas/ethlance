(ns tests.contract.ethlance-test
  (:require [bignumber.core :as bn]
            [cljs-web3-next.eth :as web3-eth]
            [cljs-web3-next.evm :as web3-evm]
            [cljs.test :refer-macros [deftest is testing async]]
            [district.server.web3 :refer [web3]]
            [ethlance.server.contract.ethlance :as ethlance]
            [cljs.core.async :refer [go <!]]
            [district.shared.async-helpers :refer [<?]]))

(deftest test-sanity
  (is (= 1 1)))

(defn create-job
  "Creates new job and returns JobCreated event data"
  []
  (let [creator "0xaddress"
        offered-values {:token-type "ETH" :value 0.1 :token-address "0xaddress"}
        job-type "GIG"
        invited-arbiters ["0xaddress"]
        ipfs-data "bytes-bytes"]
    (go
      (try
        (println "-----> BEFORE")
        (<! (ethlance/create-job))
        (println "-----> AFTER")
        (catch :default e false)))))

(deftest ethlance-contract-methods
  (async done
         (go
           (let [[creator-addr] (<! (web3-eth/accounts @web3))]

             ; Test process
             ; 1. use https://github.com/district0x/district-server-smart-contracts/blob/master/src/district/server/smart_contracts.cljs#L169
             ;    to subscribe to the event
             ; 2. Get the created contract address from the event
             ; 3. Inspect contract state to see that the passed params were persisted
             (testing "Simple calls for sanity"
               (let [result (<! (ethlance/ze-simple 111))]
                 (is (= result (str (* 42 111))))
                 (println "Count is " creator-addr)))

             (testing "Creating new job"
               (let [creator  "0xc238fa6ccc9d226e2c49644b36914611319fc3ff"
                     offered-value {:token {:tokenContract {:tokenType 0 :tokenAddress "0xc238fa6ccc9d226e2c49644b36914611319fc3ff"} :tokenId 11} :value 42}
                     job-type 1
                     arbiters ["0xc238fa6ccc9d226e2c49644b36914611319fc3ff"]
                     ipfs-data "0xc238fa6ccc9d226e2c49644b36914611319fc3ff"
                     result (<! (ethlance/create-job creator [offered-value] job-type arbiters ipfs-data))]
                 (is (= result "something?"))
                 ))
             (done)))))
