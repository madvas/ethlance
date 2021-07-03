(ns tests.runner
  (:require
   [cljs.nodejs :as nodejs]
   [cljs.test :refer [run-tests]]
   [tests.contract.ethlance-test]
   [district.server.logging]
   [district.server.web3]
   [tests.contract.ethlance-test]
   [district.shared.async-helpers :as async-helpers]
   [ethlance.shared.smart-contracts-dev :refer [smart-contracts]]
   [district.server.smart-contracts]
   [mount.core :as mount]
   [taoensso.timbre :as log]))

(nodejs/enable-util-print!)

(async-helpers/extend-promises-as-channels!)

(defn start-and-run-tests []
  (-> (mount/with-args {:web3 {:url "ws://192.168.12.1:7545"}
                        :smart-contracts {:contracts-var #'smart-contracts}
                        :logging {:level :info
                                  :console? false}})
      (mount/only [#'district.server.logging/logging
                   #'district.server.web3/web3
                   #'district.server.smart-contracts/smart-contracts])
      (mount/start)
      (as-> $ (log/warn "Started" $)))

  (run-tests
   'tests.contract.ethlance-test))

(println "------------> Running tests")
(start-and-run-tests)
