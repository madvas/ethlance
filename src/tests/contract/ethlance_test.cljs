(ns tests.contract.ethlance-test
  (:require [bignumber.core :as bn]
            [cljs-web3-next.eth :as web3-eth]
            [cljs-web3-next.evm :as web3-evm]
            [cljs.test :refer-macros [deftest is testing async]]
            [district.server.web3 :refer [web3]]
            [ethlance.server.contract.ethlance :as ethlance]
            [ethlance.shared.smart-contracts-dev :as addresses]
            ["fs" :as fs]

            [district.server.smart-contracts :as smart-contracts]
            [cljs.core.async :refer [<! put! chan go]]
            [district.shared.async-helpers :refer [<?]]))

(defn publish-to-channel-callback [&stuff])
(defn subscribe->chan [contract method options]
  (let [out (chan)
        callback (fn [event]
                   (println "Got an event" event)
                   (put! out event)
                   )
        ]
    (smart-contracts/subscribe-events contract metohd options [publish-to-channel-callback])
    out
    )
  ; (let [kanal (chan)]
  ;   (put! kanal 42)
  ;   kanal)
  )

(deftest ethlance-contract-methods
  ; (testing "Simple calls for sanity"
  ;   (let [result (<! (ethlance/ze-simple 111))]
  ;     (is (= result (str (* 4 111))))))
  (testing "Creating new job"
    (async done
           (go
             (let [[creator-addr] (<! (web3-eth/accounts @web3))
                   creator creator-addr
                   offered-value {:token {:tokenContract {:tokenType 0 :tokenAddress "0xc238fa6ccc9d226e2c49644b36914611319fc3ff"} :tokenId 11} :value 42}
                   job-type 1
                   arbiters []
                   ipfs-data "0x0"
                   job-proxy-address (get-in addresses/smart-contracts [:job :address])
                   job-created-events (subscribe->chan :ethlance :JobCreated {:from-block (<! (web3-eth/get-block-number @web3)) :to-block "latest"})
                   ; job-created-events (chan)
                   ]
                 (<! (ethlance/initialize job-proxy-address))
                 (<! (ethlance/create-job creator [offered-value] job-type arbiters ipfs-data))
                 ; (is (= job-type (<! (job-created-events))))
                 (put! job-created-events 42)
                 (println "We got an event: " (<! job-created-events))
                 (done)
                 ; (smart-contracts/subscribe-events
                 ;   :ethlance :JobCreated
                 ;   {:from-block (<! (web3-eth/get-block-number @web3)) :to-block "latest"}
                 ;   [(fn [_ event]
                 ;      (let [args (:args event)
                 ;            ethlance-address (:address event)
                 ;            created-job-address (:job args)
                 ;            actual-job-type-from-event (int (:job-type args))
                 ;            job-type-promise (smart-contracts/contract-call [:job created-job-address] :job-type [])
                 ;            ]
                 ;        (is (= job-type actual-job-type-from-event))
                 ;        (.then job-type-promise (fn [actual-job-type]
                 ;                                  (do
                 ;                                    (is (= job-type (int actual-job-type)))
                 ;                                    (done))))))])
                 )
             ))))

; (deftest ethlance-contract-methods
;   ; (testing "Simple calls for sanity"
;   ;   (let [result (<! (ethlance/ze-simple 111))]
;   ;     (is (= result (str (* 4 111))))))
;   (testing "Creating new job"
;     (async done
;            (go
;              (let [[creator-addr] (<! (web3-eth/accounts @web3))
;                    creator creator-addr
;                    offered-value {:token {:tokenContract {:tokenType 0 :tokenAddress "0xc238fa6ccc9d226e2c49644b36914611319fc3ff"} :tokenId 11} :value 42}
;                    job-type 1
;                    arbiters []
;                    ipfs-data "0x0"
;                    job-proxy-address (get-in addresses/smart-contracts [:job :address])]
;                  (<! (ethlance/initialize job-proxy-address))
;                  (<! (ethlance/create-job creator [offered-value] job-type arbiters ipfs-data))
;                  (smart-contracts/subscribe-events
;                    :ethlance :JobCreated
;                    {:from-block (<! (web3-eth/get-block-number @web3)) :to-block "latest"}
;                    [(fn [_ event]
;                       (let [args (:args event)
;                             ethlance-address (:address event)
;                             created-job-address (:job args)
;                             actual-job-type-from-event (int (:job-type args))
;                             job-type-promise (smart-contracts/contract-call [:job created-job-address] :job-type [])
;                             ]
;                         (is (= job-type actual-job-type-from-event))
;                         (.then job-type-promise (fn [actual-job-type]
;                                                   (do
;                                                     (is (= job-type (int actual-job-type)))
;                                                     (done))))))]))
;              ))))

; (deftest ethlance-erc20-support
;   (testing "Creating job paid with ERC20 token"
;     (async done
;            (go
;              (let [ethlance-addr (smart-contracts/contract-address :ethlance)
;                    [owner employer worker] (<! (web3-eth/accounts @web3))
;                     fund-employer (<? (smart-contracts/contract-send :token :mint [employer 4]))
;                     approval (<? (smart-contracts/contract-send :token :approve [employer ethlance-addr 1] {:from employer}))]
;                (println "Employer was funded: " fund-employer)
;                (println "Approval" approval)
;                (println "Addresses: " {:owner owner :employer employer :worker worker})
;                (is (bn/= (<? (smart-contracts/contract-call :token :balance-of [employer])) 4))
;                (done))))))
