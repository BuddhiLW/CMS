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


;; data        {:auth-result auth-result
;;              :profile profile}]
(rf/reg-event-fx
 :back-end/create-user
 (fn [{:keys [db]} [_ data]]
   (let [profile     (:profile data)
         auth-result (:auth-result data)
         sub         (:sub profile)
         token       (:accessToken auth-result)]
    ;; (js/console.log (.stringify js/JSON (clj->js data)))
    ;; (js/console.log token)
    {:http-xhrio {:method :post
                  :uri (str backend "/create-user")
                  :params {:sub sub}
                  :headers {"authorization" (str "Bearer " "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Incta0hsWDUwbHd3ZklRQ2F4STNXaCJ9.eyJpc3MiOiJodHRwczovL2Rldi1meGN1OGNqYnd3N3JhZ25wLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJ5NXB5T25naW1xR2JKRDhMVnZIRGhtZHZORzFxVXFVMkBjbGllbnRzIiwiYXVkIjoiaHR0cHM6Ly9idWRkaGlsdy5jb20vZ28tY21zLWF1dGgvIiwiaWF0IjoxNzA4OTc2MDMxLCJleHAiOjE3MDkwNjI0MzEsImF6cCI6Ink1cHlPbmdpbXFHYkpEOExWdkhEaG1kdk5HMXFVcVUyIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIn0.A3NUQtJi8fOLrkir_AAbRdH1ZbJbpEHBk8uyEDVq85Eo5l8m9ijB9lxpHW3eTCEfdVp-V1FS7x7vdeFtxJ-DyerbqrnTCook9EK4_y_mA7TrDWH3zKwaC3vnOT1YtuT_6hIINtpHQl_gpwRI1sHM3_EfsXpuDy0GnVjaukESfDV9M1WDL1eV2j8J8J-hV0a8ey-UWeFoFyp0Pa_NWwrM1V_w_09Eiz2YBRyECFOcib6uetNMY51crg13KfvnniXQazzoFHtDrEKWcEV9KepWCc7lbw41sPDpW92C2k-oZVCR7K9NoZHyomk1vG6OaKNUWIpoJmFLX247Kulc4EovmA")
                            "Content-Type" "application/json"}
                  :body (.stringify js/JSON (clj->js data))
                  :timeout 400
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:auth0/user-created]
                  :on-failure [:auth0/user-create-failed]}})))

(rf/reg-event-db
 :auth0/user-created
 (fn [db _]
   (assoc-in db [:user-created?] true)))

(rf/reg-event-db
 :auth0/user-create-failed
 (fn [db _]
   (assoc-in db [:errors] {:user-create "Failed to create user"})))

;;; subscriptions
(rf/reg-sub
 :auth0/user-name
 (fn [db]
   (get-in db [:user :profile :name])))
