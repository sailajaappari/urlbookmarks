(ns urlbookmarks.core
    (:require [reagent.core :as r]
              [ajax.core :as ajax]))

(enable-console-print!)

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

(defn root-component []
  (let [url (r/atom nil)
        desc (r/atom nil)
        cate (r/atom nil)]
    (fn []
      [:div
       [:h1 "Url Bookmarks"]
       [:div 
        [:button "List All Bookmarks"]]
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

(defn ^:export main []
  (r/render-component
   [root-component]
   (js/document.getElementById "app")))
