(ns tailwind.views
  (:require
   [re-frame.core :as rf]
   [tailwind.auth0.lock :as auth0]
   [tailwind.auth0.events]
   [reagent.core :as r]))
;; [reagent.core :as reagent]

(defn button [text on-click]
  [:button
   {:class    "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
    :type     "button"
    :on-click on-click}
   text])

(def login-re-frame-debug
  (button "reframe" #(rf/dispatch [:debug/js-console-log])))

;; (def login-button
;;   (button "Log in" #(.show auth0/lock)))

;; (def logout-button
;;   (button "Log out" #(rf/dispatch [:auth0/logout])))

(defn original
  []
  [:div {:class "min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8"}
   [:div {:class "sm:mx-auto sm:w-full sm:max-w-md"}
    [:h2 {:class "mt-6 text-center text-3xl font-extrabold text-gray-900"} "Sign in to your account"]
    [:p {:class "mt-2 text-center text-sm text-gray-600 max-w"} "Or "
     [:a {:href "#" :class "font-medium text-pink-600 hover:text-pink-500"} "start your 14-day free trial"]]]
   [:div {:class "mt-8 sm:mx-auto sm:w-full sm:max-w-md"}
    [:div {:class "bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10"}
     [:form {:class "space-y-6" :action "#" :method "POST"}
      [:div
       [:label {:for "email" :class "block text-sm font-medium text-gray-700"} "Email address"]
       [:div {:class "mt-1"}
        [:input {:id "email" :name "email" :type "email" :auto-complete "email" :required true :class "appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-pink-500 focus:border-pink-500 sm:text-sm"}]]]
      [:div
       [:label {:for "password" :class "block text-sm font-medium text-gray-700"} "Password"]
       [:div {:class "mt-1"}
        [:input {:id "password" :name "password" :type "password" :auto-complete "current-password" :required true :class "appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-pink-500 focus:border-pink-500 sm:text-sm"}]]]
      [:div {:class "flex items-center justify-between"}
       [:div {:class "flex items-center"}
        [:input {:id "remember_me" :name "remember_me" :type "checkbox" :class "h-4 w-4 text-pink-600 focus:ring-pink-500 border-gray-300 rounded"}]
        [:label {:for "remember_me" :class "ml-2 block text-sm text-gray-900"} "Remember me"]]
       [:div {:class "text-sm"}
        [:a {:href "#" :class "font-medium text-pink-600 hover:text-pink-500"} "Forgot your password?"]]]
      [:div
       [:button
        {:type "submit"
         :on-click #(rf/dispatch [:login])
         :class "w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-pink-600 hover:bg-pink-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-pink-500"}
        "Sign in"]]]]]])

#_(defn lock
    []
    (fn []
      (reagent/create-class
       {:component-did-mount
        (fn [] (.show auth0/lock))
        :reagent-render
        (fn [] [:div#auth0-lock])})))

(defn public
  []
  (fn []
    [:div {:class "flex flex-col mx-auto space-y-4 font-mono text-sm font-bold leading-6 md:max-w-md lg:max-w-md"}
     [:h2 {:class "flex font-bold text-3xl sm:justify-center"}
      [:<>
       [:span "LOG"]
       [:span {:class "bg-[#f84525] text-white px-2 rounded-md"}
        "IN"]]]
     [:button {:class "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
               :on-click (fn [e]
                           (.show auth0/lock))}
      "Click to Log in or Sign up"]
     [:div#login.container {:class "flex flex-col"}
      [:div#auth0-lock]]]))

(defn settings []
  (fn []
    [:div {:class "z-10 mx-3 origin-top absolute right-0 left-0 mt-1 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 divide-y divide-gray-200 focus:outline-none" :role "menu" :aria-orientation "vertical" :aria-labelledby "options-menu"}
     [:div {:class "py-1" :role "none"}
      [:a {:href "#" :class "block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900" :role "menuitem"} "View profile"]
      [:a {:href "#" :class "block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900" :role "menuitem"} "Settings"]
      [:a {:href "#" :class "block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900" :role "menuitem"} "Notifications"]]
     [:div {:class "py-1" :role "none"}
      [:a {:href "#"
           :on-click #(rf/dispatch [:auth0/logout])
           :class "block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 hover:text-gray-900" :role "menuitem"}
       "Logout"]]]))

(defn error-message
  [error]
  (fn []
    (let [[key error-message] error]
      [:div {:id "alert-additional-content-2",
             :class "p-4 mb-4 text-red-800 border border-red-300 rounded-lg bg-red-50 dark:bg-gray-800 dark:text-red-400 dark:border-red-800",
             :role "alert"}
       [:div {:class "flex items-center"}
        [:svg {:class "flex-shrink-0 w-4 h-4 me-2",
               :aria-hidden "true",
               :xmlns "http://www.w3.org/2000/svg",
               :fill "currentColor",
               :viewBox "0 0 20 20"}
         [:path
          {:d
           "M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1 1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1 1 1v4h1a1 1 0 0 1 0 2Z"}]]
        [:span {:class "sr-only"} "Info"]
        [:h3 {:class "text-lg font-medium"} (name key)]]
       [:div
        {:class "mt-2 mb-4 text-sm"}
        (str error-message)]
       [:div
        {:class "flex"}
        [:button
         {:type "button",
          :class "text-white bg-red-800 hover:bg-red-900 focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-xs px-3 py-1.5 me-2 text-center inline-flex items-center dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-800"}
         [:svg
          {:class "me-2 h-3 w-3",
           :aria-hidden "true",
           :xmlns "http://www.w3.org/2000/svg",
           :fill "currentColor",
           :viewBox "0 0 20 14"}
          [:path
           {:d
            "M10 0C4.612 0 0 5.336 0 7c0 1.742 3.546 7 10 7 6.454 0 10-5.258 10-7 0-1.664-4.612-7-10-7Zm0 10a3 3 0 1 1 0-6 3 3 0 0 1 0 6Z"}]]
         "View more"]
        [:button
         {:type "button",
          :class
          "text-red-800 bg-transparent border border-red-800 hover:bg-red-900 hover:text-white focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-xs px-3 py-1.5 text-center dark:hover:bg-red-600 dark:border-red-600 dark:text-red-500 dark:hover:text-white dark:focus:ring-red-800",
          :data-dismiss-target "#alert-additional-content-2",
          :aria-label "Close"
          :on-click #(rf/dispatch [:ui/clear-error key])}
         "Dismiss"]]])))

(defn info-message
  [info]
  (fn []
    (let [[key info-message] info]
      [:div {:id "alert-additional-content-1",
             :class "p-4 mb-4 text-blue-800 border border-blue-300 rounded-lg bg-blue-50 dark:bg-gray-800 dark:text-blue-400 dark:border-blue-800",
             :role "alert"}
       [:div {:class "flex items-center"}
        [:svg {:class "flex-shrink-0 w-4 h-4 me-2",
               :aria-hidden "true",
               :xmlns "http://www.w3.org/2000/svg",
               :fill "currentColor",
               :viewBox "0 0 20 20"}
         [:path
          {:d "M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1 1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1 1 1v4h1a1 1 0 0 1 0 2Z"}]]
        [:span {:class "sr-only"} "Info"]
        [:h3 {:class "text-lg font-medium"} (str key)]]
       [:div {:class "mt-2 mb-4 text-sm"}
        (str info-message)]
       [:div
        {:class "flex"}
        [:button
         {:type "button",
          :class
          "text-white bg-blue-800 hover:bg-blue-900 focus:ring-4 focus:outline-none focus:ring-blue-200 font-medium rounded-lg text-xs px-3 py-1.5 me-2 text-center inline-flex items-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"}
         [:svg
          {:class "me-2 h-3 w-3",
           :aria-hidden "true",
           :xmlns "http://www.w3.org/2000/svg",
           :fill "currentColor",
           :viewBox "0 0 20 14"}
          [:path {:d "M10 0C4.612 0 0 5.336 0 7c0 1.742 3.546 7 10 7 6.454 0 10-5.258 10-7 0-1.664-4.612-7-10-7Zm0 10a3 3 0 1 1 0-6 3 3 0 0 1 0 6Z"}]]
         "View more"]
        [:button {:type "button",
                  :on-click #(rf/dispatch [:ui/clear-info key])
                  :class "text-blue-800 bg-transparent border border-blue-800 hover:bg-blue-900 hover:text-white focus:ring-4 focus:outline-none focus:ring-blue-200 font-medium rounded-lg text-xs px-3 py-1.5 text-center dark:hover:bg-blue-600 dark:border-blue-600 dark:text-blue-400 dark:hover:text-white dark:focus:ring-blue-800",
                  :data-dismiss-target "#alert-additional-content-1",
                  :aria-label "Close"}
         "Dismiss"]]])))

(defn teams
  []
  [:nav {:class "px-3 mt-12"}
   [:div {:class "space-y-2"}
    [:a {:href "#" :class "bg-gray-200 text-gray-900 group flex items-center px-2 py-2 text-sm font-medium rounded-md"}
     [:svg {:class "text-gray-500 mr-3 h-6 w-6" :xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor" :aria-hidden "true"}
      [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"}]] "Home"]
    [:a {:href "#" :class "text-gray-700 hover:text-gray-900 hover:bg-gray-50 group flex items-center px-2 py-2 text-sm font-medium rounded-md"}
     [:svg {:class "text-gray-400 group-hover:text-gray-500 mr-3 h-6 w-6" :xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor" :aria-hidden "true"}
      [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M4 6h16M4 10h16M4 14h16M4 18h16"}]] "My tasks"]
    [:div {:class "mt-2"}
     [:h3 {:class "px-3 text-xs font-semibold text-gray-500 uppercase tracking-wider" :id "teams-headline"} "Teams"]
     [:div {:class "mt-6 space-y-1" :role "group" :aria-labelledby "teams-headline"}
      [:a {:href "#" :class "group flex items-center px-3 py-0.5 text-sm font-medium text-gray-700 rounded-md hover:text-gray-900 hover:bg-gray-50"}
       [:span {:class "w-2.5 h-2.5 mr-4 bg-pink-500 rounded-full" :aria-hidden "true"}]
       [:span {:class "truncate"} "Software Engineering"]]
      [:a {:href "#" :class "group flex items-center px-3 py-2 text-sm font-medium text-gray-700 rounded-md hover:text-gray-900 hover:bg-gray-50"}
       [:span {:class "w-2.5 h-2.5 mr-4 bg-pink-500 rounded-full" :aria-hidden "true"}]
       [:span {:class "truncate"} "Engineering Physics"]]]]]])

(defn authenticated
  []
  (fn []
    (let [{:keys [;; email email_verified family_name given_name locale sub updated_at
                  name nickname picture ]} @(rf/subscribe [:user/profile])
          drop-down? @(rf/subscribe [:user/user-dropdown?])]
      [:div {:class "h-screen flex overflow-hidden bg-white"}
       [:div {:class "hidden lg:flex lg:flex-shrink-0"}
        [:div {:class "flex flex-col w-64 border-r border-gray-200 pt-5 pb-4 bg-gray-100"}
         [:div {:class "flex items-center flex-shrink-0 px-6"}
          [:div {:class "text-center text-3xl font-bold text-gray-900"} "Buddhi CMS"]]
         [:div {:class "h-0 flex-1 flex flex-col overflow-y-auto"}
          [:div {:class "px-3 mt-6 relative inline-block text-left"}
           [:div
            [:button {:type "button"
                      :on-click #(rf/dispatch [:toggle-user-dropdown])
                      :class "group w-full bg-gray-100 rounded-md px-3.5 py-2 text-sm font-medium text-gray-700 hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-100 focus:ring-pink-500"
                      :id "options-menu"
                      :aria-expanded "false"
                      :aria-haspopup "true"}
             [:span {:class "flex w-full justify-between items-center"}
              [:span {:class "flex min-w-0 items-center justify-between space-x-3"}
               [:img {:class "w-10 h-10 bg-gray-300 rounded-full flex-shrink-0"
                      :src (or picture
                               "https://images.unsplash.com/photo-1502685104226-ee32379fefbe?ixlib=rb-1.2.1&ixqx=4cZVjZZC0A&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=3&w=256&h=256&q=80")
                      :alt ""}]
               [:span {:class "flex-1 min-w-0"}
                [:span {:class "text-gray-900 text-sm font-medium truncate"} (or
                                                                              name
                                                                              "Jessy Schwarz")]
                [:br]
                [:span {:class "text-gray-500 text-sm truncate"} (or (str "@" nickname)
                                                                     "@jschwarz")]]]
              [:div {:class "flex min-w-0 items-center justify-between space-x-3"}
               [:svg {:class "flex-shrink-0 h-5 w-5 text-gray-400 group-hover:text-gray-500" :xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 20 20" :fill "currentColor" :aria-hidden "true"}
                [:path {:fill-rule "evenodd" :d "M10 3a1 1 0 01.707.293l3 3a1 1 0 01-1.414 1.414L10 5.414 7.707 7.707a1 1 0 01-1.414-1.414l3-3A1 1 0 0110 3zm-3.707 9.293a1 1 0 011.414 0L10 14.586l2.293-2.293a1 1 0 011.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" :clip-rule "evenodd"}]]]]]]
           (when drop-down?
             [settings])]
          [teams]]]]
       [:div {:class "flex flex-col w-0 flex-1 overflow-hidden"}
        [:main {:class "flex-1 relative z-0 overflow-y-auto focus:outline-none" :tabIndex "0"}
         [:div {:class "px-4 mt-6 sm:px-6 lg:px-8"}
          [:h2 {:class "text-gray-500 text-xs font-medium uppercase tracking-wide"} "Main"]]
         [:div
          (when (seq @(rf/subscribe [:ui/errors]))
            (let [errors @(rf/subscribe [:ui/errors])]
              [:<>
               (for [error (seq errors)]
                 ^{:key (random-uuid)}
                 [:div [error-message error]])]))]
         [:div
          (when (seq @(rf/subscribe [:ui/infos]))
            (let [infos @(rf/subscribe [:ui/infos])]
              [:<>
               (for [info (seq infos)]
                 ^{:key (random-uuid)}
                 [:div [info-message info]])]))]]]])))
