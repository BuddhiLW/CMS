(ns tailwind.app
  (:require
   [goog.dom :as gdom]
   [re-frame.core :as rf]
   [reagent.dom :as dom]
   [day8.re-frame.http-fx]
   [tailwind.auth0.config :as config]
   [tailwind.auth0.events]
   [tailwind.subs]
   [tailwind.db]
   [tailwind.events]
   [tailwind.views :as views]))


;; [views/authenticated]
(defn- app
  []
  (let [user-name @(rf/subscribe [:auth0/user-name])]
   (if user-name
     [views/authenticated]
     [views/public])))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn- render []
  (reagent.dom/render [app] (gdom/getElement "app")))

;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (rf/clear-subscription-cache!)
  (render))

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (js/console.log "init")
  (rf/dispatch-sync [:initialize-db])
  (dev-setup)
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop"))

(defn- ^:dev/after-load re-render
  "The `:dev/after-load` metadata causes this function to be called after
  shadow-cljs hot-reloads code. This function is called implicitly by its
  annotation."
  []
  (render))


#_[:div "hello"
       [:> Auth0Provider {:domain "dev-fxcu8cjbww7ragnp.us.auth0.com"
                          :client-id "hD2zzhcv6WyO2UPQ8OEJb1Azn9O0FWPe"}
                               ;; :redirect-uri js/window.location.origin}}
        [:> Auth0Wrap]]
       #_[:> Auth0Provider #_{:domain "dev-fxcu8cjbww7ragnp.us.auth0.com"
                              :client-id "hD2zzhcv6WyO2UPQ8OEJb1Azn9O0FWPe"
                              :redirect-uri js/window.location.origin}
          [:> Auth0Wrap]
          [:div "hello"]
          #_(if isAuthenticated
              "logged"
              "not logged")]]

#_(defn Auth0Wrap
    []
    (let [use-auth0 (useAuth0)
          login-with-popup (.-loginWithPopup use-auth0)
          login-with-redirect (.-loginWithRedirect use-auth0)
          is-authenticated? (.-isAuthenticated use-auth0)
          is-loading? (.-isLoading use-auth0)]
      [:ul
       [:li
        [:button [:on-click (login-with-popup)]]
        [:button
         #_{:on-click (login-with-redirect)}]]]))

        ;; loaded? (reseda/useStore store :loaded?)]))
        ;; loader ($ Loader {:title "Authenticating"})]))
    ;; (hooks/use-effect :auto-deps
    ;;                   (when (and (not is-authenticated?)
    ;;                              (not is-loading?))
    ;;                     (login-with-redirect)))
    ;; (cond
    ;;   (not (false? is-loading?))
    ;;   loader

    ;;   loaded?
    ;;   ($ App)

    ;;   is-authenticated?
    ;;   (do
    ;;     (.then ((.-getIdTokenClaims use-auth0))
    ;;            #(start-graphql-after-auth % use-auth0))
    ;;     loader)
    ;;   :else nil)))
