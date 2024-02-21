(ns tailwind.auth0.events
  (:require
   [re-frame.core :as rf]
   [ajax.core :as ajax]))

(def backend "http://localhost:3010/v1")

;;; Events
(rf/reg-event-fx
  :auth0/set-auth-result
  (fn [{:keys [db]} [_ auth-result]]
    {:db (assoc-in db [:user :auth-result] auth-result)}))

(rf/reg-event-fx
  :auth0/set-user-profile
  (fn [{:keys [db]} [_ profile]]
    {:db (assoc-in db [:user :profile] profile)}))
;; (re-frame/dispatch [:back-end/create-user])

(rf/reg-event-db
  :auth0/logout
  (fn [db [_ profile]]
    (dissoc db :user)))

(rf/reg-event-fx
  :back-end/create-user
  (fn [{:keys [db]} _]
    (let [profile     (get-in [:user :profile] db)
          auth-result (get-in [:user :auth-result] db)
          sub         (:sub profile)
          token       (:accessToken auth-result)
          data        {:auth-result auth-result
                       :profile profile}]
     (js/console.log (str "Creating user:\n" (.stringify js/JSON (clj->js data))))
     {:http-xhrio {:method :post
                   :uri (str backend "/create-user")
                   :params {:sub sub}
                   :header {"Authorization" (str "Bearer " token)
                            "Content-Type" "application/json"}
                            ;; "Access-Control-Allow-Credentials" "true"}
                   :body (.stringify js/JSON (clj->js data))
                   :timeout 400
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success [:auth0/user-created]
                   :on-failure [:auth0/user-create-failed]}})))

;;; subscriptions
(rf/reg-sub
  :auth0/user-name
  (fn [db]
    (get-in db [:user :profile :name])))
