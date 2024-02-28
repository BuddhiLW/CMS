(ns tailwind.events
  (:require
   [tailwind.db :as db]
   [re-frame.core :as rf]))

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   db/state))

(rf/reg-event-db
 :login
 (fn [db _]
   (assoc db :auth? true)))

#_(rf/reg-event-db
   :logout
   (fn [db _]
     (-> db
      (assoc-in [:user :profile] {})
      (assoc-in [:user :auth-result] {}))))

(rf/reg-event-db
 :toggle-user-dropdown
 (fn [db _]
   (let [dropdown (:user-dropdown? db)]
     (assoc db :user-dropdown? (not dropdown)))))
;; (defn login
;;   []
;;   (swap! db/state assoc :auth? true))

;; (defn logout
;;   []
;;   (swap! db/state assoc :auth? false))

;; (defn toggle-user-dropdown
;;   []
;;   (let [dropdown (:user-dropdown? db/state)]
;;     (swap! db/state assoc :user-dropdown? (not dropdown))))

(rf/reg-event-fx
 :debug/js-console-log
 (fn [_ _]
   (js/console.log "an event")))

(rf/reg-event-db
 :ui/clear-error
 (fn [db [_ key]]
  (update-in db [:errors] dissoc key)))


(rf/reg-event-db
 :ui/clear-info
 (fn [db [_ key]]
  (update-in db [:infos] dissoc key)))

(comment
  (let [err {:errors {:ui "ui"}}]
    (update-in err [:errors] assoc :ux "ux")))
