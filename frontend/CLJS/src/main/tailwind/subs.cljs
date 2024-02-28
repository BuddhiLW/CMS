(ns tailwind.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :user/data
 (fn [db _]
   (get-in db [:user])))

(rf/reg-sub
 :user/profile
 :<- [:user/data]
 (fn [data _]
   (:profile data)))

(rf/reg-sub
 :user/auth-result
 :<- [:user/data]
 (fn [data _]
   (:auth-result data)))

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

(rf/reg-sub
 :user/data-loaded?
 (fn [db _]
  (let [profile     (get-in [:user :profile] db)
        auth-result (get-in [:user :auth-result] db)]
    (js/console.log {:profile profile :auth-result auth-result})
    #_(if (and (seq profile)
               (seq auth-result))
        (do (js/console.log {:profile profile :auth-result auth-result})
            true)
        (do (js/console.log "not sequable")
            false)))))

(rf/reg-sub
 :ui/errors
 (fn [db _]
   (:errors db)))


(rf/reg-sub
 :ui/infos
 (fn [db _]
  (:infos db)))
