(ns tailwind.auth0.lock
  (:require [re-frame.core :as re-frame]
            [tailwind.auth0.config :as config]
            [tailwind.auth0.events]
            ["auth0-lock" :as auth0]))
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
                          :theme {:primaryColor "#3A99D8"}})]
    (auth0/Auth0Lock. client-id domain options)))

(defn handle-profile-response [error profile]
  "Handle the response for Auth0 profile request"
  (let [profile-clj (js->clj profile :keywordize-keys true)]

   (re-frame/dispatch [:auth0/set-user-profile profile-clj])))
     
   

(defn on-authenticated
  "Function called by auth0 lock on authentication"
  [auth-result-js]
  (let [auth-result-clj (js->clj auth-result-js :keywordize-keys true)
        access-token (:accessToken auth-result-clj)]
    (re-frame/dispatch [:auth0/set-auth-result auth-result-clj])
    (.getUserInfo lock access-token handle-profile-response)))



(.on lock "authenticated" on-authenticated)
