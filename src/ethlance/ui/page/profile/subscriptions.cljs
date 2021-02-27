(ns ethlance.ui.page.profile.subscriptions
  (:require [ethlance.ui.page.profile.events :as profile.events]
            [ethlance.ui.subscription.utils :as subscription.utils]
            [re-frame.core :as re]))

(def create-get-handler #(subscription.utils/create-get-handler profile.events/state-key %))

(re/reg-sub
  ::job-roles-raw
  (fn [db _] (:job-role-search db)))

(re/reg-sub
  ::job-roles-per-address-role
  :<- [::job-roles-raw]
  (fn [job-roles [_ address role]]
    (get-in job-roles [address role])))

(re/reg-sub
  ::jobs
  (fn [db _] (:jobs db)))

(re/reg-sub
  ::job-roles
  (fn [[_ address role] _]
    [(re/subscribe [::jobs]) (re/subscribe [::job-roles-per-address-role address role])])

  (fn [[jobs role-data] _]
    (map #(assoc % :title (get-in jobs [(:job-id %) :job/title])) role-data)))
