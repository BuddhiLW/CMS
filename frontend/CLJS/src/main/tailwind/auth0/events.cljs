(ns tailwind.auth0.events
  (:require
   [re-frame.core :as rf]))

;;; Events
(rf/reg-event-db
  :auth0/set-auth-result
  (fn [db [_ auth-result]]
    (js/console.log "set-auth-result" auth-result)
    (js/console.log "db" db)
    (assoc-in db [:user :auth-result] auth-result)))

(rf/reg-event-db
  :auth0/set-user-profile
  (fn [db [_ profile]]
    (js/console.log "set-user-profile" profile)
    (js/console.log "db" db)
    (assoc-in db [:user :profile] profile)))

(rf/reg-event-db
  :auth0/logout
  (fn [db [_ profile]]
    (dissoc db :user)))

;;; subscriptions
(rf/reg-sub
  :auth0/user-name
  (fn [db]
    (get-in db [:user :profile :name])))
