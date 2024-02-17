(ns tailwind.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :user/name
 (fn [db _]
   (:name db)))

(rf/reg-sub
 :user/auth?
 (fn [db _]
   (:auth? db)))

(rf/reg-sub
 :user/user-dropdown?
 (fn [db _]
   (:user-dropdown? db)))
