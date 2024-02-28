(ns tailwind.db)

(defonce state
  {:user {:profile {}
          :auth-result {}}
   :name "CMS Auth Service"
   :auth? true
   :errors {}
   :infos {}
   :user-created? false
   :user-dropdown? true})
