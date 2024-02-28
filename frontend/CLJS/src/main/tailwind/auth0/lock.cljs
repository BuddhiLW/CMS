(ns tailwind.auth0.lock
  (:require [re-frame.core :as re-frame]
            [tailwind.auth0.config :as config]
            [tailwind.auth0.events]
            ["auth0-lock" :as auth0]))
;; [cljs.core.async :refer [go]]
;; [cljs.core.async.interop :refer-macros [<p!]]))
;; [auth0-lock :as Auth0Lock]))
;; [cljsjs.auth0-lock]))

(def lock
  "The auth0 lock instance used to login and make requests to Auth0"
  (let [client-id (:client-id config/auth0)
        domain (:domain config/auth0)
        options (clj->js {:allowed-connections ["google-oauth2"]
                          :rememberLastLogin false
                          :allow-login true
                          :container "auth0-lock"
                          :audience (:audience config/auth0)
                          :theme {:primaryColor "#3A99D8"}})]
    (auth0/Auth0Lock. client-id domain options)))

(defn handle-profile-response
  [auth-result-clj error profile]
  "Handle the response for Auth0 profile request"
  (let [profile-clj (js->clj profile :keywordize-keys true)]
   (re-frame/dispatch [:auth0/set-user-profile profile-clj])
   (re-frame/dispatch-sync [:auth0/issue-token {:auth-result auth-result-clj
                                                :profile profile-clj}])))
   ;; (re-frame/dispatch [:back-end/create-user {:auth-result auth-result-clj
   ;;                                            :profile profile-clj}])))

    ;; profile-clj))

(defn on-authenticated
  "Function called by auth0 lock on authentication"
  [auth-result-js]
  (let [auth-result-clj (js->clj auth-result-js :keywordize-keys true)
        access-token (:accessToken auth-result-clj)]
    (re-frame/dispatch [:auth0/set-auth-result auth-result-clj])
    (.getUserInfo lock access-token (partial handle-profile-response auth-result-clj))))


(.on lock "authenticated" on-authenticated)
