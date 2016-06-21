(ns urlbookmarks.core
    (:require [reagent.core :as r]
              [ajax.core :as ajax]
              [secretary.core :as secretary :refer-macros [defroute]]))

(enable-console-print!)

(declare home)

(secretary/set-config! :prefix "#")

(def category ["teaching" "cooking" "sports" "lifestyle" "books"])

(def ubm (r/atom []))

(def id (r/atom 0))

(defn inc-id [cnt]
  (swap! cnt inc))

(defn url-map [url1 description1 category1]
  (hash-map :id (inc-id id) :url url1 :description description1 :category category1))
  
(defn add-to-vector [item]
  (swap! ubm conj item))

(def add-url (do
               (add-to-vector (url-map "www.google.com" "searching for anything" "books"))
               (add-to-vector (url-map "www.example.com" "show examples" "teaching"))))

(defn remove-from-vector [ubm1 id1]
  (remove #(= (:id %) id1) @ubm1))

(defn book-marks []
  [:div
   [:table
    [:thead
     [:tr
      [:th "Id"]
      [:th "Url"]
      [:th "Description"]
      [:th "Category"]]]
    [:tbody
     (for [i (range (count @ubm))]
       ^{:key i}
       [:tr
        [:td (get-in @ubm [i :id])]
        [:td (get-in @ubm [i :url])]
        [:td (get-in @ubm [i :description])]
        [:td (get-in @ubm [i :category])]
        [:td
         [:button {:on-click #(reset! ubm (remove-from-vector ubm (get-in @ubm [i :id])))} "Delete"]]])]]
   [:p (str @ubm)]])

(defroute "/display" []
  (home [book-marks]))

(defn root-component []
  (let [url (r/atom nil)
        desc (r/atom nil)
        cate (r/atom nil)]
    (fn []
      [:div
       [:h1 "Url Bookmarks"]
       [:div 
        
        [:button {:type "submit"
                  :on-click #(secretary/dispatch! "/display")} "List All Bookmarks"]]
       [:div
        [:span "Url"]
        [:input {:type "text"
                 :value @url
                 :on-change #(reset! url (-> % .-target .-value))}]]
       [:div
        [:span "Description  "]
        [:input {:type "text"
                 :value @desc
                 :on-change #(reset! desc (-> % .-target .-value))}]]
       [:div
        [:span "Category"]
        [:select {:on-change #(reset! cate (-> % .-target .-value))}
         (for [i (range (count category))]
           [:option (get category i)])]]
       [:div
        [:span
         [:button {:on-click #(reset! ubm (add-to-vector (url-map @url @desc @cate)))} "Add Bookmark"]
         [:button {:on-click #(do
                                (reset! url nil)
                                (reset! desc nil))} "Reset"]]]
       [:p (str @ubm)]])))

#_(defn ^:export main []
  (r/render-component
     [root-component]
     (js/document.getElementById "app")))

(defn home [comp]
  (r/render-component comp  (js/document.getElementById "app")))

(home [root-component])
