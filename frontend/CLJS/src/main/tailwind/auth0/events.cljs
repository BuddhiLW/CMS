(ns tailwind.auth0.events
  (:require
   [ajax.core :as ajax]
   [re-frame.core :as rf]
   [reagent.core :as r]))

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


;; data        {:auth-result auth-result
;;              :profile profile}]

(rf/reg-event-fx
 :back-end/create-user
 (fn [{:keys [db]} [_ data resp]]
   (let [profile     (:profile data)
         ;; auth-result (:auth-result data)
         sub         (:sub profile)
         token       (:access_token (:data resp))]
     (js/console.log (clj->js {:data data
                               :resp resp}))
     (js/console.info (clj->js (:message resp)))
     {:db (assoc-in db [:token] (:access_token (:data resp)))
      :http-xhrio {:method :post
                   :uri (if (seq sub)
                          (str backend "/create-user" "?sub=" sub)
                          (str backend "/create-user"))
                   :headers {"authorization" (str "Bearer " token)
                             "Content-Type" "application/json"}
                   :body (.stringify js/JSON (clj->js data))
                   :timeout 400
                   :format          (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success [:auth0/user-created]
                   :on-failure [:auth0/user-create-failed]}})))
;; {"client_id":"y5pyOngimqGbJD8LVvHDhmdvNG1qUqU2","client_secret":"gQOKtxEw4svwuejHGh7B1VE6nSSE24EQA1UKci1hAsqW40MFrc15LH48nI95H143","audience":"https://buddhilw.com/go-cms-auth/","grant_type":"client_credentials"}'

(rf/reg-event-fx
 :auth0/issue-token
 (fn [_ [_ data]]
   {:http-xhrio {:method :get
                 :uri (str backend "/issue-token")
                 :headers {"Content-Type" "application/json"}
                 :timeout 500
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:back-end/create-user data]
                 :on-failure [:auth0/token-issue-failed]}}))

(rf/reg-event-db
 :auth0/token-issue-failed
 (fn [db [_ resp]]
   (js/console.info "Issuing token failed: ")
   (js/console.error (clj->js resp))
   (let [last-error (str "Couldn't fetch user token: " (:last-error resp) ".\n Please try to Log In again later.")]
    (update-in db [:errors] assoc :token last-error))))

(rf/reg-event-db
 :auth0/user-created
 (fn [db [_ resp]]
   (js/console.info (:message resp))
   (-> db
       (assoc-in [:user-created?] true)
       (update-in [:infos] assoc :user/created (:message resp)))))


(rf/reg-event-db
 :auth0/user-create-failed
 (fn [db _]
  (js/console.log "Failed to create user...")
  (update-in db [:errors] assoc :user-create "Failed to create user")))

;;; subscriptions
(rf/reg-sub
 :auth0/user-name
 (fn [db]
   (get-in db [:user :profile :name])))

