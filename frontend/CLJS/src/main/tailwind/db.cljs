(ns tailwind.db)

(defonce state
  {:user {:profile {}
          :auth-result {}}
   :name "CMS Auth Service"
   :auth? true
   :user-created? false
   :user-dropdown? true})
